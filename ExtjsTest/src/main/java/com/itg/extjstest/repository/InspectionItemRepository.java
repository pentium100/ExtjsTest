package com.itg.extjstest.repository;

import com.itg.extjstest.domain.InspectionItem;
import com.itg.extjstest.domain.MaterialDocItem;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Service
public class InspectionItemRepository {

    public  TypedQuery<InspectionItem> findInspectionItemsByMaterialDocItem(MaterialDocItem materialDocItem) {
        if (materialDocItem == null) throw new IllegalArgumentException("The materialDocItem argument is required");
        EntityManager em = this.entityManager();
        TypedQuery<InspectionItem> q = em.createQuery("SELECT o FROM InspectionItem AS o WHERE o.materialDocItem = :materialDocItem", InspectionItem.class);
        q.setParameter("materialDocItem", materialDocItem);
        return q;
    }



    @PersistenceContext
    transient EntityManager entityManager;

    public  final EntityManager entityManager() {
        EntityManager em = this.entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

    public  long countInspectionItems() {
        return entityManager().createQuery("SELECT COUNT(o) FROM InspectionItem o", Long.class).getSingleResult();
    }

    public  List<InspectionItem> findAllInspectionItems() {
        return entityManager().createQuery("SELECT o FROM InspectionItem o", InspectionItem.class).getResultList();
    }

    public  InspectionItem findInspectionItem(Long id) {
        if (id == null) return null;
        return entityManager().find(InspectionItem.class, id);
    }

    public  List<InspectionItem> findInspectionItemEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM InspectionItem o", InspectionItem.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

    @Transactional
    public void persist(InspectionItem  item) {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(item);
    }

    @Transactional
    public void remove(InspectionItem item) {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(item)) {
            this.entityManager.remove(item);
        } else {
            InspectionItem attached = findInspectionItem(item.getId());
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
    public InspectionItem merge(InspectionItem item) {
        if (this.entityManager == null) this.entityManager = entityManager();
        InspectionItem merged = this.entityManager.merge(item);
        this.entityManager.flush();
        return merged;
    }

}
