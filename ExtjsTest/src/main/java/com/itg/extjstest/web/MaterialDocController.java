package com.itg.extjstest.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


import com.itg.extjstest.domain.Contract;
import com.itg.extjstest.domain.MaterialDoc;
import com.itg.extjstest.domain.MaterialDocItem;
import com.itg.extjstest.domain.MaterialDocType;
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

@RooWebJson(jsonObject = MaterialDoc.class)
@Controller
@RequestMapping("/materialdocs/{docType}")
public class MaterialDocController {
	
    @RequestMapping(headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> listJson(@RequestParam(value="page") int page, 
    		                               @RequestParam(value="start") int start,
    		                               @RequestParam(value="limit") int limit,
    		                               @RequestParam(value = "filter", required = false) String filter,
    		                               @PathVariable int docType
    		                               
    		) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        
		List<FilterItem> filters = new ArrayList<FilterItem>();
		if (filter != null) {
			filters = new JSONDeserializer<List<FilterItem>>()
					.use(null, ArrayList.class).use("values", FilterItem.class)
					//.use("values.value", ArrayList.class)
					.use("values.value", String.class)
					.deserialize(filter);

		}
		FilterItem f = new FilterItem();
		f.setComparison("eq");
		f.setField("docType");
		f.setType("int");
		f.setValue(String.valueOf(docType));
		
		filters.add(f);
	
        
        List<MaterialDoc> result = MaterialDoc.findMaterialDocsByFilter(filters, start, page, limit);
        
        for(MaterialDoc md:result){
        	for(MaterialDocItem mi:md.getItems()){
        		mi.getLineId_in();
        		mi.fillLineInInfo();
        	}
        	
        	if(md.getDocType().getDocType_txt().equals("移仓")){
        		Contract contract = new Contract();
        		md.setContract(contract);
        		
        		String targetWarehouse = "";
        		Iterator<MaterialDocItem> it = md.getItems().iterator();
            	while(it.hasNext()){
            		MaterialDocItem mi = it.next();
            		if(mi.getMoveType().equals("101")){
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
        
        //return new ResponseEntity<String>(MaterialDoc.toJsonArray(result), headers, HttpStatus.OK);
    }

    
    @RequestMapping(method = RequestMethod.PUT, value = "/{id}", headers = "Accept=application/json")
    public ResponseEntity<String> updateFromJson(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        
        headers.add("Content-Type", "application/json; charset=utf-8");        
        MaterialDoc materialDoc = MaterialDoc.fromJsonToMaterialDoc(json);
        
        Set<MaterialDocItem> items = materialDoc.getItems();
        List<MaterialDocItem> newItems = new ArrayList<MaterialDocItem>();
        for(MaterialDocItem item:items){
        	item.setMaterialDoc(materialDoc);
        	if(materialDoc.getDocType().getDocType_txt().equals("进仓")&&item.getMoveType().equals("101")){
        		item.setLineId_in(item);
        	}
        	
        	if(materialDoc.getDocType().getDocType_txt().equals("移仓")&&item.getMoveType().equals("351")){
        		MaterialDocItem newItem = new MaterialDocItem();
        		newItem.setLineId_in(item.getLineId_in());
        		newItem.setMoveType("101");
        		newItem.setDirection((short) 1);
        		newItem.setNetWeight(item.getNetWeight());
        		newItem.setWarehouse(materialDoc.getTargetWarehouse());
        		newItem.setMaterialDoc(materialDoc);
        		newItem.setModel_contract(item.getModel_contract());
        		newItem.setModel_tested(item.getModel_tested());
        		newItems.add(newItem);
        	}
        	
        	
        	
        }
        items.addAll(newItems);
        
        if(materialDoc.getDocType().getDocType_txt().equals("移仓")){
        	materialDoc.setContract(null);
        }
      
        
        materialDoc = materialDoc.merge();
        if (materialDoc == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        
        for(MaterialDocItem mi:materialDoc.getItems()){
        	
        	mi.fillLineInInfo();
        	
        }
        
        
        
		HashMap<String, Object> map = new HashMap<String, Object>();
		List<MaterialDoc> materialDocs = new ArrayList<MaterialDoc>();
		
        if(materialDoc.getDocType().getDocType_txt().equals("移仓")){
    		Contract contract = new Contract();
    		materialDoc.setContract(contract);

        }
		
		
		
		materialDocs.add(materialDoc);
		map.put("success", true);
		String resultJson = MaterialDoc.mapToJson(map, materialDocs);
        
		return new ResponseEntity<String>(resultJson, headers,
				HttpStatus.OK);

    }


    @RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createFromJson(@RequestBody String json) {
        MaterialDoc materialDoc = MaterialDoc.fromJsonToMaterialDoc(json);
        
        MaterialDocType type = MaterialDocType.findMaterialDocType(materialDoc.getDocType().getId());
        materialDoc.setDocType(type);
        
        if(materialDoc.getDocType().getDocType_txt().equals("移仓")){
        	materialDoc.setContract(null);
        }
        Set<MaterialDocItem> items = materialDoc.getItems();
        
        List<MaterialDocItem> newItems = new ArrayList<MaterialDocItem>();
        for(MaterialDocItem item:items){
        	item.setMaterialDoc(materialDoc);
        	if(materialDoc.getDocType().getDocType_txt().equals("进仓")&&item.getMoveType().equals("101")){
        		item.setLineId_in(item);
        	}
        	
        	if(materialDoc.getDocType().getDocType_txt().equals("移仓")&&item.getMoveType().equals("351")){
        		MaterialDocItem newItem = new MaterialDocItem();
        		newItem.setLineId_in(item.getLineId_in());
        		newItem.setMoveType("101");
        		newItem.setDirection((short) 1);
        		newItem.setNetWeight(item.getNetWeight());
        		newItem.setMaterialDoc(materialDoc);
        		newItem.setWarehouse(materialDoc.getTargetWarehouse());
        		newItems.add(newItem);
        	}
        	
        	
        	
        }
        items.addAll(newItems);
        
        
        
        
        materialDoc = materialDoc.merge();
        
        if(materialDoc.getDocType().getDocType_txt().equals("移仓")){
    		Contract contract = new Contract();
    		materialDoc.setContract(contract);

    		String targetWarehouse = "";
    		Iterator<MaterialDocItem> it = materialDoc.getItems().iterator();
        	while(it.hasNext()){
        		MaterialDocItem mi = it.next();
        		if(mi.getMoveType().equals("101")){
        			targetWarehouse = mi.getWarehouse();
        			it.remove();
        			
        		}
        	}
        	materialDoc.setTargetWarehouse(targetWarehouse);
    		
        }
        
        
		
        
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        
        HashMap<String, Object> map = new HashMap<String, Object>();
		List<MaterialDoc> materialDocs = new ArrayList<MaterialDoc>();
		
		for(MaterialDocItem mi:materialDoc.getItems()){
			mi.fillLineInInfo();
		}
		
		materialDocs.add(materialDoc);
		map.put("success", true);
		String resultJson = MaterialDoc.mapToJson(map, materialDocs);
        
		return new ResponseEntity<String>(resultJson, headers,
				HttpStatus.CREATED);
        
    }
	
    
}
