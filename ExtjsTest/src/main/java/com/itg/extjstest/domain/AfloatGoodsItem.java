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

@Entity
public class AfloatGoodsItem {

    @ManyToOne
    private AfloatGoods afloatGoods;

    @Size(max = 50)
    private String model;

    private Double quantity;

    @Size(max = 50)
    private String batchNo;

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



    public AfloatGoods getAfloatGoods() {
        return this.afloatGoods;
    }

    public void setAfloatGoods(AfloatGoods afloatGoods) {
        this.afloatGoods = afloatGoods;
    }

    public String getModel() {
        return this.model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Double getQuantity() {
        return this.quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public String getBatchNo() {
        return this.batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }



}
