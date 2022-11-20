package com.itg.extjstest.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.itg.extjstest.repository.UserDetailRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
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
	@Autowired
	private UserDetailRepository userDetailRepository;

	@RequestMapping(headers = "Accept=text/html")
	public String listMessageByType(
			@RequestParam(value = "messageType", required = false) String messageType,
			@RequestParam(value = "id", required = false) Integer loginId,
			ModelMap model) {

		SecurityContext context = SecurityContextHolder.getContext();

		if ((messageType!=null)&&(!messageType.equals(""))) {
			int intMessageType = Integer.valueOf(messageType).intValue();
			switch (intMessageType) {
			case 1:
				messageType = "供应";
				break;
			case 2:
				messageType = "需求";
				break;
			case 3:
				messageType = "敞口";
				break;
			case 4:
				messageType = "锁定";
				break;
			}
		}
		UserDetail user = null;


		model.addAttribute("messageType", messageType);

		if (!context.getAuthentication().getName().equals("anonymousUser")) {

			try {
				
				user = userDetailRepository.findUserDetailsByUserNameEquals(context.getAuthentication().getName()).getSingleResult();
				
				
				
	
				if (user != null) {
					model.addAttribute("userName", user.getUserName());
					model.addAttribute("userLevel", user.getUserLevel());
				}
				
			} catch (Exception e) {
				Logger.getLogger(this.getClass()).error(e);

			}

			return "portal";
		}

		String query = "select * from z_mt_oa_status where id = :loginId and logtime > :now ";

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("loginId", loginId);
		Date now = new Date();
		now = new Date(now.getTime() - (1 * 1000 * 60));
		params.put("now", now);

		List<Map<String, Object>> l = jdbcTemplate.queryForList(query, params);

		if (l.size() > 0) {

			// List<GrantedAuthority> grantedAuthorities = new
			// ArrayList<GrantedAuthority>();
			// grantedAuthorities.add(new GrantedAuthorityImpl("USER"));

			UsernamePasswordAuthenticationToken uat = new UsernamePasswordAuthenticationToken(
					"john", "admin");

			user = new UserDetail();

			user.setUserName((String) l.get(0).get("userName"));
			user.setUserLevel((Integer) l.get(0).get("zlevel"));

			uat.setDetails(user);
			// SecurityContext context = SecurityContextHolder.getContext();

			model.addAttribute("userName", user.getUserName());
			model.addAttribute("userLevel", user.getUserLevel());

			Authentication userAuth = authenticationManager.authenticate(uat);

			context.setAuthentication(userAuth);

			return "portal";

		} else {
			return "redirect:/login.jsp";
		}

	}

}
