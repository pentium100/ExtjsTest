package com.itg.extjstest.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.itg.extjstest.domain.security.UserDetail;

@Controller
@RequestMapping("/listmessages")
public class MessageListController {

	@Autowired
	@Qualifier("authenticationManager")
	protected AuthenticationManager authenticationManager;

	@Autowired
	@Qualifier("jdbcTemplate")
	protected NamedParameterJdbcTemplate jdbcTemplate;

	@RequestMapping(headers = "Accept=text/html")
	public String listMessageByType(
			@RequestParam(value = "messageType", required = true) String messageType,
			@RequestParam(value = "id", required = true) Integer loginId,
			ModelMap model) {

		
		
		SecurityContext context = SecurityContextHolder.getContext();
		
		int intMessageType = Integer.valueOf(messageType).intValue();
		switch(intMessageType){
			case 1: messageType="供应"; break;
			case 2: messageType="需求"; break;
			case 3: messageType="敞口"; break;
			case 4: messageType="锁定"; break;
		}
		
		UserDetail user;
		
		model.addAttribute("messageType", messageType);
		
		
		if(!context.getAuthentication().getName().equals("anonymousUser")){
			
			user = (UserDetail)context.getAuthentication().getDetails();
			if(user!=null){
				model.addAttribute("userName", user.getUserName());
			}
			
			return "viewMessage";
		}
		
		String query = "select * from z_mt_oa_status where id = :loginId and logtime > :now ";

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("loginId", loginId);
		Date now = new Date();
		now = new Date(now.getTime() - (1 * 1000 * 60));
		params.put("now", now);

		List<Map<String, Object>> l = jdbcTemplate.queryForList(query, params);

		if (l.size() > 0) {

			
			List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
			grantedAuthorities.add(new GrantedAuthorityImpl("USER"));

			UsernamePasswordAuthenticationToken uat = new UsernamePasswordAuthenticationToken(
					"john", "admin", grantedAuthorities);

			user = new UserDetail();
			
			user.setUserName((String)l.get(0).get("userName"));
			
			uat.setDetails(user);
			//SecurityContext context = SecurityContextHolder.getContext();
			
			model.addAttribute("userName", user.getUserName());

			Authentication userAuth = authenticationManager.authenticate(uat);

			context.setAuthentication(userAuth);

			return "viewMessage";
			
		}else{
			return "redirect:/login.jsp";
		}


	}

}
