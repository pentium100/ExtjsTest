package com.itg.extjstest.web;

import com.itg.extjstest.domain.MaterialDocType;
import com.itg.extjstest.repository.MaterialDocTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RooWebJson(jsonObject = MaterialDocType.class)
@Controller
@RequestMapping("/materialdoctypes")
public class MaterialDocTypeController {

    @Autowired
    private MaterialDocTypeRepository materialDocTypeRepository;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> showJson(@PathVariable("id") Long id) {
        MaterialDocType materialDocType = materialDocTypeRepository.findMaterialDocType(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        if (materialDocType == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(materialDocType.toJson(), headers, HttpStatus.OK);
    }

    @RequestMapping(headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> listJson() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<MaterialDocType> result = materialDocTypeRepository.findAllMaterialDocTypes();
        return new ResponseEntity<String>(MaterialDocType.toJsonArray(result), headers, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createFromJson(@RequestBody String json) {
        MaterialDocType materialDocType = MaterialDocType.fromJsonToMaterialDocType(json);
        materialDocTypeRepository.persist(materialDocType);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createFromJsonArray(@RequestBody String json) {
        for (MaterialDocType materialDocType: MaterialDocType.fromJsonArrayToMaterialDocTypes(json)) {
            materialDocTypeRepository.persist(materialDocType);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> updateFromJson(@RequestBody String json, @PathVariable("id") Long id) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        MaterialDocType materialDocType = MaterialDocType.fromJsonToMaterialDocType(json);
        if (materialDocTypeRepository.merge(materialDocType) == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public ResponseEntity<String> deleteFromJson(@PathVariable("id") Long id) {
        MaterialDocType materialDocType = materialDocTypeRepository.findMaterialDocType(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (materialDocType == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        materialDocTypeRepository.remove(materialDocType);
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

    @RequestMapping(params = "find=ByDocType_txtEquals", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindMaterialDocTypesByDocType_txtEquals(@RequestParam("docType_txt") String docType_txt) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<String>(MaterialDocType.toJsonArray(materialDocTypeRepository.findMaterialDocTypesByDocType_txtEquals(docType_txt).getResultList()), headers, HttpStatus.OK);
    }

}
