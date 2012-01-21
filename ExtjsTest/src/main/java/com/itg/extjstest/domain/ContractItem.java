package com.itg.extjstest.domain;

import javax.persistence.ManyToOne;
import javax.validation.constraints.Size;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
@RooJson
public class ContractItem {

    @ManyToOne
    private Contract contract;

    private String model;

    private float quantity;

    @Size(max = 500)
    private String remark;

    private Double unitPrice;
}
