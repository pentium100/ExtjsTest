// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.itg.extjstest.web;

import com.itg.extjstest.domain.MaterialDocItem;
import com.itg.extjstest.web.MaterialDocItemController;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

privileged aspect MaterialDocItemController_Roo_Controller_Json {
    
    @RequestMapping(value = "/{lineId}", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> MaterialDocItemController.showJson(@PathVariable("lineId") Long lineId) {
        MaterialDocItem materialdocitem = MaterialDocItem.findMaterialDocItem(lineId);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        if (materialdocitem == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(materialdocitem.toJson(), headers, HttpStatus.OK);
    }
    
    @RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> MaterialDocItemController.createFromJson(@RequestBody String json) {
        MaterialDocItem materialDocItem = MaterialDocItem.fromJsonToMaterialDocItem(json);
        materialDocItem.persist();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }
    
    @RequestMapping(value = "/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> MaterialDocItemController.createFromJsonArray(@RequestBody String json) {
        for (MaterialDocItem materialDocItem: MaterialDocItem.fromJsonArrayToMaterialDocItems(json)) {
            materialDocItem.persist();
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }
    
    @RequestMapping(method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> MaterialDocItemController.updateFromJson(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        MaterialDocItem materialDocItem = MaterialDocItem.fromJsonToMaterialDocItem(json);
        if (materialDocItem.merge() == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> MaterialDocItemController.updateFromJsonArray(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        for (MaterialDocItem materialDocItem: MaterialDocItem.fromJsonArrayToMaterialDocItems(json)) {
            if (materialDocItem.merge() == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
        }
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/{lineId}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public ResponseEntity<String> MaterialDocItemController.deleteFromJson(@PathVariable("lineId") Long lineId) {
        MaterialDocItem materialDocItem = MaterialDocItem.findMaterialDocItem(lineId);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (materialDocItem == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        materialDocItem.remove();
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }
    
}