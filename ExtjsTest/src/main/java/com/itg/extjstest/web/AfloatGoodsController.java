package com.itg.extjstest.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.itg.extjstest.domain.AfloatGoods;
import com.itg.extjstest.domain.AfloatGoodsItem;
import com.itg.extjstest.util.FilterItem;

import flexjson.JSONDeserializer;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@RooWebJson(jsonObject = AfloatGoods.class)
@Controller
@RequestMapping("/afloatgoodses")
public class AfloatGoodsController {

	@RequestMapping(headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> listJson(
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "start", required = false) Integer start,
			@RequestParam(value = "limit", required = false) Integer limit,
			@RequestParam(value = "filter", required = false) String filter) {
		
		
		
		List<AfloatGoods> result;
		List<FilterItem> filters = null;
		if (filter != null) {
			filters = new JSONDeserializer<List<FilterItem>>()
					.use(null, ArrayList.class).use("values", FilterItem.class)
					//.use("values.value", ArrayList.class)
					.use("values.value", String.class)
					.deserialize(filter);

		}
		result = AfloatGoods.findContractsByFilter(filters, start, page, limit);
		
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		//List<AfloatGoods> result = AfloatGoods.findAllAfloatGoodses();
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("total", AfloatGoods.countAfloatGoodses());
		map.put("success", true);
		String resultJson = AfloatGoods.mapToJson(map, result);
		return new ResponseEntity<String>(resultJson, headers, HttpStatus.OK);
		
		//return new ResponseEntity<String>(AfloatGoods.toJsonArray(result),
		//		headers, HttpStatus.OK);
		
		
		
	}

	@RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<String> createFromJson(@RequestBody String json) {
		AfloatGoods afloatGoods = AfloatGoods.fromJsonToAfloatGoods(json);
		
		for(AfloatGoodsItem item : afloatGoods.getItems()){
			item.setAfloatGoods(afloatGoods);
		}
		afloatGoods = afloatGoods.merge();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		
		
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
		headers.add("Content-Type", "application/json");
		AfloatGoods afloatGoods = AfloatGoods.fromJsonToAfloatGoods(json);
		
		for(AfloatGoodsItem item : afloatGoods.getItems()){
			item.setAfloatGoods(afloatGoods);
		}
		afloatGoods = afloatGoods.merge();
		if ( afloatGoods== null) {
			return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
		}
		
		
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		List<AfloatGoods> afloatGoodses = new ArrayList<AfloatGoods>();
		afloatGoodses.add(afloatGoods);
		map.put("success", true);
		String resultJson = AfloatGoods.mapToJson(map, afloatGoodses);
		return new ResponseEntity<String>(resultJson, headers, HttpStatus.OK);
		

	}

}
