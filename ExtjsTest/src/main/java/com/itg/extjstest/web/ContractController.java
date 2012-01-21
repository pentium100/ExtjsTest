package com.itg.extjstest.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.itg.extjstest.domain.Contract;
import com.itg.extjstest.domain.ContractType;
import com.itg.extjstest.util.ContractTypeObjectFactory;

import flexjson.JSONSerializer;
import flexjson.transformer.DateTransformer;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
		map.put("total", Contract.countContracts());
		map.put("success", true);
		String resultJson = mapToJson(map, result);
		return new ResponseEntity<String>(resultJson, headers, HttpStatus.OK);
	}
	
	
    @RequestMapping(method = RequestMethod.PUT, value = "/{id}" , headers = "Accept=application/json")
    public ResponseEntity<String> updateFromJson(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json;charset=utf-8");
        
        Contract contract = Contract.fromJsonToContract(json);
        contract = contract.merge();
        if ( contract == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        
		HashMap<String, Object> map = new HashMap<String, Object>();
		List<Contract> contracts = new ArrayList<Contract>();
		contracts.add(contract);
		map.put("success", true);
		String resultJson = mapToJson(map, contracts);
		return new ResponseEntity<String>(resultJson, headers, HttpStatus.OK);
        
        //return new ResponseEntity<String>(headers, HttpStatus.OK);
    }
	
    @RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createFromJson(@RequestBody String json) {
        Contract contract = Contract.fromJsonToContract(json);
        contract = contract.merge();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
		List<Contract> contracts = new ArrayList<Contract>();
		contracts.add(contract);
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("success", true);
		String resultJson = mapToJson(map, contracts);
        
        return new ResponseEntity<String>(resultJson, headers, HttpStatus.CREATED);
    }
    
    private String mapToJson(Map<String, Object> map, List<Contract> contracts){
		map.put("contracts", contracts);
		String resultJson = new JSONSerializer()
				.exclude("*.class")
				.include("contracts")
				.include("contracts.items")
				.transform(new DateTransformer("yyyy-MM-dd HH:mm:ss"),Date.class)
				.transform(new ContractTypeObjectFactory(),ContractType.class)
				.serialize(map);
		
		return resultJson;
    	
    }

}
