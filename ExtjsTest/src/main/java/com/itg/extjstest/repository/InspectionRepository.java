package com.itg.extjstest.repository;

import com.itg.extjstest.domain.Inspection;
import com.itg.extjstest.domain.InspectionItem;
import com.itg.extjstest.domain.MaterialDocItem;
import com.itg.extjstest.util.FilterItem;
import org.springframework.beans.factory.annotation.Autowired;
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
public class InspectionRepository {

    @PersistenceContext
    transient EntityManager entityManager;

    @Autowired
    private InspectionItemRepository inspectionItemRepository;
    @Autowired
    private MaterialDocItemRepository materialDocItemRepository;

    public  final EntityManager entityManager() {
        EntityManager em = this.entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

    public  long countInspections() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Inspection o", Long.class).getSingleResult();
    }

    public  List<Inspection> findAllInspections() {
        return entityManager().createQuery("SELECT o FROM Inspection o", Inspection.class).getResultList();
    }

    public  Inspection findInspection(Long id) {
        if (id == null) return null;
        return entityManager().find(Inspection.class, id);
    }

    public  List<Inspection> findInspectionEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Inspection o", Inspection.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

    @Transactional
    public void persist( Inspection inspection) {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(inspection);
    }

    @Transactional
    public void remove(Inspection inspection) {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(inspection)) {
            this.entityManager.remove(inspection);
        } else {
            Inspection attached = findInspection(inspection.getId());
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
    public Inspection merge(Inspection inspection) {
        if (this.entityManager == null) this.entityManager = entityManager();
        Inspection merged = this.entityManager.merge(inspection);
        this.entityManager.flush();
        return merged;
    }


    public  List<com.itg.extjstest.domain.Inspection> findInspectionByFilter(
            List<com.itg.extjstest.util.FilterItem> filters, Integer start,
            Integer page, Integer limit) throws ParseException {
        CriteriaBuilder cb = entityManager().getCriteriaBuilder();
        CriteriaQuery<Inspection> c = cb.createQuery(Inspection.class);
        Root<Inspection> inspection = c.from(Inspection.class);
        HashMap<String, Path> paths = new HashMap<String, Path>();
        paths.put("", inspection);
        List<Predicate> criteria = new ArrayList<Predicate>();
        if (filters != null) {
            for (FilterItem f : filters) {
                criteria.add(f.getPredicate(cb, paths));
            }
            c.where(cb.and(criteria.toArray(new Predicate[0])));
        }
        return entityManager().createQuery(c).setFirstResult(start)
                .setMaxResults(limit).getResultList();
    }

    public void updateIsLast(Inspection inspection) {
        for (InspectionItem item : inspection.getItems()) {
            List<InspectionItem> needToBeCheckItems = inspectionItemRepository
                    .findInspectionItemsByMaterialDocItem(
                            item.getMaterialDocItem()).getResultList();
            InspectionItem maxDate = null;
            for (InspectionItem item2 : needToBeCheckItems) {
                if (maxDate == null) {
                    maxDate = item2;
                }
                if (maxDate.getInspection().getInspectionDate()
                        .before(item2.getInspection().getInspectionDate())) {
                    maxDate = item2;
                }
            }
            for (InspectionItem item2 : needToBeCheckItems) {
                item2.setIsLast(maxDate == item2);
                inspectionItemRepository.persist(item2);
            }

            assert maxDate != null;
            if (maxDate.getInspection().getModel_tested() != "") {
                MaterialDocItem materialDocItem = item.getMaterialDocItem();
                materialDocItem.setModel_tested(maxDate.getInspection()
                        .getModel_tested());
                materialDocItemRepository.persist(materialDocItem);
            }
        }
    }
}
