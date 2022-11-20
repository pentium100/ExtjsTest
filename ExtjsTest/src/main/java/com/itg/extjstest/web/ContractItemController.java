package com.itg.extjstest.web;

import com.itg.extjstest.domain.ContractItem;
import org.springframework.http.ResponseEntity;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RooWebJson(jsonObject = ContractItem.class)
@Controller
@RequestMapping("/contractitems")
public class ContractItemController {




}
