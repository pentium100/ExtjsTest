package com.itg.extjstest.repository;

import com.itg.extjstest.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Service
public class SpecificationRepository {

    @PersistenceContext
    transient EntityManager entityManager;

    public  final EntityManager entityManager() {
        EntityManager em = this.entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

    public  long countSpecifications() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Specification o", Long.class).getSingleResult();
    }

    public  List<Specification> findAllSpecifications() {
        return entityManager().createQuery("SELECT o FROM Specification o", Specification.class).getResultList();
    }

    public  Specification findSpecification(Long id) {
        if (id == null) return null;
        return entityManager().find(Specification.class, id);
    }

    public  List<Specification> findSpecificationEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Specification o", Specification.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

    @Transactional
    public void persist(Specification spec) {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(spec);
    }

    @Transactional
    public void remove(Specification spec) {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(spec)) {
            this.entityManager.remove(spec);
        } else {
            Specification attached = findSpecification(spec.getId());
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
    public Specification merge(Specification spec) {
        if (this.entityManager == null) this.entityManager = entityManager();
        Specification merged = this.entityManager.merge(spec);
        this.entityManager.flush();
        return merged;
    }


}
