// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.itg.extjstest.domain;

import com.itg.extjstest.domain.AfloatGoodsItem;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;

privileged aspect AfloatGoodsItem_Roo_Jpa_Entity {
    
    declare @type: AfloatGoodsItem: @Entity;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long AfloatGoodsItem.id;
    
    @Version
    @Column(name = "version")
    private Integer AfloatGoodsItem.version;
    
    public Long AfloatGoodsItem.getId() {
        return this.id;
    }
    
    public void AfloatGoodsItem.setId(Long id) {
        this.id = id;
    }
    
    public Integer AfloatGoodsItem.getVersion() {
        return this.version;
    }
    
    public void AfloatGoodsItem.setVersion(Integer version) {
        this.version = version;
    }
    
}
