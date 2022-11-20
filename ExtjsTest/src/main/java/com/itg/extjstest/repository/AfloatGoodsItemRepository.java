package com.itg.extjstest.repository;

import com.itg.extjstest.domain.AfloatGoodsItem;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AfloatGoodsItemRepository {

    @PersistenceContext
    transient EntityManager entityManager;

    public final EntityManager entityManager() {
        EntityManager em = this.entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

    public long countAfloatGoodsItems() {
        return entityManager().createQuery("SELECT COUNT(o) FROM AfloatGoodsItem o", Long.class).getSingleResult();
    }

    public List<AfloatGoodsItem> findAllAfloatGoodsItems() {
        return entityManager().createQuery("SELECT o FROM AfloatGoodsItem o", AfloatGoodsItem.class).getResultList();
    }

    public AfloatGoodsItem findAfloatGoodsItem(Long id) {
        if (id == null) return null;
        return entityManager().find(AfloatGoodsItem.class, id);
    }

    public List<AfloatGoodsItem> findAfloatGoodsItemEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM AfloatGoodsItem o", AfloatGoodsItem.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

    @Transactional
    public void persist(AfloatGoodsItem item) {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(item);
    }

    @Transactional
    public void remove(AfloatGoodsItem item) {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(item)) {
            this.entityManager.remove(item);
        } else {
            AfloatGoodsItem attached = findAfloatGoodsItem(item.getId());
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
    public AfloatGoodsItem merge(AfloatGoodsItem item) {
        if (this.entityManager == null) this.entityManager = entityManager();
        AfloatGoodsItem merged = this.entityManager.merge(item);
        this.entityManager.flush();
        return merged;
    }

    public String toJson() {
        return new JSONSerializer().exclude("*.class").serialize(this);
    }

    public String toJson(String[] fields) {
        return new JSONSerializer().include(fields).exclude("*.class").serialize(this);
    }

    public static AfloatGoodsItem fromJsonToAfloatGoodsItem(String json) {
        return new JSONDeserializer<AfloatGoodsItem>().use(null, AfloatGoodsItem.class).deserialize(json);
    }

    public static String toJsonArray(Collection<AfloatGoodsItem> collection) {
        return new JSONSerializer().exclude("*.class").serialize(collection);
    }

    public static String toJsonArray(Collection<AfloatGoodsItem> collection, String[] fields) {
        return new JSONSerializer().include(fields).exclude("*.class").serialize(collection);
    }

    public static Collection<AfloatGoodsItem> fromJsonArrayToAfloatGoodsItems(String json) {
        return new JSONDeserializer<List<AfloatGoodsItem>>().use(null, ArrayList.class).use("values", AfloatGoodsItem.class).deserialize(json);
    }


}
