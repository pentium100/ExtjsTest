// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.itg.extjstest.domain;

import com.itg.extjstest.domain.MaterialDocItem;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;

privileged aspect MaterialDocItem_Roo_Jpa_Entity {
    
    declare @type: MaterialDocItem: @Entity;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "lineId")
    private Long MaterialDocItem.lineId;
    
    @Version
    @Column(name = "version")
    private Integer MaterialDocItem.version;
    
    public Long MaterialDocItem.getLineId() {
        return this.lineId;
    }
    
    public void MaterialDocItem.setLineId(Long id) {
        this.lineId = id;
    }
    
    public Integer MaterialDocItem.getVersion() {
        return this.version;
    }
    
    public void MaterialDocItem.setVersion(Integer version) {
        this.version = version;
    }
    
}