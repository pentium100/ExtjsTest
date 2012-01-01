package com.itg.extjstest.web;

import com.itg.extjstest.domain.AfloatGoods;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RooWebJson(jsonObject = AfloatGoods.class)
@Controller
@RequestMapping("/afloatgoodses")
public class AfloatGoodsController {
}
