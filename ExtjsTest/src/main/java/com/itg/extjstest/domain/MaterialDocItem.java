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
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;
import javax.persistence.criteria.Subquery;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.transaction.annotation.Transactional;

@Entity
public class MaterialDocItem {

	@ManyToOne
	private MaterialDoc materialDoc;

	@Size(max = 3)
	private String moveType;

	@Size(max = 20)
	private String model_contract;

	@Size(max = 20)
	private String model_tested;

	private Double grossWeight;

	private Double lots;

	private Double netWeight;

	@Transient
	private String warehouse;

	@ManyToOne(fetch = FetchType.EAGER)
	private com.itg.extjstest.domain.MaterialDocItem lineId_in;

	@Size(max = 3000)
	private String remark;

	@Column(nullable = true)
	private short direction;

	@Transient
	private String contractNo;

	@Transient
	private String deliveryNote;

	@Transient
	private String batchNo;

	@Transient
	private String plateNum;

	@Transient
	private String workingNo;

	@Transient
	private Date docDate;

	@ManyToOne
	private com.itg.extjstest.domain.MaterialDocItem lineId_test;

	@ManyToOne
	private com.itg.extjstest.domain.MaterialDocItem lineId_up;

	@ManyToOne
	private Contract contract;

	@ManyToOne
	@NotNull
	private StockLocation stockLocation;

	public MaterialDoc getMaterialDoc() {
		return this.materialDoc;
	}

	public void setMaterialDoc(MaterialDoc materialDoc) {
		this.materialDoc = materialDoc;
	}

	public String getMoveType() {
		return this.moveType;
	}

	public void setMoveType(String moveType) {
		this.moveType = moveType;
	}

	public String getModel_contract() {
		return this.model_contract;
	}

	public void setModel_contract(String model_contract) {
		this.model_contract = model_contract;
	}

	public String getModel_tested() {
		return this.model_tested;
	}

	public void setModel_tested(String model_tested) {
		this.model_tested = model_tested;
	}

	public Double getGrossWeight() {
		return this.grossWeight;
	}

	public void setGrossWeight(Double grossWeight) {
		this.grossWeight = grossWeight;
	}

	public Double getNetWeight() {
		return this.netWeight;
	}

	public void setNetWeight(Double netWeight) {
		this.netWeight = netWeight;
	}

	public Double getLots() {
		return this.lots;
	}

	public void setLots(Double lots) {
		this.lots = lots;
	}

	public String getWarehouse() {
		return this.warehouse;
	}

	public void setWarehouse(String warehouse) {
		this.warehouse = warehouse;
	}

	public MaterialDocItem getLineId_in() {
		return this.lineId_in;
	}

	public void setLineId_in(MaterialDocItem lineId_in) {
		this.lineId_in = lineId_in;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public short getDirection() {
		return this.direction;
	}

	public void setDirection(short direction) {
		this.direction = direction;
	}

	public String getContractNo() {
		return this.contractNo;
	}

	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
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

	public MaterialDocItem getLineId_test() {
		return this.lineId_test;
	}

	public void setLineId_test(MaterialDocItem lineId_test) {
		this.lineId_test = lineId_test;
	}

	public MaterialDocItem getLineId_up() {
		return this.lineId_up;
	}

	public void setLineId_up(MaterialDocItem lineId_up) {
		this.lineId_up = lineId_up;
	}

	public Contract getContract() {
		return this.contract;
	}

	public void setContract(Contract contract) {
		this.contract = contract;
	}

	public StockLocation getStockLocation() {
		return this.stockLocation;
	}

	public void setStockLocation(StockLocation stockLocation) {
		this.stockLocation = stockLocation;
	}


	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "lineId")
	private Long lineId;

	@Version
	@Column(name = "version")
	private Integer version;

	public Long getLineId() {
		return this.lineId;
	}

	public void setLineId(Long id) {
		this.lineId = id;
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

	public static MaterialDocItem fromJsonToMaterialDocItem(String json) {
		return new JSONDeserializer<MaterialDocItem>().use(null, MaterialDocItem.class).deserialize(json);
	}

	public static String toJsonArray(Collection<MaterialDocItem> collection) {
		return new JSONSerializer().exclude("*.class").serialize(collection);
	}

	public static String toJsonArray(Collection<MaterialDocItem> collection, String[] fields) {
		return new JSONSerializer().include(fields).exclude("*.class").serialize(collection);
	}

	public static Collection<MaterialDocItem> fromJsonArrayToMaterialDocItems(String json) {
		return new JSONDeserializer<List<MaterialDocItem>>().use(null, ArrayList.class).use("values", MaterialDocItem.class).deserialize(json);
	}



	public static String mapToJson(
			HashMap<java.lang.String, java.lang.Object> map,
			List<com.itg.extjstest.domain.MaterialDocItem> result) {
		map.put("materialdocitems", result);
		String resultJson = new JSONSerializer()
				.exclude("*.class")
				.include("materialdocitems")
				.include("materialdocitems.materialDoc")
				.include("materialdocitems.materialDoc.docType")
				.exclude("*.implementation")
				.exclude("*.entityManager")
				.exclude("*.handler")
				.exclude("*.hibernateLazyInitializer")
				.transform(new DateTransformer("yyyy-MM-dd HH:mm:ss"),
						Date.class).serialize(map);
		return resultJson;
	}

	public void fillLineInInfo() {
		if (!getLineId_in().equals(this)) {
			MaterialDocItem i = getLineId_in();
			setBatchNo(i.getMaterialDoc().getBatchNo());
			setPlateNum(i.getMaterialDoc().getPlateNum());
			setDeliveryNote(i.getMaterialDoc().getDeliveryNote());
			setDocDate(i.getMaterialDoc().getDocDate());
			setWorkingNo(i.getMaterialDoc().getWorkingNo());
			setContractNo(i.getMaterialDoc().getContract().getContractNo());
		}
	}

}
