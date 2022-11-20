package com.itg.extjstest.domain;

import javax.persistence.*;
import javax.validation.constraints.Size;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@Table(
		uniqueConstraints={
        	@UniqueConstraint(columnNames={"contract", "model"})}		
		)
@Entity
public class ContractItem {

    @ManyToOne
    private Contract contract;

    private String model;

    private float quantity;

    @Size(max = 500)
    private String remark;

    private Double unitPrice;

    
    @Transient
    private Double usedQuantity;


    public Contract getContract() {
        return this.contract;
    }

    public void setContract(Contract contract) {
        this.contract = contract;
    }

    public String getModel() {
        return this.model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public float getQuantity() {
        return this.quantity;
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Double getUnitPrice() {
        return this.unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Double getUsedQuantity() {
        return this.usedQuantity;
    }

    public void setUsedQuantity(Double usedQuantity) {
        this.usedQuantity = usedQuantity;
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

    public static ContractItem fromJsonToContractItem(String json) {
        return new JSONDeserializer<ContractItem>().use(null, ContractItem.class).deserialize(json);
    }

    public static String toJsonArray(Collection<ContractItem> collection) {
        return new JSONSerializer().exclude("*.class").serialize(collection);
    }

    public static String toJsonArray(Collection<ContractItem> collection, String[] fields) {
        return new JSONSerializer().include(fields).exclude("*.class").serialize(collection);
    }

    public static Collection<ContractItem> fromJsonArrayToContractItems(String json) {
        return new JSONDeserializer<List<ContractItem>>().use(null, ArrayList.class).use("values", ContractItem.class).deserialize(json);
    }




}
