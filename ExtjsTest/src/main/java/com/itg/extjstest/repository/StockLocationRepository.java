package com.itg.extjstest.repository;

import com.itg.extjstest.domain.StockLocation;
import com.itg.extjstest.util.FilterItem;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class StockLocationRepository {


    @PersistenceContext
    transient EntityManager entityManager;

    public  final EntityManager entityManager() {
        EntityManager em = this.entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    public  List<StockLocation> findStockLocationByFilter(
            List<FilterItem> filters, int start, int page, int limit)
            throws ParseException {

        CriteriaBuilder cb = entityManager().getCriteriaBuilder();
        CriteriaQuery<StockLocation> c = cb.createQuery(StockLocation.class);
        Root<StockLocation> root = c.from(StockLocation.class);
        HashMap<String, Path> paths = new HashMap<String, Path>();
        paths.put("", root);
        List<Predicate> criteria = new ArrayList<Predicate>();
        if (filters != null) {
            for (FilterItem f : filters) {
                criteria.add(f.getPredicate(cb, paths));
            }
            c.where(cb.and(criteria.toArray(new Predicate[0])));
        }

        List<StockLocation> list;
        list = entityManager().createQuery(c).setFirstResult(start)
                .setMaxResults(limit).getResultList();

        return list;

    }

    public  long countStockLocations() {
        return entityManager().createQuery("SELECT COUNT(o) FROM StockLocation o", Long.class).getSingleResult();
    }

    public  List<StockLocation> findAllStockLocations() {
        return entityManager().createQuery("SELECT o FROM StockLocation o", StockLocation.class).getResultList();
    }

    public  StockLocation findStockLocation(Long id) {
        if (id == null) return null;
        return entityManager().find(StockLocation.class, id);
    }

    public  List<StockLocation> findStockLocationEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM StockLocation o", StockLocation.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

    @Transactional
    public void persist(StockLocation stockLocation) {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(stockLocation);
    }

    @Transactional
    public void remove(StockLocation stockLocation) {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(stockLocation)) {
            this.entityManager.remove(stockLocation);
        } else {
            StockLocation attached = findStockLocation(stockLocation.getId());
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
    public StockLocation merge(StockLocation stockLocation) {
        if (this.entityManager == null) this.entityManager = entityManager();
        StockLocation merged = this.entityManager.merge(stockLocation);
        this.entityManager.flush();
        return merged;
    }

}
