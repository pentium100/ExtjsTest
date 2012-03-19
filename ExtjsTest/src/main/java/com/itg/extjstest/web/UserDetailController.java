package com.itg.extjstest.web;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.itg.extjstest.domain.Contract;
import com.itg.extjstest.domain.security.SecurityRole;
import com.itg.extjstest.domain.security.UserDetail;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@RooWebJson(jsonObject = UserDetail.class)
@Controller
@RequestMapping("/userdetails")
public class UserDetailController {

	@RequestMapping(value = "/logout", headers = "Accept=text/html")
	public String listMessageByType(HttpSession session) {

		session.invalidate();

		return "redirect:/login.jsp";
	}

	@RequestMapping(headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> listJson(

	@RequestParam(value = "page") int page,
			@RequestParam(value = "start") int start,
			@RequestParam(value = "limit") int limit, HttpServletRequest request

	) throws NoSuchAlgorithmException {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");

		SecurityContext context = SecurityContextHolder.getContext();

		List<UserDetail> result = UserDetail.findAllUserDetails();

		// MessageDigest md = MessageDigest.getInstance("MD5");

		// UserDetail userDetail =
		// UserDetail.findUserDetailsByUserNameEquals(context.getAuthentication().getName()).getSingleResult();
		String userName = context.getAuthentication().getName();

		Iterator<UserDetail> it = result.iterator();

		UserDetail user;
		while (it.hasNext()) {
			user = it.next();
			user.setPassword("");

			if (!request.isUserInRole("ROLE_ADMIN")) {
				if (!user.getUserName().equals(userName)) {
					it.remove();
				}
			}

		}

		HashMap<String, Object> map = new HashMap<String, Object>();

		// List<UserDetail> userDetails = new ArrayList<UserDetail>();
		// userDetails.addAll(result);
		map.put("success", true);
		map.put("total", UserDetail.countUserDetails());
		String resultJson = UserDetail.mapToJson(map, result);
		return new ResponseEntity<String>(resultJson, headers, HttpStatus.OK);

		// return new ResponseEntity<String>(UserDetail.toJsonArray(result),
		// headers, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<String> createFromJson(@RequestBody String json,
			HttpServletRequest request) {

		UserDetail userDetail = UserDetail.fromJsonToUserDetail(json);

		Md5PasswordEncoder md5 = new Md5PasswordEncoder();
		String md5Password = md5.encodePassword(userDetail.getPassword(), null);
		userDetail.setPassword(md5Password);

		for (SecurityRole role : userDetail.getRoles()) {
			SecurityRole role2 = SecurityRole
					.findSecurityRolesByRoleNameEquals(role.getRoleName())
					.getSingleResult();
			if (role2 != null) {
				role.setId(role2.getId());
				role.setVersion(role2.getVersion());
			}

		}

		userDetail.merge();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");

		HashMap<String, Object> map = new HashMap<String, Object>();
		List<UserDetail> userDetails = new ArrayList<UserDetail>();
		userDetail.setPassword("");
		userDetails.add(userDetail);
		map.put("success", true);
		String resultJson = UserDetail.mapToJson(map, userDetails);
		return new ResponseEntity<String>(resultJson, headers, HttpStatus.OK);

		// return new ResponseEntity<String>(headers, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, headers = "Accept=application/json")
	public ResponseEntity<String> updateFromJson(@RequestBody String json,
			HttpServletRequest request) {

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");

		UserDetail userDetail = UserDetail.fromJsonToUserDetail(json);

		if (!request.isUserInRole("ROLE_ADMIN")) {

			UserDetail userDetail2 = UserDetail
					.findUserDetailsByUserNameEquals(userDetail.getUserName())
					.getSingleResult();
			if (!userDetail.getPassword().equals("")) {
				userDetail2.setPassword(userDetail.getPassword());
			}

			userDetail = userDetail2;

		}

		if (userDetail.getPassword() != null
				&& (!userDetail.getPassword().equals(""))) {
			Md5PasswordEncoder md5 = new Md5PasswordEncoder();
			String md5Password = md5.encodePassword(userDetail.getPassword(),
					null);
			userDetail.setPassword(md5Password);
		}

		for (SecurityRole role : userDetail.getRoles()) {
			SecurityRole role2 = SecurityRole
					.findSecurityRolesByRoleNameEquals(role.getRoleName())
					.getSingleResult();
			if (role2 != null) {
				role.setId(role2.getId());
				role.setVersion(role2.getVersion());
			}
		}
		userDetail = userDetail.merge();
		if (userDetail == null) {
			return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
		}
		
		userDetail.setPassword("");

		HashMap<String, Object> map = new HashMap<String, Object>();
		List<UserDetail> userDetails = new ArrayList<UserDetail>();
		userDetails.add(userDetail);
		map.put("success", true);
		String resultJson = UserDetail.mapToJson(map, userDetails);
		return new ResponseEntity<String>(resultJson, headers, HttpStatus.OK);

		// return new ResponseEntity<String>(headers, HttpStatus.OK);
	}

}
