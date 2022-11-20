package com.itg.extjstest.web;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.itg.extjstest.domain.Message;
import com.itg.extjstest.domain.StockLocation;
import com.itg.extjstest.repository.StockLocationRepository;
import com.itg.extjstest.util.FilterItem;

import flexjson.JSONDeserializer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RooWebJson(jsonObject = StockLocation.class)
@Controller
@RequestMapping("/stocklocations")
public class StockLocationController {


	@Autowired
	private StockLocationRepository stockLocationRepository;
	@RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<String> createFromJson(@RequestBody String json) {
		StockLocation stockLocation = StockLocation
				.fromJsonToStockLocation(json);
		stockLocation = stockLocationRepository.merge(stockLocation);
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

		// return new ResponseEntity<String>(headers, HttpStatus.CREATED);
	}

	@RequestMapping(method = RequestMethod.PUT, headers = "Accept=application/json")
	public ResponseEntity<String> updateFromJson(@RequestBody String json) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		StockLocation stockLocation = StockLocation
				.fromJsonToStockLocation(json);
		if (stockLocationRepository.merge(stockLocation) == null) {
			return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
		}

		List<StockLocation> stockLocations = new ArrayList<StockLocation>();
		stockLocations.add(stockLocation);
		HashMap<String, Object> map = new HashMap<String, Object>();
		String resultJson = StockLocation.mapToJson(map, stockLocations);

		return new ResponseEntity<String>(resultJson, headers, HttpStatus.OK);

	}

	@RequestMapping(headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> listJson(
			@RequestParam(value = "page") int page,
			@RequestParam(value = "start") int start,
			@RequestParam(value = "limit") int limit,
			@RequestParam(value = "filter", required = false) String filter

	) throws ParseException {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		
		List<FilterItem> filters = new ArrayList<FilterItem>();
		if (filter != null) {
			filters = new JSONDeserializer<List<FilterItem>>()
					.use(null, ArrayList.class).use("values", FilterItem.class)
					.use("values.value", String.class).deserialize(filter);

		}
		
		List<StockLocation> result = stockLocationRepository.findStockLocationByFilter(filters, start, page, limit);

		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("total", result.size());
		map.put("success", true);
		String resultJson = StockLocation.mapToJson(map, result);
		return new ResponseEntity<String>(resultJson, headers, HttpStatus.OK);

	}
	@RequestMapping(value = "/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> showJson(@PathVariable("id") Long id) {
		StockLocation stockLocation = stockLocationRepository.findStockLocation(id);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		if (stockLocation == null) {
			return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<String>(stockLocation.toJson(), headers, HttpStatus.OK);
	}

	@RequestMapping(value = "/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<String> createFromJsonArray(@RequestBody String json) {
		for (StockLocation stockLocation: StockLocation.fromJsonArrayToStockLocations(json)) {
			stockLocationRepository.persist(stockLocation);
		}
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		return new ResponseEntity<String>(headers, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
	public ResponseEntity<String> deleteFromJson(@PathVariable("id") Long id) {
		StockLocation stockLocation = stockLocationRepository.findStockLocation(id);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		if (stockLocation == null) {
			return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
		}
		stockLocationRepository.remove(stockLocation);
		return new ResponseEntity<String>(headers, HttpStatus.OK);
	}

}
