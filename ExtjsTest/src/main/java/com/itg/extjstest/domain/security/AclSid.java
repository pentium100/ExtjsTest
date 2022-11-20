package com.itg.extjstest.domain.security;

import javax.persistence.*;
import javax.validation.constraints.NotNull;


import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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


	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}



	public boolean isPrincipal() {
		return this.principal;
	}

	public void setPrincipal(boolean principal) {
		this.principal = principal;
	}

	public String getSid() {
		return this.sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	@PersistenceContext
	transient EntityManager entityManager;

	public static final EntityManager entityManager() {
		EntityManager em = new AclSid().entityManager;
		if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
		return em;
	}

	public static long countAclSids() {
		return entityManager().createQuery("SELECT COUNT(o) FROM AclSid o", Long.class).getSingleResult();
	}

	public static List<AclSid> findAllAclSids() {
		return entityManager().createQuery("SELECT o FROM AclSid o", AclSid.class).getResultList();
	}

	public static AclSid findAclSid(Long id) {
		if (id == null) return null;
		return entityManager().find(AclSid.class, id);
	}

	public static List<AclSid> findAclSidEntries(int firstResult, int maxResults) {
		return entityManager().createQuery("SELECT o FROM AclSid o", AclSid.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
	}

	@Transactional
	public void persist() {
		if (this.entityManager == null) this.entityManager = entityManager();
		this.entityManager.persist(this);
	}

	@Transactional
	public void remove() {
		if (this.entityManager == null) this.entityManager = entityManager();
		if (this.entityManager.contains(this)) {
			this.entityManager.remove(this);
		} else {
			AclSid attached = AclSid.findAclSid(this.id);
			this.entityManager.remove(attached);
		}
	}

	@Transactional
	public void flush() {
		if (this.entityManager == null) this.entityManager = entityManager();
		this.entityManager.flush();
	}

	@Transactional
	public void clear() {
		if (this.entityManager == null) this.entityManager = entityManager();
		this.entityManager.clear();
	}

	@Transactional
	public AclSid merge() {
		if (this.entityManager == null) this.entityManager = entityManager();
		AclSid merged = this.entityManager.merge(this);
		this.entityManager.flush();
		return merged;
	}


}