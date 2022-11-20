package com.itg.extjstest.web;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.itg.extjstest.domain.Contract;
import com.itg.extjstest.domain.MaterialDoc;
import com.itg.extjstest.domain.Message;
import com.itg.extjstest.repository.MessageRepository;
import com.itg.extjstest.util.FilterItem;

import flexjson.JSONDeserializer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@RooWebJson(jsonObject = Message.class)
@Controller
@RequestMapping("/messages")
public class MessageController {


	@Autowired
	private MessageRepository messageRepository;
	@RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<String> createFromJson(@RequestBody String json) {
		Message message = Message.fromJsonToMessage(json);
		message.setLastChangeTime(new Date());
		message = messageRepository.merge(message);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		if (message == null) {
			return new ResponseEntity<String>(headers,
					HttpStatus.METHOD_FAILURE);
		}

		HashMap<String, Object> map = new HashMap<String, Object>();
		List<Message> messages = new ArrayList<Message>();
		messages.add(message);
		map.put("success", true);
		String resultJson = Message.mapToJson(map, messages);

		return new ResponseEntity<String>(resultJson, headers,
				HttpStatus.CREATED);

	}

	@RequestMapping(method = RequestMethod.PUT, value = "/{id}", headers = "Accept=application/json")
	public ResponseEntity<String> updateFromJson(@RequestBody String json,
			HttpServletRequest request) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		Message message = Message.fromJsonToMessage(json);
		List<Message> messages = new ArrayList<Message>();
		HashMap<String, Object> map = new HashMap<String, Object>();
		org.springframework.http.HttpStatus status = HttpStatus.CREATED;

		String validateMessage = message.validateObject(request.getLocale());

		if (!validateMessage.equals("")) {
			map.put("success", false);
			map.put("message", validateMessage);
			status = HttpStatus.NOT_ACCEPTABLE;
			String resultJson = Message.mapToJson(map, messages);
			return new ResponseEntity<String>(resultJson, headers, status);

		}

		try {
			message.setLastChangeTime(new Date());
			message = messageRepository.merge(message);

			messages.add(message);
			map.put("success", true);

		} catch (Exception e) {

			map.put("success", false);
			map.put("message", e.getLocalizedMessage());
			status = HttpStatus.NOT_ACCEPTABLE;
		}

		String resultJson = Message.mapToJson(map, messages);

		return new ResponseEntity<String>(resultJson, headers, status);

		// if (message == null) {
		// return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
		// }

		// return new ResponseEntity<String>(headers, HttpStatus.OK);
	}

	@RequestMapping(headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> listJson(

			@RequestParam(value = "page", required = true) Integer page,
			@RequestParam(value = "start", required = false) Integer start,
			@RequestParam(value = "limit", required = false) Integer limit,
			@RequestParam(value = "filter", required = false) String filter,
			@RequestParam(value = "messageType", required = false) String messageType) throws ParseException {

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");

		List<Message> result;
		List<FilterItem> filters = new ArrayList<FilterItem>();
		if (filter != null) {
			filters = new JSONDeserializer<List<FilterItem>>()
					.use(null, ArrayList.class).use("values", FilterItem.class)
					// .use("values.value", ArrayList.class)
					.use("values.value", String.class).deserialize(filter);

		}

		if (messageType != null) {
			FilterItem typeFilter = new FilterItem();
			typeFilter.setComparison("eq");
			typeFilter.setField("type");
			typeFilter.setValue(messageType);
			typeFilter.setType("string");

			filters.add(typeFilter);
		}

		// List<Message> result = Message.findAllMessages();
		result = messageRepository.findMessagesByFilter(filters, start, page, limit);

		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("total", result.size());
		map.put("success", true);
		String resultJson = Message.mapToJson(map, result);
		return new ResponseEntity<String>(resultJson, headers, HttpStatus.OK);

	}



	@RequestMapping(value = "/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> showJson(@PathVariable("id") Long id) {
		Message message = messageRepository.findMessage(id);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		if (message == null) {
			return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<String>(message.toJson(), headers, HttpStatus.OK);
	}

	@RequestMapping(value = "/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<String> createFromJsonArray(@RequestBody String json) {
		for (Message message: Message.fromJsonArrayToMessages(json)) {
			messageRepository.persist(message);
		}
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		return new ResponseEntity<String>(headers, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
	public ResponseEntity<String> deleteFromJson(@PathVariable("id") Long id) {
		Message message = messageRepository.findMessage(id);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		if (message == null) {
			return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
		}
		messageRepository.remove(message);
		return new ResponseEntity<String>(headers, HttpStatus.OK);
	}


	// @RequestMapping(headers = "Accept=application/json")
	// @ResponseBody
	// public ResponseEntity<String> listJson() {
	// HttpHeaders headers = new HttpHeaders();
	// headers.add("Content-Type", "application/json; charset=utf-8");
	// List<Message> result = Message.findAllMessages();

	// HashMap<String, Object> map = new HashMap<String, Object>();
	// map.put("total", result.size());
	// map.put("success", true);
	// String resultJson = Message.mapToJson(map, result);
	// return new ResponseEntity<String>(resultJson, headers, HttpStatus.OK);

	// }

}
