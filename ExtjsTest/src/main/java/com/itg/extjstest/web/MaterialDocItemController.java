package com.itg.extjstest.web;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.itg.extjstest.domain.MaterialDoc;
import com.itg.extjstest.domain.MaterialDocItem;
import com.itg.extjstest.repository.MaterialDocItemRepository;
import com.itg.extjstest.util.FilterItem;

import flexjson.JSONDeserializer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RooWebJson(jsonObject = MaterialDocItem.class)
@Controller
@RequestMapping("/materialdocitems/{queryMode}")
public class MaterialDocItemController {

	@Autowired
	private MaterialDocItemRepository materialDocItemRepository;

	@RequestMapping(headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> listJson(
			@RequestParam(value = "page") int page,
			@RequestParam(value = "start") int start,
			@RequestParam(value = "limit") int limit,
			@RequestParam(value = "filter", required = false) String filter,
			@PathVariable int queryMode

	) throws ParseException {

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");

		List<FilterItem> filters = new ArrayList<FilterItem>();
		if (filter != null) {
			filters = new JSONDeserializer<List<FilterItem>>()
					.use(null, ArrayList.class).use("values", FilterItem.class)
					// .use("values.value", ArrayList.class)
					.use("values.value", String.class).deserialize(filter);

		}
		HashMap<String, Object> map = new HashMap<String, Object>();
		Integer reccount = 0;
		List<MaterialDocItem> result = null;
		if (queryMode == 1) {
			FilterItem f = new FilterItem();
			f.setComparison("gt");
			f.setField("quantity");
			f.setType("int");
			f.setValue(String.valueOf(0));

			result = materialDocItemRepository.findMaterialDocItemsByFilter(filters,
					start, page, limit);
			map.put("total", materialDocItemRepository.countMaterialDocItemsByFilter(
					filters, start, page, limit));
		}

		if (queryMode == 2) {

			result = materialDocItemRepository.findIncomingMaterialDocItemsByFilter(
					filters, start, page, limit);
			map.put("total", materialDocItemRepository
					.countIncomingMaterialDocItemsByFilter(filters, start,
							page, limit));

		}

		for (MaterialDocItem i : result) {
			i.setBatchNo(i.getMaterialDoc().getBatchNo());
			i.setPlateNum(i.getMaterialDoc().getPlateNum());
			i.setDeliveryNote(i.getMaterialDoc().getDeliveryNote());
			i.setDocDate(i.getMaterialDoc().getDocDate());
			i.setWorkingNo(i.getMaterialDoc().getWorkingNo());
			i.setContractNo(i.getMaterialDoc().getContract().getContractNo());
			i.setWarehouse(i.getStockLocation().getStockLocation());
		}

		map.put("success", true);
		String resultJson = MaterialDocItem.mapToJson(map, result);
		return new ResponseEntity<String>(resultJson, headers, HttpStatus.OK);

		// return new
		// ResponseEntity<String>(MaterialDocItem.toJsonArray(result),
		// headers, HttpStatus.OK);

	}

	@RequestMapping(value = "/{lineId}", headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> showJson(@PathVariable("lineId") Long lineId) {
		MaterialDocItem materialdocitem = materialDocItemRepository
				.findMaterialDocItem(lineId);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		if (materialdocitem == null) {
			return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
		}

		List<MaterialDocItem> result = new ArrayList<MaterialDocItem>();
		result.add(materialdocitem);
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("total", 1);
		map.put("success", true);
		String resultJson = MaterialDocItem.mapToJson(map, result);
		return new ResponseEntity<String>(resultJson, headers, HttpStatus.OK);

		// return new ResponseEntity<String>(materialdocitem.toJson(), headers,
		// HttpStatus.OK);
	}


	@RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<String> createFromJson(@RequestBody String json) {
		MaterialDocItem materialDocItem = MaterialDocItem.fromJsonToMaterialDocItem(json);
		materialDocItemRepository.persist(materialDocItem);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		return new ResponseEntity<String>(headers, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<String> createFromJsonArray(@RequestBody String json) {
		for (MaterialDocItem materialDocItem: MaterialDocItem.fromJsonArrayToMaterialDocItems(json)) {
			materialDocItemRepository.persist(materialDocItem);
		}
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		return new ResponseEntity<String>(headers, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{lineId}", method = RequestMethod.PUT, headers = "Accept=application/json")
	public ResponseEntity<String> updateFromJson(@RequestBody String json, @PathVariable("lineId") Long lineId) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		MaterialDocItem materialDocItem = MaterialDocItem.fromJsonToMaterialDocItem(json);
		if (materialDocItemRepository.merge(materialDocItem) == null) {
			return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<String>(headers, HttpStatus.OK);
	}

	@RequestMapping(value = "/{lineId}", method = RequestMethod.DELETE, headers = "Accept=application/json")
	public ResponseEntity<String> deleteFromJson(@PathVariable("lineId") Long lineId) {
		MaterialDocItem materialDocItem = materialDocItemRepository.findMaterialDocItem(lineId);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		if (materialDocItem == null) {
			return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
		}
		materialDocItemRepository.remove(materialDocItem);
		return new ResponseEntity<String>(headers, HttpStatus.OK);
	}

	@RequestMapping(params = "find=ByLineId_up", headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> jsonFindMaterialDocItemsByLineId_up(@RequestParam("lineId_up") MaterialDocItem lineId_up) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		return new ResponseEntity<String>(MaterialDocItem.toJsonArray(materialDocItemRepository.findMaterialDocItemsByLineId_up(lineId_up).getResultList()), headers, HttpStatus.OK);
	}


}
