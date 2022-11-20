package com.itg.extjstest.domain;

import com.itg.extjstest.util.FilterItem;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import flexjson.transformer.DateTransformer;
import java.text.ParseException;
import java.util.*;
import javax.persistence.*;
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
import org.springframework.transaction.annotation.Transactional;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
@RooJson
@Entity
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

	private String model_tested;



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



	public Date getInspectionDate() {
		return this.InspectionDate;
	}

	public void setInspectionDate(Date InspectionDate) {
		this.InspectionDate = InspectionDate;
	}

	public String getAuthority() {
		return this.authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}

	public String getDocNo() {
		return this.docNo;
	}

	public void setDocNo(String docNo) {
		this.docNo = docNo;
	}

	public Boolean getOriginal() {
		return this.original;
	}

	public void setOriginal(Boolean original) {
		this.original = original;
	}

	public Set<InspectionItem> getItems() {
		return this.items;
	}

	public void setItems(Set<InspectionItem> items) {
		this.items = items;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getContracts() {
		return this.contracts;
	}

	public void setContracts(String contracts) {
		this.contracts = contracts;
	}

	public String getModel_tested() {
		return this.model_tested;
	}

	public void setModel_tested(String model_tested) {
		this.model_tested = model_tested;
	}

	public String toJson() {
		return new JSONSerializer().exclude("*.class").serialize(this);
	}

	public String toJson(String[] fields) {
		return new JSONSerializer().include(fields).exclude("*.class").serialize(this);
	}

	public static Inspection fromJsonToInspection(String json) {
		return new JSONDeserializer<Inspection>().use(null, Inspection.class).deserialize(json);
	}

	public static String toJsonArray(Collection<Inspection> collection) {
		return new JSONSerializer().exclude("*.class").serialize(collection);
	}

	public static String toJsonArray(Collection<Inspection> collection, String[] fields) {
		return new JSONSerializer().include(fields).exclude("*.class").serialize(collection);
	}

	public static Collection<Inspection> fromJsonArrayToInspections(String json) {
		return new JSONDeserializer<List<Inspection>>().use(null, ArrayList.class).use("values", Inspection.class).deserialize(json);
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
