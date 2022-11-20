package com.itg.extjstest.repository;

import com.itg.extjstest.domain.AfloatGoods;
import com.itg.extjstest.domain.AfloatGoodsItem;
import com.itg.extjstest.domain.ContractType;
import com.itg.extjstest.util.ContractTypeObjectFactory;
import com.itg.extjstest.util.FilterItem;
import com.itg.extjstest.util.SortItem;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import flexjson.transformer.DateTransformer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class AfloatGoodsRepository {



    @PersistenceContext
    transient EntityManager entityManager;

    public final EntityManager entityManager() {
        EntityManager em = this.entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }


    public long countAfloatGoodses() {
        return entityManager().createQuery("SELECT COUNT(o) FROM AfloatGoods o", Long.class).getSingleResult();
    }

    public List<AfloatGoods> findAllAfloatGoodses() {
        return entityManager().createQuery("SELECT o FROM AfloatGoods o", AfloatGoods.class).getResultList();
    }

    public AfloatGoods findAfloatGoods(Long id) {
        if (id == null) return null;
        return entityManager().find(AfloatGoods.class, id);
    }

    public List<AfloatGoods> findAfloatGoodsEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM AfloatGoods o", AfloatGoods.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

    @Transactional
    public void persist(AfloatGoods af) {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(af);
    }

    @Transactional
    public void remove(AfloatGoods goods) {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(goods)) {
            this.entityManager.remove(goods);
        } else {
            AfloatGoods attached = findAfloatGoods(goods.getId());
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
    public AfloatGoods merge(AfloatGoods goods) {
        if (this.entityManager == null) this.entityManager = entityManager();
        AfloatGoods merged = this.entityManager.merge(goods);
        this.entityManager.flush();
        return merged;
    }


    public  List<com.itg.extjstest.domain.AfloatGoods> findAfloatGoodsByFilter(
            List<com.itg.extjstest.util.FilterItem> filters, Integer start,
            Integer page, Integer limit, List<SortItem> sorts)
            throws ParseException {
        CriteriaBuilder cb = entityManager().getCriteriaBuilder();
        CriteriaQuery<AfloatGoods> c = cb.createQuery(AfloatGoods.class);
        Root<AfloatGoods> afloatGoods = c.from(AfloatGoods.class);
        Join<AfloatGoods, AfloatGoodsItem> j = afloatGoods.join("items");
        HashMap<String, Path> paths = new HashMap<String, Path>();
        paths.put("", afloatGoods);
        paths.put("items", j);
        List<Predicate> criteria = new ArrayList<Predicate>();
        if (filters != null) {
            for (FilterItem f : filters) {
                criteria.add(f.getPredicate(cb, paths));
            }
            c.where(cb.and(criteria.toArray(new Predicate[0])));
        }

        List<Order> orders = new ArrayList<Order>();
        if (sorts != null) {
            for (SortItem s : sorts) {

                orders.add(s.buildSortQuery(cb, paths));

            }
        }

        c.orderBy(orders);

        return entityManager().createQuery(c).setFirstResult(start)
                .setMaxResults(limit).getResultList();
    }


}
