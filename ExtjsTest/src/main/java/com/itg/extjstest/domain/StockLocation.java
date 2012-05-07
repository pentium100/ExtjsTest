package com.itg.extjstest.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
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
public class StockLocation {

    @Size(max = 50)
    private String stockLocation;

	public static String mapToJson(HashMap<String, Object> map,
			List<StockLocation> stockLocations) {
		// TODO Auto-generated method stub
	
		map.put("stockLocations", stockLocations);
		String resultJson = new JSONSerializer()
				.exclude("*.class")
				.include("stockLocations")
				.transform(new DateTransformer("yyyy-MM-dd HH:mm:ss"),
						Date.class).serialize(map);
		return resultJson;
		
		
	}

	public static List<StockLocation> findStockLocationByFilter(
			List<FilterItem> filters, int start, int page, int limit) {
		
		
        CriteriaBuilder cb = entityManager().getCriteriaBuilder();
        CriteriaQuery<StockLocation> c = cb.createQuery(StockLocation.class);
        Root<StockLocation> root = c.from(StockLocation.class);
        HashMap<String, Path> paths = new HashMap<String, Path>();
        paths.put("", root);
        List<Predicate> criteria = new ArrayList<Predicate>();
        if (filters != null) {
            for (FilterItem f : filters) {
                criteria.add(f.getPredicate(cb, paths));
            }
            c.where(cb.and(criteria.toArray(new Predicate[0])));
        }
        
        List<StockLocation> list; 
        list = entityManager().createQuery(c).setFirstResult(start).setMaxResults(limit).getResultList();
        
        return list;


		
		
	}
}
