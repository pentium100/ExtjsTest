package com.itg.extjstest.domain;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.constraints.Size;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import com.itg.extjstest.util.FilterItem;

import flexjson.JSONSerializer;
import flexjson.transformer.DateTransformer;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
@RooJson
public class Employee {

	@Size(max = 50)
	private String name;

	public static String mapToJson(HashMap<String, Object> map,
			List<Employee> employees) {
		map.put("employees", employees);
		String resultJson = new JSONSerializer()
				.exclude("*.class")
				.include("employees")
				.transform(new DateTransformer("yyyy-MM-dd HH:mm:ss"),
						Date.class).serialize(map);
		return resultJson;
	}

	public static List<Employee> findEmployeeByFilter(List<FilterItem> filters,
			int start, int page, int limit) throws ParseException {

		CriteriaBuilder cb = entityManager().getCriteriaBuilder();
		CriteriaQuery<Employee> c = cb.createQuery(Employee.class);
		Root<Employee> root = c.from(Employee.class);
		HashMap<String, Path> paths = new HashMap<String, Path>();
		paths.put("", root);
		List<Predicate> criteria = new ArrayList<Predicate>();
		if (filters != null) {
			for (FilterItem f : filters) {
				criteria.add(f.getPredicate(cb, paths));
			}
			c.where(cb.and(criteria.toArray(new Predicate[0])));
		}

		List<Employee> list;
		list = entityManager().createQuery(c).setFirstResult(start)
				.setMaxResults(limit).getResultList();

		return list;
	}

}
