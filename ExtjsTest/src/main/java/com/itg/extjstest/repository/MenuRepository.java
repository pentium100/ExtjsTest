package com.itg.extjstest.repository;

import com.itg.extjstest.domain.Menu;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
@Service
public class MenuRepository {

    @PersistenceContext
    transient EntityManager entityManager;

    public final EntityManager entityManager() {
        EntityManager em = this.entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

    public long countMenus() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Menu o", Long.class).getSingleResult();
    }

    public List<Menu> findAllMenus() {
        return entityManager().createQuery("SELECT o FROM Menu o", Menu.class).getResultList();
    }

    public Menu findMenu(Long id) {
        if (id == null) return null;
        return entityManager().find(Menu.class, id);
    }

    public List<Menu> findMenuEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Menu o", Menu.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

    @Transactional
    public void persist(Menu menu) {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(menu);
    }

    @Transactional
    public void remove(Menu menu) {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(menu)) {
            this.entityManager.remove(menu);
        } else {
            Menu attached = findMenu(menu.getId());
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
    public Menu merge(Menu menu) {
        if (this.entityManager == null) this.entityManager = entityManager();
        Menu merged = this.entityManager.merge(menu);
        this.entityManager.flush();
        return merged;
    }


    public TypedQuery<Menu> findMenusByParent(Menu parent) {
        if (parent == null) throw new IllegalArgumentException("The parent argument is required");
        EntityManager em = entityManager();
        TypedQuery<Menu> q = em.createQuery("SELECT o FROM Menu AS o WHERE o.parent = :parent", Menu.class);
        q.setParameter("parent", parent);
        return q;
    }

}
