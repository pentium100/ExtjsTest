package com.itg.extjstest.domain.security;

import javax.persistence.Entity;
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
@Table(name = "acl_sid", uniqueConstraints = { @UniqueConstraint(columnNames = {
		"sid", "principal" }) })
public class AclSid {

	@NotNull
	private boolean principal;

	@NotNull
	private String sid;

}