package com.itg.extjstest.domain;

import com.itg.extjstest.util.ContractTypeObjectFactory;
import com.itg.extjstest.util.FilterItem;
import com.itg.extjstest.util.SortItem;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import flexjson.transformer.DateTransformer;
import java.text.ParseException;
import java.util.*;
import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Order;
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


@Entity
public class AfloatGoods {

	@ManyToOne
	private Contract contract;

	@Size(max = 50)
	private String plateNum;

	@Size(max = 50)
	private String dispatch;

	@Size(max = 50)
	private String destination;

	@DateTimeFormat(style = "M-")
	private Date transportDate;

	@DateTimeFormat(style = "M-")
	private Date dispatchDate;

	@DateTimeFormat(style = "M-")
	private Date eta;

	@Size(max = 500)
	private String remark;

	@DateTimeFormat(style = "M-")
	private Date arrivalDate;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<AfloatGoodsItem> items = new HashSet<AfloatGoodsItem>();

	private Boolean original;

	private Boolean sourceFee;

	private Boolean destinationFee;

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




	public Contract getContract() {
		return this.contract;
	}

	public void setContract(Contract contract) {
		this.contract = contract;
	}

	public String getPlateNum() {
		return this.plateNum;
	}

	public void setPlateNum(String plateNum) {
		this.plateNum = plateNum;
	}

	public String getDispatch() {
		return this.dispatch;
	}

	public void setDispatch(String dispatch) {
		this.dispatch = dispatch;
	}

	public String getDestination() {
		return this.destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public Date getTransportDate() {
		return this.transportDate;
	}

	public void setTransportDate(Date transportDate) {
		this.transportDate = transportDate;
	}

	public Date getDispatchDate() {
		return this.dispatchDate;
	}

	public void setDispatchDate(Date dispatchDate) {
		this.dispatchDate = dispatchDate;
	}

	public Date getEta() {
		return this.eta;
	}

	public void setEta(Date eta) {
		this.eta = eta;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Date getArrivalDate() {
		return this.arrivalDate;
	}

	public void setArrivalDate(Date arrivalDate) {
		this.arrivalDate = arrivalDate;
	}

	public Set<AfloatGoodsItem> getItems() {
		return this.items;
	}

	public void setItems(Set<AfloatGoodsItem> items) {
		this.items = items;
	}

	public Boolean getOriginal() {
		return this.original;
	}

	public void setOriginal(Boolean original) {
		this.original = original;
	}

	public Boolean getSourceFee() {
		return this.sourceFee;
	}

	public void setSourceFee(Boolean sourceFee) {
		this.sourceFee = sourceFee;
	}

	public Boolean getDestinationFee() {
		return this.destinationFee;
	}

	public void setDestinationFee(Boolean destinationFee) {
		this.destinationFee = destinationFee;
	}

	public String toJson() {
		return new JSONSerializer().exclude("*.class").serialize(this);
	}

	public String toJson(String[] fields) {
		return new JSONSerializer().include(fields).exclude("*.class").serialize(this);
	}

	public static String toJsonArray(Collection<AfloatGoods> collection) {
		return new JSONSerializer().exclude("*.class").serialize(collection);
	}

	public static String toJsonArray(Collection<AfloatGoods> collection, String[] fields) {
		return new JSONSerializer().include(fields).exclude("*.class").serialize(collection);
	}

	public static Collection<AfloatGoods> fromJsonArrayToAfloatGoodses(String json) {
		return new JSONDeserializer<List<AfloatGoods>>().use(null, ArrayList.class).use("values", AfloatGoods.class).deserialize(json);
	}


	public static String mapToJson(
			HashMap<java.lang.String, java.lang.Object> map,
			List<com.itg.extjstest.domain.AfloatGoods> result) {
		map.put("afloatGoods", result);
		String resultJson = new JSONSerializer()
				.exclude("*.class")
				.include("afloatGoods")
				.include("afloatGoods.items")
				.transform(new DateTransformer("yyyy-MM-dd HH:mm:ss"),
						Date.class).serialize(map);
		return resultJson;
	}

	public  static com.itg.extjstest.domain.AfloatGoods fromJsonToAfloatGoods(
			String json) {
		return new JSONDeserializer<AfloatGoods>().use(null, AfloatGoods.class)
				.use(ContractType.class, new ContractTypeObjectFactory())
				.deserialize(json);
	}


}
