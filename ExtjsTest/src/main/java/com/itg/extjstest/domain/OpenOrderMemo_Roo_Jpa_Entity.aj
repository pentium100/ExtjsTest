// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.itg.extjstest.domain;

import com.itg.extjstest.domain.OpenOrderMemo;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;

privileged aspect OpenOrderMemo_Roo_Jpa_Entity {
    
    declare @type: OpenOrderMemo: @Entity;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long OpenOrderMemo.id;
    
    @Version
    @Column(name = "version")
    private Integer OpenOrderMemo.version;
    
    public Long OpenOrderMemo.getId() {
        return this.id;
    }
    
    public void OpenOrderMemo.setId(Long id) {
        this.id = id;
    }
    
    public Integer OpenOrderMemo.getVersion() {
        return this.version;
    }
    
    public void OpenOrderMemo.setVersion(Integer version) {
        this.version = version;
    }
    
}
