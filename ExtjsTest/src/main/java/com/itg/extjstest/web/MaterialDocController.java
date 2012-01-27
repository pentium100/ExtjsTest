package com.itg.extjstest.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.itg.extjstest.domain.Contract;
import com.itg.extjstest.domain.MaterialDoc;

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

@RooWebJson(jsonObject = MaterialDoc.class)
@Controller
@RequestMapping("/materialdocs")
public class MaterialDocController {
	
    @RequestMapping(headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> listJson(@RequestParam(value="page") int page, 
    		                               @RequestParam(value="start") int start,
    		                               @RequestParam(value="limit") int limit
    		) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<MaterialDoc> result = MaterialDoc.findAllMaterialDocs();
        
        
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
        materialDoc = materialDoc.merge();
        if (materialDoc == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        
        
		HashMap<String, Object> map = new HashMap<String, Object>();
		List<MaterialDoc> materialDocs = new ArrayList<MaterialDoc>();
		materialDocs.add(materialDoc);
		map.put("success", true);
		String resultJson = MaterialDoc.mapToJson(map, materialDocs);
        
		return new ResponseEntity<String>(resultJson, headers,
				HttpStatus.OK);

    }


	
    
}
