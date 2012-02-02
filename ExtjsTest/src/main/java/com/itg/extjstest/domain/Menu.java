package com.itg.extjstest.domain;

import javax.persistence.ManyToOne;
import javax.validation.constraints.Size;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(finders = { "findMenusByParent" })
@RooJson
public class Menu {

    private String text;

    private Boolean leaf;

    private String controller;

    @ManyToOne
    private com.itg.extjstest.domain.Menu parent;

    private String iconCls;

    private String views;

    @Size(max = 200)
    private String controllerParam;
}
