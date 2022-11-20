package com.itg.extjstest.domain.security;

import javax.persistence.*;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Entity
@RooJavaBean
@RooToString
@RooJpaActiveRecord(versionField = "")
@Table(name = "acl_class")
public class AclClass {
	
	@Column(name = "class", nullable=false, unique=true)
	private String clazz;

	public String getClazz() {
		return this.clazz;
	}

	public void setClazz(String clazz) {
		this.clazz = clazz;
	}


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


	@PersistenceContext
	transient EntityManager entityManager;

	public static final EntityManager entityManager() {
		EntityManager em = new AclClass().entityManager;
		if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
		return em;
	}

	public static long countAclClasses() {
		return entityManager().createQuery("SELECT COUNT(o) FROM AclClass o", Long.class).getSingleResult();
	}

	public static List<AclClass> findAllAclClasses() {
		return entityManager().createQuery("SELECT o FROM AclClass o", AclClass.class).getResultList();
	}

	public static AclClass findAclClass(Long id) {
		if (id == null) return null;
		return entityManager().find(AclClass.class, id);
	}

	public static List<AclClass> findAclClassEntries(int firstResult, int maxResults) {
		return entityManager().createQuery("SELECT o FROM AclClass o", AclClass.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
			AclClass attached = AclClass.findAclClass(this.id);
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
	public AclClass merge() {
		if (this.entityManager == null) this.entityManager = entityManager();
		AclClass merged = this.entityManager.merge(this);
		this.entityManager.flush();
		return merged;
	}

}