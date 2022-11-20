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




    public AclObjectIdentity getAcl_object_identity() {
        return this.acl_object_identity;
    }

    public void setAcl_object_identity(AclObjectIdentity acl_object_identity) {
        this.acl_object_identity = acl_object_identity;
    }

    public Integer getAce_order() {
        return this.ace_order;
    }

    public void setAce_order(Integer ace_order) {
        this.ace_order = ace_order;
    }

    public AclSid getSid() {
        return this.sid;
    }

    public void setSid(AclSid sid) {
        this.sid = sid;
    }

    public Integer getMask() {
        return this.mask;
    }

    public void setMask(Integer mask) {
        this.mask = mask;
    }

    public boolean isGranting() {
        return this.granting;
    }

    public void setGranting(boolean granting) {
        this.granting = granting;
    }

    public boolean isAudit_success() {
        return this.audit_success;
    }

    public void setAudit_success(boolean audit_success) {
        this.audit_success = audit_success;
    }

    public boolean isAudit_failure() {
        return this.audit_failure;
    }

    public void setAudit_failure(boolean audit_failure) {
        this.audit_failure = audit_failure;
    }

    @PersistenceContext
    transient EntityManager entityManager;

    public static final EntityManager entityManager() {
        EntityManager em = new AclEntry().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

    public static long countAclEntrys() {
        return entityManager().createQuery("SELECT COUNT(o) FROM AclEntry o", Long.class).getSingleResult();
    }

    public static List<AclEntry> findAllAclEntrys() {
        return entityManager().createQuery("SELECT o FROM AclEntry o", AclEntry.class).getResultList();
    }

    public static AclEntry findAclEntry(Long id) {
        if (id == null) return null;
        return entityManager().find(AclEntry.class, id);
    }

    public static List<AclEntry> findAclEntryEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM AclEntry o", AclEntry.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
            AclEntry attached = AclEntry.findAclEntry(this.id);
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
    public AclEntry merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        AclEntry merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

}