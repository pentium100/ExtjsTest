package com.itg.extjstest.web;

import com.itg.extjstest.domain.Message;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RooWebJson(jsonObject = Message.class)
@Controller
@RequestMapping("/messages")
public class MessageController {
}
