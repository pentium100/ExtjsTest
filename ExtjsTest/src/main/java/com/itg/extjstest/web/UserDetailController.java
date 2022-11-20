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

import com.itg.extjstest.repository.UserDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RooWebJson(jsonObject = UserDetail.class)
@Controller
@RequestMapping("/userdetails")
public class UserDetailController {


	private final UserDetailRepository userDetailRepository;


	@Autowired
	public UserDetailController( UserDetailRepository userDetailRepository){
		this.userDetailRepository = userDetailRepository;
	}


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

		List<UserDetail> result = userDetailRepository.findAllUserDetails();

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
		map.put("total", userDetailRepository.countUserDetails());
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

		userDetailRepository.merge(userDetail);
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

		UserDetail userDetail2 = userDetailRepository.findUserDetailsByUserNameEquals(
				userDetail.getUserName()).getSingleResult();

		if (!request.isUserInRole("ROLE_ADMIN")) {

			if (!userDetail.getPassword().equals("")) {

				Md5PasswordEncoder md5 = new Md5PasswordEncoder();
				String md5Password = md5.encodePassword(
						userDetail.getPassword(), null);
				userDetail.setPassword(md5Password);
				userDetail2.setPassword(userDetail.getPassword());
			}

			userDetail = userDetail2;

		}

		if (request.isUserInRole("ROLE_ADMIN")) {

			if (userDetail.getPassword() != null
					&& (!userDetail.getPassword().equals(""))) {
				Md5PasswordEncoder md5 = new Md5PasswordEncoder();
				String md5Password = md5.encodePassword(
						userDetail.getPassword(), null);
				userDetail.setPassword(md5Password);
			} else {
				userDetail.setPassword(userDetail2.getPassword());
			}

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
		userDetail = userDetailRepository.merge(userDetail);
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


	@RequestMapping(value = "/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> showJson(@PathVariable("id") Long id) {
		UserDetail userDetail = userDetailRepository.findUserDetail(id);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		if (userDetail == null) {
			return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<String>(userDetail.toJson(), headers, HttpStatus.OK);
	}

	@RequestMapping(value = "/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<String> createFromJsonArray(@RequestBody String json) {
		for (UserDetail userDetail: UserDetail.fromJsonArrayToUserDetails(json)) {
			userDetailRepository.persist(userDetail);
		}
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		return new ResponseEntity<String>(headers, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
	public ResponseEntity<String> deleteFromJson(@PathVariable("id") Long id) {
		UserDetail userDetail = userDetailRepository.findUserDetail(id);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		if (userDetail == null) {
			return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
		}
		userDetailRepository.remove(userDetail);
		return new ResponseEntity<String>(headers, HttpStatus.OK);
	}

	@RequestMapping(params = "find=ByUserNameEquals", headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> jsonFindUserDetailsByUserNameEquals(@RequestParam("userName") String userName) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		return new ResponseEntity<String>(UserDetail.toJsonArray(userDetailRepository.findUserDetailsByUserNameEquals(userName).getResultList()), headers, HttpStatus.OK);
	}


}
