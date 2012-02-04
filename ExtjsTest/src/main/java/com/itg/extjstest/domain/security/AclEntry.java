package com.itg.extjstest.domain.security;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@Entity
@RooJavaBean
@RooToString
@RooJpaActiveRecord(versionField = "")
@Table(name = "acl_entry", uniqueConstraints = { @UniqueConstraint(columnNames = {
		"acl_object_identity", "ace_order" }) })
public class AclEntry {

    @NotNull
    @ManyToOne(targetEntity = AclObjectIdentity.class)
    @JoinColumn
    private AclObjectIdentity acl_object_identity;

    @NotNull
    private Integer ace_order;

    @NotNull
    @ManyToOne(targetEntity = AclSid.class)
    @JoinColumn
    private AclSid sid;
    
    @NotNull
    private Integer mask;
    
    @NotNull
    private boolean granting;
    
    @NotNull
    private boolean audit_success;
    
    @NotNull
    private boolean audit_failure;
}