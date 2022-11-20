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


    public AclClass getObject_id_class() {
        return this.object_id_class;
    }

    public void setObject_id_class(AclClass object_id_class) {
        this.object_id_class = object_id_class;
    }

    public Long getObject_id_identity() {
        return this.object_id_identity;
    }

    public void setObject_id_identity(Long object_id_identity) {
        this.object_id_identity = object_id_identity;
    }

    public AclObjectIdentity getParent_object() {
        return this.parent_object;
    }

    public void setParent_object(AclObjectIdentity parent_object) {
        this.parent_object = parent_object;
    }

    public AclSid getOwner_sid() {
        return this.owner_sid;
    }

    public void setOwner_sid(AclSid owner_sid) {
        this.owner_sid = owner_sid;
    }

    public boolean isEntries_inheriting() {
        return this.entries_inheriting;
    }

    public void setEntries_inheriting(boolean entries_inheriting) {
        this.entries_inheriting = entries_inheriting;
    }

    @PersistenceContext
    transient EntityManager entityManager;

    public static final EntityManager entityManager() {
        EntityManager em = new AclObjectIdentity().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

    public static long countAclObjectIdentitys() {
        return entityManager().createQuery("SELECT COUNT(o) FROM AclObjectIdentity o", Long.class).getSingleResult();
    }

    public static List<AclObjectIdentity> findAllAclObjectIdentitys() {
        return entityManager().createQuery("SELECT o FROM AclObjectIdentity o", AclObjectIdentity.class).getResultList();
    }

    public static AclObjectIdentity findAclObjectIdentity(Long id) {
        if (id == null) return null;
        return entityManager().find(AclObjectIdentity.class, id);
    }

    public static List<AclObjectIdentity> findAclObjectIdentityEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM AclObjectIdentity o", AclObjectIdentity.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
            AclObjectIdentity attached = AclObjectIdentity.findAclObjectIdentity(this.id);
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
    public AclObjectIdentity merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        AclObjectIdentity merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

}