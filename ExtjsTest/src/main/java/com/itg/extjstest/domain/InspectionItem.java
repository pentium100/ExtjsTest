package com.itg.extjstest.domain;

import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import javax.validation.constraints.Size;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class InspectionItem {

    @ManyToOne
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
    
    
}
