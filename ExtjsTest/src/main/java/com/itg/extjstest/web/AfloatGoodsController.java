package com.itg.extjstest.web;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.itg.extjstest.domain.AfloatGoods;
import com.itg.extjstest.domain.AfloatGoodsItem;
import com.itg.extjstest.repository.AfloatGoodsRepository;
import com.itg.extjstest.util.FilterItem;
import com.itg.extjstest.util.SortItem;

import flexjson.JSONDeserializer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RooWebJson(jsonObject = AfloatGoods.class)
@Controller
@RequestMapping("/afloatgoodses")
public class AfloatGoodsController {

	@Autowired
	private AfloatGoodsRepository afloatGoodsRepository;

	@RequestMapping(headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> listJson(
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "start", required = false) Integer start,
			@RequestParam(value = "limit", required = false) Integer limit,
			@RequestParam(value = "filter", required = false) String filter,
			@RequestParam(value = "sort", required = false) String sort)
			throws ParseException {

		List<AfloatGoods> result;
		List<FilterItem> filters = null;
		if (filter != null) {
			filters = new JSONDeserializer<List<FilterItem>>()
					.use(null, ArrayList.class).use("values", FilterItem.class)
					// .use("values.value", ArrayList.class)
					.use("values.value", String.class).deserialize(filter);

		}
		
		List<SortItem> sorts = null;
		if (sort !=null){
			sorts =  new JSONDeserializer<List<SortItem>>()
					.use(null, ArrayList.class).use("values", SortItem.class)
					.deserialize(sort);
		}
		result = afloatGoodsRepository.findAfloatGoodsByFilter(filters, start, page,
				limit, sorts);

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		// List<AfloatGoods> result = AfloatGoods.findAllAfloatGoodses();

		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("total", afloatGoodsRepository.countAfloatGoodses());
		map.put("success", true);
		String resultJson = AfloatGoods.mapToJson(map, result);
		return new ResponseEntity<String>(resultJson, headers, HttpStatus.OK);

		// return new ResponseEntity<String>(AfloatGoods.toJsonArray(result),
		// headers, HttpStatus.OK);

	}

	@RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<String> createFromJson(@RequestBody String json) {
		AfloatGoods afloatGoods = AfloatGoods.fromJsonToAfloatGoods(json);

		for (AfloatGoodsItem item : afloatGoods.getItems()) {
			item.setAfloatGoods(afloatGoods);
		}
		afloatGoods = afloatGoodsRepository.merge(afloatGoods);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");

		HashMap<String, Object> map = new HashMap<String, Object>();
		List<AfloatGoods> afloatGoodses = new ArrayList<AfloatGoods>();
		afloatGoodses.add(afloatGoods);
		map.put("success", true);
		String resultJson = AfloatGoods.mapToJson(map, afloatGoodses);
		return new ResponseEntity<String>(resultJson, headers, HttpStatus.OK);

	}

	@RequestMapping(method = RequestMethod.PUT, value = "/{id}", headers = "Accept=application/json")
	public ResponseEntity<String> updateFromJson(@RequestBody String json) {

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		AfloatGoods afloatGoods = AfloatGoods.fromJsonToAfloatGoods(json);

		for (AfloatGoodsItem item : afloatGoods.getItems()) {
			item.setAfloatGoods(afloatGoods);
		}
		afloatGoods = afloatGoodsRepository.merge(afloatGoods);
		if (afloatGoods == null) {
			return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
		}

		HashMap<String, Object> map = new HashMap<String, Object>();
		List<AfloatGoods> afloatGoodses = new ArrayList<AfloatGoods>();
		afloatGoodses.add(afloatGoods);
		map.put("success", true);
		String resultJson = AfloatGoods.mapToJson(map, afloatGoodses);
		return new ResponseEntity<String>(resultJson, headers, HttpStatus.OK);

	}


	@RequestMapping(value = "/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> showJson(@PathVariable("id") Long id) {
		AfloatGoods afloatGoods = afloatGoodsRepository.findAfloatGoods(id);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		if (afloatGoods == null) {
			return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<String>(afloatGoods.toJson(), headers, HttpStatus.OK);
	}

	@RequestMapping(value = "/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<String> createFromJsonArray(@RequestBody String json) {
		for (AfloatGoods afloatGoods: AfloatGoods.fromJsonArrayToAfloatGoodses(json)) {
			afloatGoodsRepository.persist(afloatGoods);
		}
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		return new ResponseEntity<String>(headers, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
	public ResponseEntity<String> deleteFromJson(@PathVariable("id") Long id) {
		AfloatGoods afloatGoods = afloatGoodsRepository.findAfloatGoods(id);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		if (afloatGoods == null) {
			return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
		}
		afloatGoodsRepository.remove(afloatGoods);
		return new ResponseEntity<String>(headers, HttpStatus.OK);
	}

}
