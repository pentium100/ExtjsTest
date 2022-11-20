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

import com.itg.extjstest.repository.MenuRepository;
import com.itg.extjstest.repository.UserDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RooWebJson(jsonObject = Menu.class)
@Controller
@RequestMapping("/menus")
public class MenuController {
	@Autowired
	private UserDetailRepository userDetailRepository;
	@Autowired
	private MenuRepository menuRepository;

	@RequestMapping(headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> listJson(
			@RequestParam(value = "node") String node,
			HttpServletRequest request) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		List<Menu> result;
		if (node.equals("root")) {

			EntityManager em = menuRepository.entityManager();
			TypedQuery<Menu> q = em.createQuery(
					"SELECT o FROM Menu AS o WHERE o.parent is null",
					Menu.class);

			result = q.getResultList();

		} else {
			Menu parent = menuRepository.findMenu(Long.valueOf(node));
			result =  menuRepository.findMenusByParent(parent).getResultList();

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

		UserDetail user = userDetailRepository.findUserDetailsByUserNameEquals(
				request.getRemoteUser()).getSingleResult();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");

		return new ResponseEntity<String>(user.toJson(), headers, HttpStatus.OK);

	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> showJson(@PathVariable("id") Long id) {
		Menu menu = menuRepository.findMenu(id);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		if (menu == null) {
			return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<String>(menu.toJson(), headers, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<String> createFromJson(@RequestBody String json) {
		Menu menu = Menu.fromJsonToMenu(json);
		menuRepository.persist(menu);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		return new ResponseEntity<String>(headers, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<String> createFromJsonArray(@RequestBody String json) {
		for (Menu menu: Menu.fromJsonArrayToMenus(json)) {
			menuRepository.persist(menu);
		}
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		return new ResponseEntity<String>(headers, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, headers = "Accept=application/json")
	public ResponseEntity<String> updateFromJson(@RequestBody String json, @PathVariable("id") Long id) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		Menu menu = Menu.fromJsonToMenu(json);
		if (menuRepository.merge(menu) == null) {
			return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<String>(headers, HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
	public ResponseEntity<String> deleteFromJson(@PathVariable("id") Long id) {
		Menu menu = menuRepository.findMenu(id);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		if (menu == null) {
			return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
		}
		menuRepository.remove(menu);
		return new ResponseEntity<String>(headers, HttpStatus.OK);
	}

	@RequestMapping(params = "find=ByParent", headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> jsonFindMenusByParent(@RequestParam("parent") Menu parent) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		return new ResponseEntity<String>(Menu.toJsonArray(menuRepository.findMenusByParent(parent).getResultList()), headers, HttpStatus.OK);
	}


}
