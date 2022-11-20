package com.itg.extjstest.domain;

import java.text.ParseException;
import java.util.*;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.constraints.Size;

import flexjson.JSONDeserializer;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import com.itg.extjstest.util.FilterItem;

import flexjson.JSONSerializer;
import flexjson.transformer.DateTransformer;
import org.springframework.transaction.annotation.Transactional;

@Entity
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




	public String getStockLocation() {
		return this.stockLocation;
	}

	public void setStockLocation(String stockLocation) {
		this.stockLocation = stockLocation;
	}


	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;

	@Version
	@Column(name = "version")
	private Integer version;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getVersion() {
		return this.version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}



	public String toJson() {
		return new JSONSerializer().exclude("*.class").serialize(this);
	}

	public String toJson(String[] fields) {
		return new JSONSerializer().include(fields).exclude("*.class").serialize(this);
	}

	public static StockLocation fromJsonToStockLocation(String json) {
		return new JSONDeserializer<StockLocation>().use(null, StockLocation.class).deserialize(json);
	}

	public static String toJsonArray(Collection<StockLocation> collection) {
		return new JSONSerializer().exclude("*.class").serialize(collection);
	}

	public static String toJsonArray(Collection<StockLocation> collection, String[] fields) {
		return new JSONSerializer().include(fields).exclude("*.class").serialize(collection);
	}

	public static Collection<StockLocation> fromJsonArrayToStockLocations(String json) {
		return new JSONDeserializer<List<StockLocation>>().use(null, ArrayList.class).use("values", StockLocation.class).deserialize(json);
	}

}
