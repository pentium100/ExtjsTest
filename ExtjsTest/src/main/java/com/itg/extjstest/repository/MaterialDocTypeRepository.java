package com.itg.extjstest.repository;

import com.itg.extjstest.domain.MaterialDocType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Service
public class MaterialDocTypeRepository {

    public  TypedQuery<MaterialDocType> findMaterialDocTypesByDocType_txtEquals(String docType_txt) {
        if (docType_txt == null || docType_txt.length() == 0) throw new IllegalArgumentException("The docType_txt argument is required");
        EntityManager em = entityManager();
        TypedQuery<MaterialDocType> q = em.createQuery("SELECT o FROM MaterialDocType AS o WHERE o.docType_txt = :docType_txt", MaterialDocType.class);
        q.setParameter("docType_txt", docType_txt);
        return q;
    }



    @PersistenceContext
    transient EntityManager entityManager;

    public  final EntityManager entityManager() {
        EntityManager em = this.entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

    public  long countMaterialDocTypes() {
        return entityManager().createQuery("SELECT COUNT(o) FROM MaterialDocType o", Long.class).getSingleResult();
    }

    public List<MaterialDocType> findAllMaterialDocTypes() {
        return entityManager().createQuery("SELECT o FROM MaterialDocType o", MaterialDocType.class).getResultList();
    }

    public  MaterialDocType findMaterialDocType(Long id) {
        if (id == null) return null;
        return entityManager().find(MaterialDocType.class, id);
    }

    public  List<MaterialDocType> findMaterialDocTypeEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM MaterialDocType o", MaterialDocType.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

    @Transactional
    public void persist(MaterialDocType docType) {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(docType);
    }

    @Transactional
    public void remove(MaterialDocType docType) {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(docType)) {
            this.entityManager.remove(docType);
        } else {
            MaterialDocType attached = findMaterialDocType(docType.getId());
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
    public MaterialDocType merge(MaterialDocType docType) {
        if (this.entityManager == null) this.entityManager = entityManager();
        MaterialDocType merged = this.entityManager.merge(docType);
        this.entityManager.flush();
        return merged;
    }

}
