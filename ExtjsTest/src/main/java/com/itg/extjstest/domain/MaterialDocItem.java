package com.itg.extjstest.domain;

import javax.persistence.ManyToOne;
import javax.validation.constraints.Size;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(identifierColumn = "lineId", identifierField = "lineId")
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

    private Double netWeight;

    @Size(max = 50)
    private String warehouse;

    @ManyToOne
    private com.itg.extjstest.domain.MaterialDocItem lineId_in;

    @Size(max = 3000)
    private String reamrk;
}
