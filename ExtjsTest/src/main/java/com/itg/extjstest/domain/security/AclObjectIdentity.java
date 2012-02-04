package com.itg.extjstest.domain.security;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import javax.persistence.UniqueConstraint;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;


@Entity
@RooJavaBean
@RooToString
@RooJpaActiveRecord(versionField = "")
@Table(name = "acl_object_identity", uniqueConstraints = { @UniqueConstraint(columnNames = {
		"object_id_class", "object_id_identity" }) })
public class AclObjectIdentity {

    @NotNull
    @ManyToOne(targetEntity = AclClass.class)
    @JoinColumn
    private AclClass object_id_class;

    @NotNull
    private Long object_id_identity;

    @ManyToOne(targetEntity = AclObjectIdentity.class)
    @JoinColumn
    private AclObjectIdentity parent_object;

    @NotNull
    @ManyToOne(targetEntity = AclSid.class)
    @JoinColumn
    private AclSid owner_sid;
    
    @NotNull
    private boolean entries_inheriting;
}