package com.itg.extjstest.web;

import com.itg.extjstest.domain.MaterialDocType;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RooWebJson(jsonObject = MaterialDocType.class)
@Controller
@RequestMapping("/materialdoctypes")
public class MaterialDocTypeController {
}
