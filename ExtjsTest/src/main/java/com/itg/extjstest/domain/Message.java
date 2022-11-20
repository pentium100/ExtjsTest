package com.itg.extjstest.domain;

import com.itg.extjstest.util.FilterItem;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import flexjson.transformer.DateTransformer;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.*;
import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.transaction.annotation.Transactional;

@Entity
public class Message {

	@Size(max = 20)
	private String department;

	@Size(max = 4)
	@Pattern(regexp = "(供应|需求|敞口|锁定)")
	private String type;

	@Size(max = 50)
	private String article;

	private Double quantity;

	@Size(max = 40)
	private String departure;

	@Size(max = 50)
	private String supplier;

	@Size(max = 50)
	private String owner;

	private Double costPrice;

	private Double suggestedPrice;

	private String remark;

	private Boolean isUrgent;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<Specification> specifications = new HashSet<Specification>();

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(style = "M-")
	private Date validBefore;

	@Size(max = 150)
	private String eta;

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(style = "M-")
	private Date lastChangeTime;


	public String getDepartment() {
		return this.department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getArticle() {
		return this.article;
	}

	public void setArticle(String article) {
		this.article = article;
	}

	public Double getQuantity() {
		return this.quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	public String getDeparture() {
		return this.departure;
	}

	public void setDeparture(String departure) {
		this.departure = departure;
	}

	public String getSupplier() {
		return this.supplier;
	}

	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}

	public String getOwner() {
		return this.owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public Double getCostPrice() {
		return this.costPrice;
	}

	public void setCostPrice(Double costPrice) {
		this.costPrice = costPrice;
	}

	public Double getSuggestedPrice() {
		return this.suggestedPrice;
	}

	public void setSuggestedPrice(Double suggestedPrice) {
		this.suggestedPrice = suggestedPrice;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Boolean getIsUrgent() {
		return this.isUrgent;
	}

	public void setIsUrgent(Boolean isUrgent) {
		this.isUrgent = isUrgent;
	}

	public Set<Specification> getSpecifications() {
		return this.specifications;
	}

	public void setSpecifications(Set<Specification> specifications) {
		this.specifications = specifications;
	}

	public Date getValidBefore() {
		return this.validBefore;
	}

	public void setValidBefore(Date validBefore) {
		this.validBefore = validBefore;
	}

	public String getEta() {
		return this.eta;
	}

	public void setEta(String eta) {
		this.eta = eta;
	}

	public Date getLastChangeTime() {
		return this.lastChangeTime;
	}

	public void setLastChangeTime(Date lastChangeTime) {
		this.lastChangeTime = lastChangeTime;
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

	public static String toJsonArray(Collection<Message> collection) {
		return new JSONSerializer().exclude("*.class").serialize(collection);
	}

	public static String toJsonArray(Collection<Message> collection, String[] fields) {
		return new JSONSerializer().include(fields).exclude("*.class").serialize(collection);
	}

	public static Collection<Message> fromJsonArrayToMessages(String json) {
		return new JSONDeserializer<List<Message>>().use(null, ArrayList.class).use("values", Message.class).deserialize(json);
	}


	public static String mapToJson(
			HashMap<java.lang.String, java.lang.Object> map,
			List<com.itg.extjstest.domain.Message> messages) {
		map.put("messages", messages);
		String resultJson = new JSONSerializer()
				.exclude("*.class")
				.include("messages")
				.include("messages.specifications")
				.transform(new DateTransformer("yyyy-MM-dd HH:mm:ss"),
						Date.class).serialize(map);
		return resultJson;
	}

	public String validateObject(Locale locale) {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		Set<ConstraintViolation<Message>> violations = validator.validate(this);
		StringBuffer buf = new StringBuffer();
		if (violations.size() != 0) {
			ResourceBundle bundle = ResourceBundle.getBundle(
					"validateMessages", locale);
			for (ConstraintViolation<Message> violation : violations) {
				buf.append("-"
						+ bundle.getString("Message."
								+ violation.getPropertyPath().toString()));
				buf.append(violation.getMessage() + "<BR/>\n");
			}
		}
		return buf.toString();
	}

	public static com.itg.extjstest.domain.Message fromJsonToMessage(String json) {
		return new JSONDeserializer<Message>().use(null, Message.class)
				.deserialize(json);
	}



}
