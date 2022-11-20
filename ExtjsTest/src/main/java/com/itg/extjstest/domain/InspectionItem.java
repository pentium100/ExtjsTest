package com.itg.extjstest.domain;

import javax.persistence.*;
import javax.validation.constraints.Size;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Entity
public class InspectionItem {

    @OneToOne
    private MaterialDocItem materialDocItem;

    private Double netWeight;

    private Double si;

    private Double fe;

    private Double al;

    private Double ca;

    private Double p;

    @Size(max = 500)
    private String remark;

    @ManyToOne
    private Inspection inspection;

    @Transient
    private String contractNo;

    @Transient
    private String deliveryNote;

    @Transient
    private String batchNo;

    @Transient
    private String plateNum;

    @Transient
    private String model_contract;

    private Boolean isLast;


    public MaterialDocItem getMaterialDocItem() {
        return this.materialDocItem;
    }

    public void setMaterialDocItem(MaterialDocItem materialDocItem) {
        this.materialDocItem = materialDocItem;
    }

    public Double getNetWeight() {
        return this.netWeight;
    }

    public void setNetWeight(Double netWeight) {
        this.netWeight = netWeight;
    }

    public Double getSi() {
        return this.si;
    }

    public void setSi(Double si) {
        this.si = si;
    }

    public Double getFe() {
        return this.fe;
    }

    public void setFe(Double fe) {
        this.fe = fe;
    }

    public Double getAl() {
        return this.al;
    }

    public void setAl(Double al) {
        this.al = al;
    }

    public Double getCa() {
        return this.ca;
    }

    public void setCa(Double ca) {
        this.ca = ca;
    }

    public Double getP() {
        return this.p;
    }

    public void setP(Double p) {
        this.p = p;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Inspection getInspection() {
        return this.inspection;
    }

    public void setInspection(Inspection inspection) {
        this.inspection = inspection;
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

    public String getModel_contract() {
        return this.model_contract;
    }

    public void setModel_contract(String model_contract) {
        this.model_contract = model_contract;
    }

    public Boolean getIsLast() {
        return this.isLast;
    }

    public void setIsLast(Boolean isLast) {
        this.isLast = isLast;
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

}
