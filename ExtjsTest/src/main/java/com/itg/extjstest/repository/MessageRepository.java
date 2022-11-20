package com.itg.extjstest.repository;

import com.itg.extjstest.domain.Message;
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
public class MessageRepository {

    @PersistenceContext
    transient EntityManager entityManager;

    public  final EntityManager entityManager() {
        EntityManager em = this.entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

    public  long countMessages() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Message o", Long.class).getSingleResult();
    }

    public  List<Message> findAllMessages() {
        return entityManager().createQuery("SELECT o FROM Message o", Message.class).getResultList();
    }

    public  Message findMessage(Long id) {
        if (id == null) return null;
        return entityManager().find(Message.class, id);
    }

    public  List<Message> findMessageEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Message o", Message.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

    @Transactional
    public void persist(Message message) {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(message);
    }

    @Transactional
    public void remove(Message message) {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(message)) {
            this.entityManager.remove(message);
        } else {
            Message attached = findMessage(message.getId());
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
    public Message merge(Message message) {
        if (this.entityManager == null) this.entityManager = entityManager();
        Message merged = this.entityManager.merge(message);
        this.entityManager.flush();
        return merged;
    }


    public  List<com.itg.extjstest.domain.Message> findMessagesByFilter(
            List<com.itg.extjstest.util.FilterItem> filters, Integer start,
            Integer page, Integer limit) throws ParseException {
        CriteriaBuilder cb = entityManager().getCriteriaBuilder();
        CriteriaQuery<Message> c = cb.createQuery(Message.class);
        Root<Message> root = c.from(Message.class);
        HashMap<String, Path> paths = new HashMap<String, Path>();
        paths.put("", root);
        List<Predicate> criteria = new ArrayList<Predicate>();
        if (filters != null) {
            for (FilterItem f : filters) {
                criteria.add(f.getPredicate(cb, paths));
            }
            c.where(cb.and(criteria.toArray(new Predicate[0])));
        }
        c.orderBy(cb.desc(root.get("lastChangeTime")));
        return entityManager().createQuery(c).setFirstResult(start)
                .setMaxResults(limit).getResultList();
    }
}
