package com.itg.extjstest.repository;

import com.itg.extjstest.domain.Contract;
import com.itg.extjstest.domain.MaterialDoc;
import com.itg.extjstest.domain.MaterialDocItem;
import com.itg.extjstest.domain.StockLocation;
import com.itg.extjstest.util.FilterItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class MaterialDocItemRepository {

    @Autowired
    private StockLocationRepository stockLocationRepository;
    @PersistenceContext
    transient EntityManager entityManager;

    public  final EntityManager entityManager() {
        EntityManager em = this.entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

    public  long countMaterialDocItems() {
        return entityManager().createQuery("SELECT COUNT(o) FROM MaterialDocItem o", Long.class).getSingleResult();
    }

    public  List<MaterialDocItem> findAllMaterialDocItems() {
        return entityManager().createQuery("SELECT o FROM MaterialDocItem o", MaterialDocItem.class).getResultList();
    }

    public  MaterialDocItem findMaterialDocItem(Long lineId) {
        if (lineId == null) return null;
        return entityManager().find(MaterialDocItem.class, lineId);
    }

    public  List<MaterialDocItem> findMaterialDocItemEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM MaterialDocItem o", MaterialDocItem.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

    @Transactional
    public void persist(MaterialDocItem item) {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(item);
    }

    @Transactional
    public void remove(MaterialDocItem item) {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(item)) {
            this.entityManager.remove(item);
        } else {
            MaterialDocItem attached = findMaterialDocItem(item.getLineId());
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
    public MaterialDocItem merge(MaterialDocItem item) {
        if (this.entityManager == null) this.entityManager = entityManager();
        MaterialDocItem merged = this.entityManager.merge(item);
        this.entityManager.flush();
        return merged;
    }

    public  int countMaterialDocItemsByFilter(
            List<com.itg.extjstest.util.FilterItem> filters, int start,
            int page, int limit) throws ParseException {
        CriteriaBuilder cb = entityManager().getCriteriaBuilder();
        List<Predicate> criteria = new ArrayList<Predicate>();
        CriteriaQuery<Tuple> c = cb.createTupleQuery();
        Root<MaterialDocItem> fromMaterialDocItem = c
                .from(MaterialDocItem.class);
        Expression<Double> weight = fromMaterialDocItem.get("netWeight");
        Expression<Double> direction = fromMaterialDocItem.get("direction");
        Expression<Double> directionWeight = (Expression<Double>) cb.prod(
                weight, direction);
        Expression<Double> sumOfDirectionWeight = cb.sum(directionWeight);

        Expression<Double> lots = fromMaterialDocItem.get("lots");

        Expression<Double> directionLots = (Expression<Double>) cb.prod(
                lots, direction);
        Expression<Double> sumOfDirectionLots = cb.sum(directionLots);

        c.select(cb.tuple(fromMaterialDocItem.get("lineId_in").get("lineId")
                        .alias("lineId_in"), fromMaterialDocItem.get("stockLocation")
                        .get("id").alias("stockLocation"),
                sumOfDirectionWeight.alias("stockWeight"),
                sumOfDirectionLots.alias("stockLots")));
        c.groupBy(fromMaterialDocItem.get("lineId_in").get("lineId"),
                fromMaterialDocItem.get("stockLocation"));
        c.having(cb.gt(sumOfDirectionWeight, 0));
        Subquery<MaterialDocItem> subq = c.subquery(MaterialDocItem.class);
        Root<MaterialDocItem> subFromMaterialDocItem = subq
                .from(MaterialDocItem.class);
        Root<MaterialDoc> subFromMaterialDoc = subq.from(MaterialDoc.class);
        Root<Contract> subFromContract = subq.from(Contract.class);
        Root<StockLocation> subFromStockLocation = subq
                .from(StockLocation.class);
        subq.select(subFromMaterialDocItem);
        List<Predicate> subCriteria = new ArrayList<Predicate>();
        subCriteria.add(cb.equal(subFromMaterialDocItem.get("lineId"),
                subFromMaterialDocItem.get("lineId_in")));
        subCriteria.add(cb.equal(subFromMaterialDocItem.get("materialDoc"),
                subFromMaterialDoc.get("docNo")));
        subCriteria.add(cb.equal(subFromMaterialDocItem.get("contract"),
                subFromContract.get("id")));
        subCriteria.add(cb.equal(subFromMaterialDocItem.get("stockLocation"),
                subFromStockLocation.get("id")));
        HashMap<String, Path> paths = new HashMap<String, Path>();
        paths.put("", fromMaterialDocItem);
        HashMap<String, Path> subPaths = new HashMap<String, Path>();
        subPaths.put("", subFromMaterialDocItem);
        subPaths.put("materialDoc", subFromMaterialDoc);
        subPaths.put("contract", subFromContract);
        subPaths.put("stockLocation", subFromStockLocation);
        if (filters != null) {
            for (FilterItem f : filters) {
                // if (!f.getField().equals("stockLocation.stockLocation")) {
                subCriteria.add(f.getPredicate(cb, subPaths));
                // } else {
                // criteria.add(f.getPredicate(cb, paths));
                // }
            }
            subq.where(cb.and(subCriteria.toArray(new Predicate[0])));
        }
        c.where(cb.in(fromMaterialDocItem.get("lineId_in")).value(subq));
        return entityManager().createQuery(c).getResultList().size();

    }

    public  List<com.itg.extjstest.domain.MaterialDocItem> findMaterialDocItemsByFilter(
            List<com.itg.extjstest.util.FilterItem> filters, int start,
            int page, int limit) throws ParseException {
        CriteriaBuilder cb = entityManager().getCriteriaBuilder();
        // List<Predicate> criteria = new ArrayList<Predicate>();
        CriteriaQuery<Tuple> c = cb.createTupleQuery();
        Root<MaterialDocItem> fromMaterialDocItem = c
                .from(MaterialDocItem.class);
        Expression<Double> weight = fromMaterialDocItem.get("netWeight");
        Expression<Double> lots = fromMaterialDocItem.get("lots");
        Expression<Double> direction = fromMaterialDocItem.get("direction");
        Expression<Double> directionWeight = (Expression<Double>) cb.prod(
                weight, direction);
        Expression<Double> directionLots = (Expression<Double>) cb.prod(
                lots, direction);
        Expression<Double> sumOfDirectionWeight = cb.sum(directionWeight);
        Expression<Double> sumOfDirectionLots = cb.sum(directionLots);
        c.select(cb.tuple(fromMaterialDocItem.get("lineId_in").get("lineId")
                        .alias("lineId_in"), fromMaterialDocItem.get("stockLocation")
                        .get("id").alias("stockLocation"),
                sumOfDirectionWeight.alias("stockWeight"),
                sumOfDirectionLots.alias("stockLots")
        ));
        c.groupBy(fromMaterialDocItem.get("lineId_in").get("lineId"),
                fromMaterialDocItem.get("stockLocation"));
        c.having(cb.gt(sumOfDirectionWeight, 0));
        Subquery<MaterialDocItem> subq = c.subquery(MaterialDocItem.class);
        Root<MaterialDocItem> subFromMaterialDocItem = subq
                .from(MaterialDocItem.class);
        Root<MaterialDoc> subFromMaterialDoc = subq.from(MaterialDoc.class);
        Root<Contract> subFromContract = subq.from(Contract.class);
        Root<StockLocation> subFromStockLocation = subq
                .from(StockLocation.class);
        subq.select(subFromMaterialDocItem);
        List<Predicate> subCriteria = new ArrayList<Predicate>();
        subCriteria.add(cb.equal(subFromMaterialDocItem.get("lineId"),
                subFromMaterialDocItem.get("lineId_in")));
        subCriteria.add(cb.equal(subFromMaterialDocItem.get("materialDoc"),
                subFromMaterialDoc.get("docNo")));
        subCriteria.add(cb.equal(subFromMaterialDocItem.get("contract"),
                subFromContract.get("id")));
        subCriteria.add(cb.equal(subFromMaterialDocItem.get("stockLocation"),
                subFromStockLocation.get("id")));
        HashMap<String, Path> paths = new HashMap<String, Path>();
        paths.put("", fromMaterialDocItem);
        HashMap<String, Path> subPaths = new HashMap<String, Path>();
        subPaths.put("", subFromMaterialDocItem);
        subPaths.put("materialDoc", subFromMaterialDoc);
        subPaths.put("contract", subFromContract);
        subPaths.put("stockLocation", subFromStockLocation);
        if (filters != null) {
            for (FilterItem f : filters) {
                // if (!f.getField().equals("stockLocation.stockLocation")) {
                subCriteria.add(f.getPredicate(cb, subPaths));
                // } else {
                // criteria.add(f.getPredicate(cb, paths));
                // }
            }
            subq.where(cb.and(subCriteria.toArray(new Predicate[0])));
        }
        c.where(cb.in(fromMaterialDocItem.get("lineId_in")).value(subq));
        List<Tuple> result = entityManager().createQuery(c)
                .setFirstResult(start).setMaxResults(limit).getResultList();
        List<MaterialDocItem> l = new ArrayList<MaterialDocItem>();
        for (Tuple o : result) {
            Long lineId = (Long) o.get("lineId_in");
            MaterialDocItem item = findMaterialDocItem(lineId);
            StockLocation stockLocation = stockLocationRepository
                    .findStockLocation((Long) o.get("stockLocation"));

            MaterialDoc materialDoc = item.getMaterialDoc();
            Contract contract = materialDoc.getContract();
            item.setWarehouse(stockLocation.getStockLocation());
            item.setNetWeight((Double) o.get("stockWeight"));
            entityManager().detach(item);
            l.add(item);
        }

        return l;
    }

    public  int countIncomingMaterialDocItemsByFilter(
            List<com.itg.extjstest.util.FilterItem> filters, int start,
            int page, int limit) throws ParseException {
        CriteriaBuilder cb = entityManager().getCriteriaBuilder();
        CriteriaQuery<MaterialDocItem> c = cb
                .createQuery(MaterialDocItem.class);
        Root<MaterialDocItem> root = c.from(MaterialDocItem.class);
        Join<MaterialDocItem, MaterialDoc> materialDoc = root
                .join("materialDoc");
        Join<MaterialDoc, Contract> contract = materialDoc.join("contract");
        HashMap<String, Path> paths = new HashMap<String, Path>();
        paths.put("", root);
        paths.put("materialDoc", materialDoc);
        paths.put("contract", contract);
        List<Predicate> criteria = new ArrayList<Predicate>();
        if (filters != null) {
            for (FilterItem f : filters) {
                if (f.getField().equals("contractNo")) {
                    f.setField("contract.contractNo");
                }
                criteria.add(f.getPredicate(cb, paths));
            }
            c.where(cb.and(criteria.toArray(new Predicate[0])));
        }

        return entityManager().createQuery(c).getResultList().size();
    }

    public  List<com.itg.extjstest.domain.MaterialDocItem> findIncomingMaterialDocItemsByFilter(
            List<com.itg.extjstest.util.FilterItem> filters, int start,
            int page, int limit) throws ParseException {
        CriteriaBuilder cb = entityManager().getCriteriaBuilder();
        CriteriaQuery<MaterialDocItem> c = cb
                .createQuery(MaterialDocItem.class);
        Root<MaterialDocItem> root = c.from(MaterialDocItem.class);
        Join<MaterialDocItem, MaterialDoc> materialDoc = root
                .join("materialDoc");
        Join<MaterialDoc, Contract> contract = materialDoc.join("contract");
        HashMap<String, Path> paths = new HashMap<String, Path>();
        paths.put("", root);
        paths.put("materialDoc", materialDoc);
        paths.put("contract", contract);
        List<Predicate> criteria = new ArrayList<Predicate>();
        if (filters != null) {
            for (FilterItem f : filters) {
                if (f.getField().equals("contractNo")) {
                    f.setField("contract.contractNo");
                }
                criteria.add(f.getPredicate(cb, paths));
            }
            c.where(cb.and(criteria.toArray(new Predicate[0])));
        }

        return entityManager().createQuery(c).setFirstResult(start)
                .setMaxResults(limit).getResultList();
    }

    public  TypedQuery<MaterialDocItem> findMaterialDocItemsByLineId_up(MaterialDocItem lineId_up) {
        if (lineId_up == null) throw new IllegalArgumentException("The lineId_up argument is required");
        EntityManager em = entityManager();
        TypedQuery<MaterialDocItem> q = em.createQuery("SELECT o FROM MaterialDocItem AS o WHERE o.lineId_up = :lineId_up", MaterialDocItem.class);
        q.setParameter("lineId_up", lineId_up);
        return q;
    }

}
