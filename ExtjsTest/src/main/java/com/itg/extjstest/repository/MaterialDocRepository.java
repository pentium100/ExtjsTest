package com.itg.extjstest.repository;

import com.itg.extjstest.domain.Contract;
import com.itg.extjstest.domain.MaterialDoc;
import com.itg.extjstest.util.FilterItem;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Tuple;
import javax.persistence.criteria.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class MaterialDocRepository {

    @PersistenceContext
    transient EntityManager entityManager;

    public final EntityManager entityManager() {
        EntityManager em = this.entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

    public  long countMaterialDocs() {
        return entityManager().createQuery("SELECT COUNT(o) FROM MaterialDoc o", Long.class).getSingleResult();
    }

    public  List<MaterialDoc> findAllMaterialDocs() {
        return entityManager().createQuery("SELECT o FROM MaterialDoc o", MaterialDoc.class).getResultList();
    }

    public  MaterialDoc findMaterialDoc(Long docNo) {
        if (docNo == null) return null;
        return entityManager().find(MaterialDoc.class, docNo);
    }

    public  List<MaterialDoc> findMaterialDocEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM MaterialDoc o", MaterialDoc.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

    @Transactional
    public void persist(MaterialDoc doc) {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(doc);
    }

    @Transactional
    public void remove(MaterialDoc doc) {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(doc)) {
            this.entityManager.remove(doc);
        } else {
            MaterialDoc attached = findMaterialDoc(doc.getDocNo());
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


    @Transactional(propagation= Propagation.REQUIRED)
    public MaterialDoc merge(MaterialDoc doc) {
        if (this.entityManager == null) this.entityManager = entityManager();
        MaterialDoc merged = this.entityManager.merge(doc);
        this.entityManager.flush();
        return merged;
    }

    public  Long countMaterialDocsByFilter(
            List<com.itg.extjstest.util.FilterItem> filters)
            throws ParseException {

        CriteriaBuilder cb = entityManager().getCriteriaBuilder();
        CriteriaQuery<Tuple> c = cb.createTupleQuery();

        Root<MaterialDoc> root = c.from(MaterialDoc.class);

        Expression<Long> id = root.get("docNo");
        Expression<Long> countId = (Expression<Long>) cb.count(id);

        c.select(cb.tuple(countId.alias("total")));
        HashMap<String, Path> paths = new HashMap<String, Path>();
        paths.put("", root);
        List<Predicate> criteria = new ArrayList<Predicate>();
        if (filters != null) {
            for (FilterItem f : filters) {
                if (f.getField().contains("contractNo")) {
                    f.setField("contract.contractNo");
                    Join<MaterialDoc, Contract> fromContract = root
                            .join("contract");
                    paths.put("contract", fromContract);
                }
                criteria.add(f.getPredicate(cb, paths));
            }
            c.where(cb.and(criteria.toArray(new Predicate[0])));
        }
        Tuple result = entityManager().createQuery(c).getSingleResult();
        return (Long) result.get("total");

    }

    public  List<com.itg.extjstest.domain.MaterialDoc> findMaterialDocsByFilter(
            List<com.itg.extjstest.util.FilterItem> filters, Integer start,
            Integer page, Integer limit) throws ParseException {
        CriteriaBuilder cb = entityManager().getCriteriaBuilder();
        CriteriaQuery<MaterialDoc> c = cb.createQuery(MaterialDoc.class);
        Root<MaterialDoc> root = c.from(MaterialDoc.class);
        HashMap<String, Path> paths = new HashMap<String, Path>();
        paths.put("", root);
        List<Predicate> criteria = new ArrayList<Predicate>();
        if (filters != null) {
            for (FilterItem f : filters) {
                if (f.getField().equals("contractNo")) {
                    f.setField("contract.contractNo");
                    Join<MaterialDoc, Contract> fromContract = root
                            .join("contract");
                    paths.put("contract", fromContract);
                }
                criteria.add(f.getPredicate(cb, paths));
            }
            c.where(cb.and(criteria.toArray(new Predicate[0])));
        }
        return entityManager().createQuery(c).setFirstResult(start)
                .setMaxResults(limit).getResultList();
    }
}
