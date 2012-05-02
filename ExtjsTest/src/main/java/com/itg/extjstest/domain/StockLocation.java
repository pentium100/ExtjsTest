package com.itg.extjstest.domain;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.validation.constraints.Size;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

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
}
