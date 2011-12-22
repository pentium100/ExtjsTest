package com.itg.extjstest.domain;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;
import com.itg.extjstest.domain.Contract;
import javax.persistence.ManyToOne;

@RooJavaBean
@RooToString
@RooEntity
public class ContractItem {

    private String model;

    private Double quantity;

    @ManyToOne
    private Contract contract;
}
