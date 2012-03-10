package com.itg.extjstest.domain;

import com.itg.extjstest.util.FilterItem;
import flexjson.JSONSerializer;
import flexjson.transformer.DateTransformer;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
@RooJson
public class Inspection {

	@DateTimeFormat(style = "M-")
	private Date InspectionDate;

	@Size(max = 50)
	private String authority;

	@Size(max = 50)
	private String docNo;

	private Boolean original;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<InspectionItem> items = new HashSet<InspectionItem>();

	@Size(max = 250)
	private String remark;

	@Size(max = 200)
	private String contracts;

	public static List<com.itg.extjstest.domain.Inspection> findInspectionByFilter(
			List<com.itg.extjstest.util.FilterItem> filters, Integer start,
			Integer page, Integer limit) {
		CriteriaBuilder cb = entityManager().getCriteriaBuilder();
		CriteriaQuery<Inspection> c = cb.createQuery(Inspection.class);
		Root<Inspection> inspection = c.from(Inspection.class);
		HashMap<String, Path> paths = new HashMap<String, Path>();
		paths.put("", inspection);
		List<Predicate> criteria = new ArrayList<Predicate>();
		if (filters != null) {
			for (FilterItem f : filters) {
				criteria.add(f.getPredicate(cb, paths));
			}
			c.where(cb.and(criteria.toArray(new Predicate[0])));
		}
		return entityManager().createQuery(c).setFirstResult(start)
				.setMaxResults(limit).getResultList();
	}

	public static String mapToJson(
			HashMap<java.lang.String, java.lang.Object> map,
			List<com.itg.extjstest.domain.Inspection> result) {
		map.put("inspections", result);
		String resultJson = new JSONSerializer()
				.exclude("*.class")
				.include("inspections")
				.include("inspections.items")
				.transform(new DateTransformer("yyyy-MM-dd HH:mm:ss"),
						Date.class).serialize(map);
		return resultJson;
	}
}
