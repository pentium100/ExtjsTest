package com.itg.extjstest.web;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.itg.extjstest.domain.Contract;
import com.itg.extjstest.domain.Inspection;
import com.itg.extjstest.domain.InspectionItem;
import com.itg.extjstest.domain.MaterialDocItem;
import com.itg.extjstest.util.FilterItem;

import flexjson.JSONDeserializer;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@RooWebJson(jsonObject = Inspection.class)
@Controller
@RequestMapping("/inspections")
public class InspectionController {
	
    @RequestMapping(headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> listJson(
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "start", required = false) Integer start,
			@RequestParam(value = "limit", required = false) Integer limit,
			@RequestParam(value = "filter", required = false) String filter) throws ParseException {
    	
		List<Inspection> result;
		List<FilterItem> filters = null;
		if (filter != null) {
			filters = new JSONDeserializer<List<FilterItem>>()
					.use(null, ArrayList.class).use("values", FilterItem.class)
					//.use("values.value", ArrayList.class)
					.use("values.value", String.class)
					.deserialize(filter);

		}
		result = Inspection.findInspectionByFilter(filters, start, page, limit);
    	for(Inspection i :result){
    		for(InspectionItem item:i.getItems()){
    			item.setContractNo(item.getMaterialDocItem().getMaterialDoc().getContract().getContractNo());
    			item.setModel_contract(item.getMaterialDocItem().getModel_contract());
    			item.setPlateNum(item.getMaterialDocItem().getMaterialDoc().getPlateNum());
    			item.setBatchNo(item.getMaterialDocItem().getMaterialDoc().getBatchNo());
    			item.setDeliveryNote(item.getMaterialDocItem().getMaterialDoc().getDeliveryNote());
    		}
    	}
		
		
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        
        
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("total", Inspection.countInspections());
		map.put("success", true);
		String resultJson = Inspection.mapToJson(map, result);
		return new ResponseEntity<String>(resultJson, headers, HttpStatus.OK);
        
        //return new ResponseEntity<String>(Inspection.toJsonArray(result), headers, HttpStatus.OK);
    }
    
    @RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createFromJson(@RequestBody String json) {
        Inspection inspection = Inspection.fromJsonToInspection(json);
        inspection.setContracts("");
        for(InspectionItem item : inspection.getItems()){
        	item.setInspection(inspection);
        	MaterialDocItem mdi = MaterialDocItem.findMaterialDocItem(item.getMaterialDocItem().getLineId());
        	String contractNo = mdi.getMaterialDoc().getContract().getContractNo();
        	
        	if(!inspection.getContracts().contains(contractNo)){
        		if(!inspection.getContracts().equals("")){
        			inspection.setContracts(inspection.getContracts()+",");	
        		}
        		inspection.setContracts(inspection.getContracts()+contractNo);
        	}
        }
        inspection = inspection.merge();
        inspection.updateIsLast();
        
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        
        
		for(InspectionItem item:inspection.getItems()){
			item.setContractNo(item.getMaterialDocItem().getMaterialDoc().getContract().getContractNo());
			item.setModel_contract(item.getMaterialDocItem().getModel_contract());
			item.setPlateNum(item.getMaterialDocItem().getMaterialDoc().getPlateNum());
			item.setBatchNo(item.getMaterialDocItem().getMaterialDoc().getBatchNo());
			item.setDeliveryNote(item.getMaterialDocItem().getMaterialDoc().getDeliveryNote());
		}
        
		List<Inspection> inspections = new ArrayList<Inspection>();
		inspections.add(inspection);
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("success", true);
		String resultJson = Inspection.mapToJson(map, inspections);
        
		return new ResponseEntity<String>(resultJson, headers, HttpStatus.OK);
    }
    
    

    @RequestMapping(method = RequestMethod.PUT, value = "/{id}", headers = "Accept=application/json")
    public ResponseEntity<String> updateFromJson(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        Inspection inspection = Inspection.fromJsonToInspection(json);
        inspection.setContracts("");
        for(InspectionItem item : inspection.getItems()){
        	item.setInspection(inspection);
        	MaterialDocItem mdi = MaterialDocItem.findMaterialDocItem(item.getMaterialDocItem().getLineId());
        	String contractNo = mdi.getMaterialDoc().getContract().getContractNo();
        	if(!inspection.getContracts().contains(contractNo)){
        		if(!inspection.getContracts().equals("")){
        			inspection.setContracts(inspection.getContracts()+",");	
        		}
        		
        		inspection.setContracts(inspection.getContracts()+contractNo);
        	}
        	
        }
        inspection = inspection.merge();
        inspection.updateIsLast();
		for(InspectionItem item:inspection.getItems()){
			item.setContractNo(item.getMaterialDocItem().getMaterialDoc().getContract().getContractNo());
			item.setModel_contract(item.getMaterialDocItem().getModel_contract());
			item.setPlateNum(item.getMaterialDocItem().getMaterialDoc().getPlateNum());
			item.setBatchNo(item.getMaterialDocItem().getMaterialDoc().getBatchNo());
			item.setDeliveryNote(item.getMaterialDocItem().getMaterialDoc().getDeliveryNote());
		}

        if (inspection == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        
        
		List<Inspection> inspections = new ArrayList<Inspection>();
		inspections.add(inspection);
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("success", true);
		String resultJson = Inspection.mapToJson(map, inspections);
		return new ResponseEntity<String>(resultJson, headers, HttpStatus.OK);
        //return new ResponseEntity<String>(headers, HttpStatus.OK);
    }
    
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public ResponseEntity<String> deleteFromJson(@PathVariable("id") Long id) {
        Inspection inspection = Inspection.findInspection(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        if (inspection == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        inspection.remove();
        inspection.updateIsLast();
        
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }
    
}
