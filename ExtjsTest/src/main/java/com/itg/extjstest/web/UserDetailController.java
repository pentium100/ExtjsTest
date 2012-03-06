package com.itg.extjstest.web;

import com.itg.extjstest.domain.security.UserDetail;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RooWebJson(jsonObject = UserDetail.class)
@Controller
@RequestMapping("/userdetails")
public class UserDetailController {
}
