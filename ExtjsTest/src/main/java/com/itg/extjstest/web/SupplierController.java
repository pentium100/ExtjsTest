package com.itg.extjstest.web;

import com.itg.extjstest.domain.Supplier;
import org.springframework.roo.addon.web.mvc.controller.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RooWebScaffold(path = "suppliers", formBackingObject = Supplier.class)
@RequestMapping("/suppliers")
@Controller
public class SupplierController {
}
