package com.itg.extjstest.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.itg.extjstest.domain.MaterialDoc;
import com.itg.extjstest.domain.MaterialDocItem;
import com.itg.extjstest.util.FilterItem;

import flexjson.JSONDeserializer;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@RooWebJson(jsonObject = MaterialDocItem.class)
@Controller
@RequestMapping("/materialdocitems/{queryMode}")
public class MaterialDocItemController {

	@RequestMapping(headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> listJson(
			@RequestParam(value = "page") int page,
			@RequestParam(value = "start") int start,
			@RequestParam(value = "limit") int limit,
			@RequestParam(value = "filter", required = false) String filter,
			@PathVariable int queryMode

	) {

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");

		List<FilterItem> filters = new ArrayList<FilterItem>();
		if (filter != null) {
			filters = new JSONDeserializer<List<FilterItem>>()
					.use(null, ArrayList.class).use("values", FilterItem.class)
					// .use("values.value", ArrayList.class)
					.use("values.value", String.class).deserialize(filter);

		}

		if (queryMode == 1) {
			FilterItem f = new FilterItem();
			f.setComparison("gt");
			f.setField("quantity");
			f.setType("int");
			f.setValue(String.valueOf(0));
		}

		List<MaterialDocItem> result = MaterialDocItem.findMaterialDocItemsByFilter(filters, start, page, limit);
		
		//List<MaterialDocItem> result = MaterialDocItem
		//		.findAllMaterialDocItems();
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("total", MaterialDocItem.countMaterialDocItems());
		map.put("success", true);
		String resultJson = MaterialDocItem.mapToJson(map, result);
		return new ResponseEntity<String>(resultJson, headers, HttpStatus.OK);
		
		//return new ResponseEntity<String>(MaterialDocItem.toJsonArray(result),
		//		headers, HttpStatus.OK);

	}

}