package com.itg.extjstest.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.itg.extjstest.domain.Contract;
import com.itg.extjstest.domain.ContractItem;
import com.itg.extjstest.domain.MaterialDoc;
import com.itg.extjstest.domain.MaterialDocItem;
import com.itg.extjstest.domain.MaterialDocType;
import com.itg.extjstest.domain.Message;
import com.itg.extjstest.util.FilterItem;

import flexjson.JSONDeserializer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@RooWebJson(jsonObject = MaterialDoc.class)
@Controller
@RequestMapping("/materialdocs/{docType}")
public class MaterialDocController {

	@Autowired
	@Qualifier("jdbcTemplate2")
	protected NamedParameterJdbcTemplate jdbcTemplate;

	@RequestMapping(headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> listJson(
			@RequestParam(value = "page") int page,
			@RequestParam(value = "start") int start,
			@RequestParam(value = "limit") int limit,
			@RequestParam(value = "filter", required = false) String filter,
			@PathVariable int docType

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
		FilterItem f = new FilterItem();
		f.setComparison("eq");
		f.setField("docType");
		f.setType("int");
		f.setValue(String.valueOf(docType));

		filters.add(f);

		List<MaterialDoc> result = MaterialDoc.findMaterialDocsByFilter(
				filters, start, page, limit);

		for (MaterialDoc md : result) {
			for (MaterialDocItem mi : md.getItems()) {
				mi.getLineId_in();
				mi.fillLineInInfo();
			}

			if (md.getDocType().getDocType_txt().equals("移仓")) {
				// Contract contract = new Contract();
				// md.setContract(contract);

				String targetWarehouse = "";
				Iterator<MaterialDocItem> it = md.getItems().iterator();
				while (it.hasNext()) {
					MaterialDocItem mi = it.next();
					if (mi.getMoveType().equals("101")) {
						targetWarehouse = mi.getWarehouse();
						it.remove();

					}
				}
				md.setTargetWarehouse(targetWarehouse);

			}
		}

		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("total", MaterialDoc.countMaterialDocs());
		map.put("success", true);
		String resultJson = MaterialDoc.mapToJson(map, result);
		return new ResponseEntity<String>(resultJson, headers, HttpStatus.OK);

		// return new ResponseEntity<String>(MaterialDoc.toJsonArray(result),
		// headers, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/{id}", headers = "Accept=application/json")
	public ResponseEntity<String> updateFromJson(@RequestBody String json,
			HttpServletRequest request) {
		HttpHeaders headers = new HttpHeaders();
		HashMap<String, Object> map = new HashMap<String, Object>();
		org.springframework.http.HttpStatus status = HttpStatus.CREATED;

		headers.add("Content-Type", "application/json; charset=utf-8");
		MaterialDoc materialDoc = MaterialDoc.fromJsonToMaterialDoc(json);

		
		String res = "";
		if (request.getParameter("focusUpdate")==null||!request.getParameter("focusUpdate").equals("true")) {
			res = checkContractQuantity(materialDoc);
		}
		
		if (!res.equals("")) {
			map.put("success", false);
			map.put("message", res);
			status = HttpStatus.MULTIPLE_CHOICES;
			String resultJson = MaterialDocItem.mapToJson(map, null);

			return new ResponseEntity<String>(resultJson, headers, status);

		}
		
		Set<MaterialDocItem> items = materialDoc.getItems();
		// List<MaterialDocItem> newItems = new ArrayList<MaterialDocItem>();

		for (MaterialDocItem item : items) {
			item.setMaterialDoc(materialDoc);
			if (materialDoc.getDocType().getDocType_txt().equals("进仓")
					&& item.getMoveType().equals("101")) {
				item.setLineId_in(item);
				item.setLineId_test(item);
			}


			if (materialDoc.getDocType().getDocType_txt().equals("出仓")
					) {

				if (item.getLineId_in().getLineId_test() == null) {
					item.setLineId_in(MaterialDocItem.findMaterialDocItem(item
							.getLineId_in().getLineId()));
				}
				
				item.setLineId_test(item.getLineId_in().getLineId_test());
			}
			
			if (materialDoc.getDocType().getDocType_txt().equals("移仓")
					&& item.getMoveType().equals("351")) {

				// MaterialDocItem newItem = new MaterialDocItem();

				if (item.getLineId_in().getLineId_test() == null) {
					item.setLineId_in(MaterialDocItem.findMaterialDocItem(item
							.getLineId_in().getLineId()));
				}

				item.setLineId_test(item.getLineId_in().getLineId_test());
				MaterialDocItem newItem = MaterialDocItem
						.findMaterialDocItemsByLineId_up(item)
						.getSingleResult();
				if (newItem == null) {
					newItem = new MaterialDocItem();
				}
				newItem.setLineId_test(item.getLineId_in());
				newItem.setLineId_in(newItem);
				newItem.setMoveType("101");
				newItem.setDirection((short) 1);
				newItem.setNetWeight(item.getNetWeight());
				newItem.setWarehouse(materialDoc.getTargetWarehouse());
				newItem.setMaterialDoc(materialDoc);
				newItem.setModel_contract(item.getModel_contract());
				newItem.setModel_tested(item.getModel_tested());
				items.add(newItem);
			}

		}

		// if(materialDoc.getDocType().getDocType_txt().equals("移仓")){
		// materialDoc.setContract(null);
		// }

		materialDoc = materialDoc.merge();
		if (materialDoc == null) {
			return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
		}

		for (MaterialDocItem mi : materialDoc.getItems()) {

			mi.fillLineInInfo();

		}

		List<MaterialDoc> materialDocs = new ArrayList<MaterialDoc>();

		// if(materialDoc.getDocType().getDocType_txt().equals("移仓")){
		// Contract contract = new Contract();
		// materialDoc.setContract(contract);

		// }

		materialDocs.add(materialDoc);
		map.put("success", true);
		String resultJson = MaterialDoc.mapToJson(map, materialDocs);

		return new ResponseEntity<String>(resultJson, headers, HttpStatus.OK);

	}

	@RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<String> createFromJson(@RequestBody String json,
			HttpServletRequest request) {
		MaterialDoc materialDoc = MaterialDoc.fromJsonToMaterialDoc(json);

		MaterialDocType type = MaterialDocType.findMaterialDocType(materialDoc
				.getDocType().getId());
		materialDoc.setDocType(type);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");

		// if(materialDoc.getDocType().getDocType_txt().equals("移仓")){
		// materialDoc.setContract(null);
		// }

		String res = "";
		if (request.getParameter("focusUpdate")==null||!request.getParameter("focusUpdate").equals("true")) {
			res = checkContractQuantity(materialDoc);
		}
		HashMap<String, Object> map = new HashMap<String, Object>();
		if (!res.equals("")) {
			map.put("success", false);
			map.put("message", res);
			HttpStatus status = HttpStatus.MULTIPLE_CHOICES;
			String resultJson = MaterialDocItem.mapToJson(map, null);

			return new ResponseEntity<String>(resultJson, headers, status);

		}

		Set<MaterialDocItem> items = materialDoc.getItems();

		List<MaterialDocItem> newItems = new ArrayList<MaterialDocItem>();
		for (MaterialDocItem item : items) {
			item.setLineId(null);
			item.setMaterialDoc(materialDoc);

			if (materialDoc.getDocType().getDocType_txt().equals("进仓")
					&& item.getMoveType().equals("101")) {
				item.setLineId_in(item);
				item.setLineId_test(item);

			}

			if (materialDoc.getDocType().getDocType_txt().equals("出仓")
					) {

				if (item.getLineId_in().getLineId_test() == null) {
					item.setLineId_in(MaterialDocItem.findMaterialDocItem(item
							.getLineId_in().getLineId()));
				}
				
				item.setLineId_test(item.getLineId_in().getLineId_test());
			}
						
			if (materialDoc.getDocType().getDocType_txt().equals("移仓")
					&& item.getMoveType().equals("351")) {
				if (item.getLineId_in().getLineId_test() == null) {
					item.setLineId_in(item.findMaterialDocItem(item
							.getLineId_in().getLineId()));
				}

				item.setLineId_test(item.getLineId_in().getLineId_test());

				MaterialDocItem newItem = new MaterialDocItem();
				newItem.setLineId_test(item.getLineId_test());
				newItem.setLineId_in(newItem);
				newItem.setLineId_up(item);
				newItem.setLineId(null);
				newItem.setMoveType("101");
				newItem.setDirection((short) 1);
				newItem.setNetWeight(item.getNetWeight());
				newItem.setMaterialDoc(materialDoc);
				newItem.setModel_contract(item.getModel_contract());
				newItem.setModel_tested(item.getModel_tested());

				newItem.setWarehouse(materialDoc.getTargetWarehouse());
				items.add(newItem);
				// newItems.add(newItem);
			}

		}
		// items.addAll(newItems);

		// materialDoc =
		materialDoc.setDocNo(null);
		materialDoc.persist();

		if (materialDoc.getDocType().getDocType_txt().equals("移仓")) {
			// Contract contract = new Contract();
			// materialDoc.setContract(contract);

			String targetWarehouse = "";
			Iterator<MaterialDocItem> it = materialDoc.getItems().iterator();
			while (it.hasNext()) {
				MaterialDocItem mi = it.next();
				if (mi.getMoveType().equals("101")) {
					targetWarehouse = mi.getWarehouse();
					it.remove();

				}
			}
			materialDoc.setTargetWarehouse(targetWarehouse);

		}

		List<MaterialDoc> materialDocs = new ArrayList<MaterialDoc>();

		for (MaterialDocItem mi : materialDoc.getItems()) {
			mi.fillLineInInfo();
		}

		materialDocs.add(materialDoc);
		map.put("success", true);
		String resultJson = MaterialDoc.mapToJson(map, materialDocs);

		return new ResponseEntity<String>(resultJson, headers,
				HttpStatus.CREATED);

	}

	private String checkContractQuantity(MaterialDoc m) {

		
		
		String query = "select isnull(sum(material_doc_item.net_weight),0)  as used_net_weight, "  
				+ "        isnull(sum(contract_item.quantity),0) as signed_net_weight "
				+ "		from contract_item  "
				+ "		left join material_doc_item on "
				+ "               contract_item.model = material_doc_item.model_contract  "
				+ "     left join material_doc on material_doc.doc_no = material_doc_item.material_doc "				                           				    				                           
				+ "                           and contract_item.contract = material_doc.contract "
				+ "                           and material_doc.doc_no <> :doc_no "
				+ " 	where contract_item.contract = :contract and contract_item.model = :model ";
		

		Map<String, Object> param = new HashMap<String, Object>();

		Contract contract = Contract.findContract(m.getContract().getId());
		param.put("contract", contract.getId());
		param.put("doc_no", m.getDocNo());
		for (MaterialDocItem i : m.getItems()) {
			param.put("model", i.getModel_contract());
			Map<String, Object> result2 = jdbcTemplate.queryForList(
					query.toString(), param).get(0);
			if ((i.getNetWeight() + (Double) result2.get("used_net_weight")) > (Double) (result2
					.get("signed_net_weight"))) {
				return "进仓数量大于合同签约数量!";
			}

		}
		return "";

	}

}
