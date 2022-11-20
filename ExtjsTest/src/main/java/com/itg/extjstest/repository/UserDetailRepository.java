package com.itg.extjstest.repository;

import com.itg.extjstest.domain.security.UserDetail;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
@Service
public class UserDetailRepository {


    @PersistenceContext
    public EntityManager entityManager;


    public TypedQuery<UserDetail> findUserDetailsByUserNameEquals(String userName) {
        if (userName == null || userName.length() == 0) throw new IllegalArgumentException("The userName argument is required");
        EntityManager em = entityManager();
        TypedQuery<UserDetail> q = em.createQuery("SELECT o FROM UserDetail AS o WHERE o.userName = :userName", UserDetail.class);
        q.setParameter("userName", userName);
        return q;
    }



    public EntityManager entityManager() {
        EntityManager em = this.entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

    public  long countUserDetails() {
        return entityManager().createQuery("SELECT COUNT(o) FROM UserDetail o", Long.class).getSingleResult();
    }

    public  List<UserDetail> findAllUserDetails() {
        return entityManager().createQuery("SELECT o FROM UserDetail o", UserDetail.class).getResultList();
    }

    public  UserDetail findUserDetail(Long id) {
        if (id == null) return null;
        return entityManager().find(UserDetail.class, id);
    }

    public  List<UserDetail> findUserDetailEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM UserDetail o", UserDetail.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

    @Transactional
    public void persist(UserDetail userDetail) {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(userDetail);
    }

    @Transactional
    public void remove(UserDetail user) {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(user)) {
            this.entityManager.remove(user);
        } else {
            UserDetail attached = this.findUserDetail(user.getId());
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
    public UserDetail merge(UserDetail user) {
        if (this.entityManager == null) this.entityManager = entityManager();
        UserDetail merged = entityManager().merge(user);
        this.entityManager.flush();
        return merged;
    }



}
