package com.itg.extjstest.repository;

import com.itg.extjstest.domain.Contract;
import com.itg.extjstest.domain.ContractItem;
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
public class ContractRepository {


    @PersistenceContext
    transient EntityManager entityManager;

    public final EntityManager entityManager() {
        EntityManager em = this.entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

    public long countContracts() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Contract o", Long.class).getSingleResult();
    }

    public List<Contract> findAllContracts() {
        return entityManager().createQuery("SELECT o FROM Contract o", Contract.class).getResultList();
    }

    public  Contract findContract(Long id) {
        if (id == null) return null;
        return entityManager().find(Contract.class, id);
    }

    public  List<Contract> findContractEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Contract o", Contract.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

    @Transactional
    public void persist(Contract contract) {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(contract);
    }

    @Transactional
    public void remove(Contract contract) {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(contract)) {
            this.entityManager.remove(contract);
        } else {
            Contract attached = findContract(contract.getId());
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
    public Contract merge(Contract contract) {
        if (this.entityManager == null) this.entityManager = entityManager();
        Contract merged = this.entityManager.merge(contract);
        this.entityManager.flush();
        return merged;
    }


    public  List<com.itg.extjstest.domain.Contract> findContractsByFilter(
            List<com.itg.extjstest.util.FilterItem> filters, Integer start,
            Integer page, Integer limit, boolean byItems) throws ParseException {
        CriteriaBuilder cb = entityManager().getCriteriaBuilder();
        CriteriaQuery<Contract> c = cb.createQuery(Contract.class);
        Root<Contract> rootContract = c.from(Contract.class);
        HashMap<String, Path> paths = new HashMap<String, Path>();
        paths.put("", rootContract);
        List<Predicate> criteria = new ArrayList<Predicate>();
        if (byItems) {
            Join<Contract, ContractItem> j = rootContract.join("items");
            paths.put("items", j);
        }
        if (filters != null) {
            for (FilterItem f : filters) {
                if (f.getField().equals("contractType")) {
                    f.setType("sList");
                }
                criteria.add(f.getPredicate(cb, paths));
            }
            c.where(cb.and(criteria.toArray(new Predicate[0])));
        }
        List<com.itg.extjstest.domain.Contract> list;
        list = entityManager().createQuery(c).setFirstResult(start)
                .setMaxResults(limit).getResultList();
        return list;
    }

}
