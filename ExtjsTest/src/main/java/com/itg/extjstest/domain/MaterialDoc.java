package com.itg.extjstest.domain;

import com.itg.extjstest.util.ContractObjectFactory;
import com.itg.extjstest.util.ContractTypeObjectFactory;
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
import java.util.Set;
import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.transaction.annotation.Transactional;

@Entity
public class MaterialDoc {

	@ManyToOne
	private Contract contract;

	@Size(max = 50)
	private String deliveryNote;

	@Size(max = 50)
	private String batchNo;

	@Size(max = 50)
	private String plateNum;

	@Size(max = 50)
	private String workingNo;

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(style = "M-")
	private Date docDate;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<MaterialDocItem> items = new HashSet<MaterialDocItem>();

	@ManyToOne
	@NotNull
	private MaterialDocType docType;

	@Transient
	private String targetWarehouse;

	@Transient
	private StockLocation targetStockLocation;

	@Size(max = 50)
	private String invNo;

	@Size(max = 10)
	private String cause;



	public Contract getContract() {
		return this.contract;
	}

	public void setContract(Contract contract) {
		this.contract = contract;
	}

	public String getDeliveryNote() {
		return this.deliveryNote;
	}

	public void setDeliveryNote(String deliveryNote) {
		this.deliveryNote = deliveryNote;
	}

	public String getBatchNo() {
		return this.batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public String getPlateNum() {
		return this.plateNum;
	}

	public void setPlateNum(String plateNum) {
		this.plateNum = plateNum;
	}

	public String getWorkingNo() {
		return this.workingNo;
	}

	public void setWorkingNo(String workingNo) {
		this.workingNo = workingNo;
	}

	public Date getDocDate() {
		return this.docDate;
	}

	public void setDocDate(Date docDate) {
		this.docDate = docDate;
	}

	public Set<MaterialDocItem> getItems() {
		return this.items;
	}

	public void setItems(Set<MaterialDocItem> items) {
		this.items = items;
	}

	public MaterialDocType getDocType() {
		return this.docType;
	}

	public void setDocType(MaterialDocType docType) {
		this.docType = docType;
	}

	public String getTargetWarehouse() {
		return this.targetWarehouse;
	}

	public void setTargetWarehouse(String targetWarehouse) {
		this.targetWarehouse = targetWarehouse;
	}

	public StockLocation getTargetStockLocation() {
		return this.targetStockLocation;
	}

	public void setTargetStockLocation(StockLocation targetStockLocation) {
		this.targetStockLocation = targetStockLocation;
	}

	public String getInvNo() {
		return this.invNo;
	}

	public void setInvNo(String invNo) {
		this.invNo = invNo;
	}

	public String getCause() {
		return this.cause;
	}

	public void setCause(String cause) {
		this.cause = cause;
	}





	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "docNo")
	private Long docNo;

	@Version
	@Column(name = "version")
	private Integer version;

	public Long getDocNo() {
		return this.docNo;
	}

	public void setDocNo(Long id) {
		this.docNo = id;
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

	public static Collection<MaterialDoc> fromJsonArrayToMaterialDocs(String json) {
		return new JSONDeserializer<List<MaterialDoc>>().use(null, ArrayList.class).use("values", MaterialDoc.class).deserialize(json);
	}


	public static String toJsonArray(
			Collection<com.itg.extjstest.domain.MaterialDoc> collection) {
		return new JSONSerializer()
				.exclude("*.class")
				.transform(new DateTransformer("yyyy-MM-dd HH:mm:ss"),
						Date.class).serialize(collection);
	}




	public static com.itg.extjstest.domain.MaterialDoc fromJsonToMaterialDoc(
			String json) {
		return new JSONDeserializer<MaterialDoc>().use(null, MaterialDoc.class)
				.use(ContractType.class, new ContractTypeObjectFactory())
				.use("contract.contractType", new ContractTypeObjectFactory())
				.use(Contract.class, new ContractObjectFactory())
				.use(Date.class, new DateTransformer("yyyy-MM-dd HH:mm:ss"))
				.deserialize(json);
	}



	public static String mapToJson(
			HashMap<java.lang.String, java.lang.Object> map,
			List<com.itg.extjstest.domain.MaterialDoc> result) {
		map.put("materialdocs", result);
		String resultJson = new JSONSerializer()
				.exclude("*.class")
				.include("materialdocs")
				.include("materialdocs.items")
				.include("materialdocs.contract")
				.include("materialdocs.docType")
				.include("materialdocs.items.lineId_in")
				.include("materialdocs.items.lineId_in.lineId")
				.include("materialdocs.items.lineId_in.version")
				.exclude("materialdocs.items.lineId_in.*.*")
				.transform(new DateTransformer("yyyy-MM-dd HH:mm:ss"),
						Date.class).serialize(map);
		return resultJson;
	}


}
