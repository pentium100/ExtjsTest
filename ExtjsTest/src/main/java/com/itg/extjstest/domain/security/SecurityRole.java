package com.itg.extjstest.domain.security;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(finders = { "findSecurityRolesByRoleNameEquals", "findSecurityRolesByRoleNameLike" })
@Entity
public class SecurityRole {

    @NotNull
    @Size(max = 45)
    private String roleName;


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Version
    @Column(name = "version")
    private Integer version;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getVersion() {
        return this.version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
    public String getRoleName() {
        return this.roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }


    public static TypedQuery<SecurityRole> findSecurityRolesByRoleNameEquals(String roleName) {
        if (roleName == null || roleName.length() == 0) throw new IllegalArgumentException("The roleName argument is required");
        EntityManager em = SecurityRole.entityManager();
        TypedQuery<SecurityRole> q = em.createQuery("SELECT o FROM SecurityRole AS o WHERE o.roleName = :roleName", SecurityRole.class);
        q.setParameter("roleName", roleName);
        return q;
    }

    public static TypedQuery<SecurityRole> findSecurityRolesByRoleNameLike(String roleName) {
        if (roleName == null || roleName.length() == 0) throw new IllegalArgumentException("The roleName argument is required");
        roleName = roleName.replace('*', '%');
        if (roleName.charAt(0) != '%') {
            roleName = "%" + roleName;
        }
        if (roleName.charAt(roleName.length() - 1) != '%') {
            roleName = roleName + "%";
        }
        EntityManager em = SecurityRole.entityManager();
        TypedQuery<SecurityRole> q = em.createQuery("SELECT o FROM SecurityRole AS o WHERE LOWER(o.roleName) LIKE LOWER(:roleName)", SecurityRole.class);
        q.setParameter("roleName", roleName);
        return q;
    }

    @PersistenceContext
    transient EntityManager entityManager;

    public static final EntityManager entityManager() {
        EntityManager em = new SecurityRole().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

    public static long countSecurityRoles() {
        return entityManager().createQuery("SELECT COUNT(o) FROM SecurityRole o", Long.class).getSingleResult();
    }

    public static List<SecurityRole> findAllSecurityRoles() {
        return entityManager().createQuery("SELECT o FROM SecurityRole o", SecurityRole.class).getResultList();
    }

    public static SecurityRole findSecurityRole(Long id) {
        if (id == null) return null;
        return entityManager().find(SecurityRole.class, id);
    }

    public static List<SecurityRole> findSecurityRoleEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM SecurityRole o", SecurityRole.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
            SecurityRole attached = SecurityRole.findSecurityRole(this.id);
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
    public SecurityRole  merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        SecurityRole merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }



}
