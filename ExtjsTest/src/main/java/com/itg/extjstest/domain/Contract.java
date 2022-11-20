package com.itg.extjstest.domain;

import com.itg.extjstest.util.ContractTypeObjectFactory;
import com.itg.extjstest.util.EmployeeObjectFactory;
import com.itg.extjstest.util.FilterItem;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import flexjson.transformer.DateTransformer;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Fetch;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.transaction.annotation.Transactional;

@Entity
public class Contract {

	@NotNull
	@Column(unique = true)
	@Size(max = 30)
	private String contractNo;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
	private Set<ContractItem> items = new HashSet<ContractItem>();

	@Enumerated
	private ContractType contractType;

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(style = "M-")
	private Date lastShippingDate;

	@Size(max = 50)
	private String supplier;

	@Size(max = 50)
	private String payTerm;

	@Size(max = 500)
	private String remark;

	@Column
	private boolean closed;

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(style = "M-")
	private Date signDate;

	@ManyToOne
	private Employee employee;

	public String getContractNo() {
		return this.contractNo;
	}

	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}

	public Set<ContractItem> getItems() {
		return this.items;
	}

	public void setItems(Set<ContractItem> items) {
		this.items = items;
	}

	public ContractType getContractType() {
		return this.contractType;
	}

	public void setContractType(ContractType contractType) {
		this.contractType = contractType;
	}

	public Date getLastShippingDate() {
		return this.lastShippingDate;
	}

	public void setLastShippingDate(Date lastShippingDate) {
		this.lastShippingDate = lastShippingDate;
	}

	public String getSupplier() {
		return this.supplier;
	}

	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}

	public String getPayTerm() {
		return this.payTerm;
	}

	public void setPayTerm(String payTerm) {
		this.payTerm = payTerm;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public boolean isClosed() {
		return this.closed;
	}

	public void setClosed(boolean closed) {
		this.closed = closed;
	}

	public Date getSignDate() {
		return this.signDate;
	}

	public void setSignDate(Date signDate) {
		this.signDate = signDate;
	}

	public Employee getEmployee() {
		return this.employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
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

	public static Collection<Contract> fromJsonArrayToContracts(String json) {
		return new JSONDeserializer<List<Contract>>().use(null, ArrayList.class).use("values", Contract.class).deserialize(json);
	}


	public String toJson() {
		return new JSONSerializer()
				.exclude("*.class")
				.transform(new DateTransformer("yyyy-MM-dd HH:mm:ss"),
						Date.class).serialize(this);
	}

	public static String toJsonArray(
			Collection<com.itg.extjstest.domain.Contract> collection) {
		return new JSONSerializer()
				.exclude("*.class")
				.transform(new DateTransformer("yyyy-MM-dd HH:mm:ss"),
						Date.class).include("items").serialize(collection);
	}

	public static com.itg.extjstest.domain.Contract fromJsonToContract(
			String json) {
		return new JSONDeserializer<Contract>().use(null, Contract.class)
				.use(ContractType.class, new ContractTypeObjectFactory())
				// .use(Employee.class, new EmployeeObjectFactory())
				.deserialize(json);
	}


	public static String mapToJson(Map<String, Object> map, List<Contract> contracts) {
		map.put("contracts", contracts);
		String resultJson = new JSONSerializer()
				.exclude("*.class")
				.include("contracts")
				.include("contracts.items")
				.transform(new DateTransformer("yyyy-MM-dd HH:mm:ss"),
						Date.class)
				.transform(new ContractTypeObjectFactory(), ContractType.class)
				.serialize(map);

		return resultJson;

	}


}
