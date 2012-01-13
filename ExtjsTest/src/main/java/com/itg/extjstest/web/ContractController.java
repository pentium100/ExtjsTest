package com.itg.extjstest.web;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.itg.extjstest.domain.Contract;

import flexjson.JSONSerializer;
import flexjson.transformer.DateTransformer;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RooWebJson(jsonObject = Contract.class)
@Controller
@RequestMapping("/contracts")
public class ContractController {

	@RequestMapping(headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> listJson() {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		List<Contract> result = Contract.findAllContracts();
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("total", 2);
		map.put("success", true);
		map.put("contracts", result);
		String resultJson = new JSONSerializer()
				.exclude("*.class")
				.include("contracts")
				.include("contracts.items")
				.transform(new DateTransformer("yyyy-MM-dd HH:mm:ss"),Date.class)
				.include("items")
				.serialize(map);
		return new ResponseEntity<String>(resultJson, headers, HttpStatus.OK);
	}
	
	
}
