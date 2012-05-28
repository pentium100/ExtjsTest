package com.itg.extjstest.web;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.persistence.Transient;

import com.itg.extjstest.domain.Contract;
import com.itg.extjstest.domain.ContractItem;
import com.itg.extjstest.domain.ContractType;
import com.itg.extjstest.util.ContractTypeObjectFactory;
import com.itg.extjstest.util.FilterItem;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import flexjson.transformer.DateTransformer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@RooWebJson(jsonObject = Contract.class)
@Controller
@RequestMapping("/contracts")
public class ContractController {

	@Autowired
	@Qualifier("jdbcTemplate2")
	protected NamedParameterJdbcTemplate jdbcTemplate;

	@RequestMapping(headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> listJson(
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "start", required = false) Integer start,
			@RequestParam(value = "limit", required = false) Integer limit,
			@RequestParam(value = "filter", required = false) String filter,
			@RequestParam(value = "byItems", required = false) Boolean byItems)
			throws ParseException {

		List<Contract> result;
		List<FilterItem> filters = null;
		if (filter != null) {
			filters = new JSONDeserializer<List<FilterItem>>()
					.use(null, ArrayList.class).use("values", FilterItem.class)
					// .use("values.value", ArrayList.class)
					.use("values.value", String.class).deserialize(filter);

		}
		if (byItems == null) {
			byItems = false;
		}
		result = Contract.findContractsByFilter(filters, start, page, limit,
				byItems);

		fillUsedQuantity(result);

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");

		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("total", Contract.countContracts());
		map.put("success", true);
		String resultJson = mapToJson(map, result);
		return new ResponseEntity<String>(resultJson, headers, HttpStatus.OK);
	}

	private void fillUsedQuantity(List<Contract> contracts) {
		// TODO Auto-generated method stub

		StringBuffer query = new StringBuffer();
		query.append("select sum(net_weight) as used from material_doc_item mi, material_doc md ");
		query.append("       where mi.material_doc = md.doc_no ");
		query.append("          and (md.doc_type = 1 or md.doc_type = 2) ");
		query.append("          and (md.cause ='采购' or md.cause = '销售') ");
		query.append("          and md.contract = :contract ");
		query.append("          and mi.model_contract = :model ");
		Map<String, Object> param = new HashMap<String, Object>();

		for (Contract contract : contracts) {

			for (ContractItem item : contract.getItems()) {
				param.put("contract", item.getContract().getId());
				param.put("model", item.getModel());
				List<Map<String, Object>> result = jdbcTemplate.queryForList(
						query.toString(), param);
				item.setUsedQuantity((Double) result.get(0).get("used"));

			}

		}

	}

	@RequestMapping(method = RequestMethod.PUT, value = "/{id}", headers = "Accept=application/json")
	public ResponseEntity<String> updateFromJson(@RequestBody String json) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");

		Contract contract = Contract.fromJsonToContract(json);

		for (ContractItem item : contract.getItems()) {
			item.setContract(contract);
		}

		contract = contract.merge();
		if (contract == null) {
			return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
		}

		HashMap<String, Object> map = new HashMap<String, Object>();
		List<Contract> contracts = new ArrayList<Contract>();
		contracts.add(contract);
		map.put("success", true);
		String resultJson = mapToJson(map, contracts);
		return new ResponseEntity<String>(resultJson, headers, HttpStatus.OK);

		// return new ResponseEntity<String>(headers, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<String> createFromJson(@RequestBody String json) {
		Contract contract = Contract.fromJsonToContract(json);

		for (ContractItem item : contract.getItems()) {
			item.setContract(contract);
		}

		contract = contract.merge();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		List<Contract> contracts = new ArrayList<Contract>();
		contracts.add(contract);
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("success", true);
		String resultJson = mapToJson(map, contracts);

		return new ResponseEntity<String>(resultJson, headers,
				HttpStatus.CREATED);
	}

	private String mapToJson(Map<String, Object> map, List<Contract> contracts) {
		map.put("contracts", contracts);
		String resultJson = new JSONSerializer()
				.exclude("*.class")
				.include("contracts")
				.include("contracts.items")
				.transform(new DateTransformer("yyyy-MM-dd HH:mm:ss"),
						Date.class)
				.transform(new ContractTypeObjectFactory(), ContractType.class)
				.serialize(map);

		return resultJson;

	}

}
