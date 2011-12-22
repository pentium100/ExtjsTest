package com.itg.extjstest.web;

import com.itg.extjstest.domain.Contract;
import org.springframework.roo.addon.web.mvc.controller.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RooWebScaffold(path = "contracts", formBackingObject = Contract.class)
@RequestMapping("/contracts")
@Controller
public class ContractController {
}
