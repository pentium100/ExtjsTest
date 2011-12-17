package com.itg.extjstest.web;

import com.itg.extjstest.domain.ContractItem;
import org.springframework.roo.addon.web.mvc.controller.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RooWebScaffold(path = "contractitems", formBackingObject = ContractItem.class)
@RequestMapping("/contractitems")
@Controller
public class ContractItemController {
}
