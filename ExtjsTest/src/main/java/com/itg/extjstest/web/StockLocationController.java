package com.itg.extjstest.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.itg.extjstest.domain.Message;
import com.itg.extjstest.domain.StockLocation;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@RooWebJson(jsonObject = StockLocation.class)
@Controller
@RequestMapping("/stocklocations")
public class StockLocationController {

	@RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<String> createFromJson(@RequestBody String json) {
		StockLocation stockLocation = StockLocation
				.fromJsonToStockLocation(json);
		stockLocation = stockLocation.merge();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");

		if (stockLocation == null) {
			return new ResponseEntity<String>(headers,
					HttpStatus.METHOD_FAILURE);
		}

		
		HashMap<String, Object> map = new HashMap<String, Object>();
		List<StockLocation> stockLocations = new ArrayList<StockLocation>();
		stockLocations.add(stockLocation);
		map.put("success", true);
		String resultJson = StockLocation.mapToJson(map, stockLocations);

		return new ResponseEntity<String>(resultJson, headers,
				HttpStatus.CREATED);
		
		//return new ResponseEntity<String>(headers, HttpStatus.CREATED);
	}

	@RequestMapping(method = RequestMethod.PUT, headers = "Accept=application/json")
	public ResponseEntity<String> updateFromJson(@RequestBody String json) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		StockLocation stockLocation = StockLocation
				.fromJsonToStockLocation(json);
		if (stockLocation.merge() == null) {
			return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
		}
		

		List<StockLocation> stockLocations = new ArrayList<StockLocation>();
		stockLocations.add(stockLocation);
		HashMap<String, Object> map = new HashMap<String, Object>();
		String resultJson = StockLocation.mapToJson(map,stockLocations);

		return new ResponseEntity<String>(resultJson, headers, HttpStatus.OK);
		
		
		
	}

	
    @RequestMapping(headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> listJson() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<StockLocation> result = StockLocation.findAllStockLocations();
        
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("total", result.size());
		map.put("success", true);
		String resultJson = StockLocation.mapToJson(map, result);
		return new ResponseEntity<String>(resultJson, headers, HttpStatus.OK);
        

    }
	
}
