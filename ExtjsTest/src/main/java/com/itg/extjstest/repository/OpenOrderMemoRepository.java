package com.itg.extjstest.repository;

import com.itg.extjstest.domain.OpenOrderMemo;
import flexjson.JSONSerializer;
import flexjson.transformer.DateTransformer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class OpenOrderMemoRepository {

    @PersistenceContext
    transient EntityManager entityManager;

    public  final EntityManager entityManager() {
        EntityManager em = this.entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

    public  long countOpenOrderMemoes() {
        return entityManager().createQuery("SELECT COUNT(o) FROM OpenOrderMemo o", Long.class).getSingleResult();
    }

    public  List<OpenOrderMemo> findAllOpenOrderMemoes() {
        return entityManager().createQuery("SELECT o FROM OpenOrderMemo o", OpenOrderMemo.class).getResultList();
    }

    public  OpenOrderMemo findOpenOrderMemo(Long id) {
        if (id == null) return null;
        return entityManager().find(OpenOrderMemo.class, id);
    }

    public  List<OpenOrderMemo> findOpenOrderMemoEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM OpenOrderMemo o", OpenOrderMemo.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

    @Transactional
    public void persist(OpenOrderMemo memo) {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(memo);
    }

    @Transactional
    public void remove(OpenOrderMemo memo) {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(memo)) {
            this.entityManager.remove(memo);
        } else {
            OpenOrderMemo attached = findOpenOrderMemo(memo.getId());
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
    public OpenOrderMemo merge(OpenOrderMemo memo) {
        if (this.entityManager == null) this.entityManager = entityManager();
        OpenOrderMemo merged = this.entityManager.merge(memo);
        this.entityManager.flush();
        return merged;
    }


    public  TypedQuery<OpenOrderMemo> findOpenOrderMemoesByModelEquals(String model) {
        if (model == null || model.length() == 0) throw new IllegalArgumentException("The model argument is required");
        EntityManager em = entityManager();
        TypedQuery<OpenOrderMemo> q = em.createQuery("SELECT o FROM OpenOrderMemo AS o WHERE o.model = :model", OpenOrderMemo.class);
        q.setParameter("model", model);
        return q;
    }


}
