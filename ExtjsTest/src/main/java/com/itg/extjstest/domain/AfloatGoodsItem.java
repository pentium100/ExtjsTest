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
public class AfloatGoodsItem {

    @ManyToOne
    private AfloatGoods afloatGoods;

    @Size(max = 50)
    private String model;

    private Double quantity;

    @Size(max = 50)
    private String batchNo;
}
