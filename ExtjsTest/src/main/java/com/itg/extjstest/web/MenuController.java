package com.itg.extjstest.web;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.servlet.http.HttpServletRequest;

import com.itg.extjstest.domain.ContractItem;
import com.itg.extjstest.domain.Menu;
import com.itg.extjstest.domain.security.UserDetail;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@RooWebJson(jsonObject = Menu.class)
@Controller
@RequestMapping("/menus")
public class MenuController {

	@RequestMapping(headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> listJson(
			@RequestParam(value = "node") String node,
			HttpServletRequest request) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		List<Menu> result;
		if (node.equals("root")) {

			EntityManager em = Menu.entityManager();
			TypedQuery<Menu> q = em.createQuery(
					"SELECT o FROM Menu AS o WHERE o.parent is null",
					Menu.class);

			result = q.getResultList();

		} else {
			Menu parent = Menu.findMenu(Long.valueOf(node));
			result = Menu.findMenusByParent(parent).getResultList();

			boolean needFilter = false;
			if (request.isUserInRole("ROLE_QUERY")) {
				Iterator<Menu> iterator = result.iterator();
				while (iterator.hasNext()) {
					Menu m = iterator.next();
					if (m.getText().equals("数据查询")) {
						needFilter = true;

					}
				}

				iterator = result.iterator();
				while (needFilter && iterator.hasNext()) {
					Menu m = iterator.next();
					if (!m.getText().equals("数据查询")
							&& (!m.getText().equals("用户管理"))) {
						iterator.remove();

					}
				}

			}

		}

		return new ResponseEntity<String>(Menu.toJsonArray(result), headers,
				HttpStatus.OK);
	}

	@RequestMapping(value = "/getUserInfo", headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> getUserInfo(HttpServletRequest request) {

		UserDetail user = UserDetail.findUserDetailsByUserNameEquals(
				request.getRemoteUser()).getSingleResult();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");

		return new ResponseEntity<String>(user.toJson(), headers, HttpStatus.OK);

	}

}
