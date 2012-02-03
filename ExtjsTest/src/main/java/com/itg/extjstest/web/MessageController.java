package com.itg.extjstest.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.itg.extjstest.domain.MaterialDoc;
import com.itg.extjstest.domain.Message;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@RooWebJson(jsonObject = Message.class)
@Controller
@RequestMapping("/messages")
public class MessageController {
	
	
	
    @RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createFromJson(@RequestBody String json) {
        Message message = Message.fromJsonToMessage(json);
        message = message.merge();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        
        
        if (message == null) {
            return new ResponseEntity<String>(headers, HttpStatus.METHOD_FAILURE);
        }
        
        
		HashMap<String, Object> map = new HashMap<String, Object>();
		List<Message> messages = new ArrayList<Message>();
		messages.add(message);
		map.put("success", true);
		String resultJson = Message.mapToJson(map, messages);
        
		return new ResponseEntity<String>(resultJson, headers,
				HttpStatus.CREATED);
        
    }
    
    @RequestMapping(method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> updateFromJson(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        Message message = Message.fromJsonToMessage(json);
        if (message.merge() == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        
        
		HashMap<String, Object> map = new HashMap<String, Object>();
		List<Message> messages = new ArrayList<Message>();
		messages.add(message);
		map.put("success", true);
		String resultJson = Message.mapToJson(map, messages);
        
		return new ResponseEntity<String>(resultJson, headers,
				HttpStatus.CREATED);
        
        
        //return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

    
    
}
