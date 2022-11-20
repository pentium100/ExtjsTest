package com.itg.extjstest.repository;

import com.itg.extjstest.domain.Employee;
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
public class EmployeeRepository {


    @PersistenceContext
    transient EntityManager entityManager;

    public final EntityManager entityManager() {
        EntityManager em = this.entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

    public  long countEmployees() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Employee o", Long.class).getSingleResult();
    }

    public  List<Employee> findAllEmployees() {
        return entityManager().createQuery("SELECT o FROM Employee o", Employee.class).getResultList();
    }

    public  Employee findEmployee(Long id) {
        if (id == null) return null;
        return entityManager().find(Employee.class, id);
    }

    public  List<Employee> findEmployeeEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Employee o", Employee.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

    @Transactional
    public void persist(Employee employee) {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(employee);
    }

    @Transactional
    public void remove(Employee e) {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(e)) {
            this.entityManager.remove(e);
        } else {
            Employee attached = findEmployee(e.getId());
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
    public Employee merge(Employee e) {
        if (this.entityManager == null) this.entityManager = entityManager();
        Employee merged = this.entityManager.merge(e);
        this.entityManager.flush();
        return merged;
    }


    public List<Employee> findEmployeeByFilter(List<FilterItem> filters,
                                                      int start, int page, int limit) throws ParseException {

        CriteriaBuilder cb = entityManager().getCriteriaBuilder();
        CriteriaQuery<Employee> c = cb.createQuery(Employee.class);
        Root<Employee> root = c.from(Employee.class);
        HashMap<String, Path> paths = new HashMap<String, Path>();
        paths.put("", root);
        List<Predicate> criteria = new ArrayList<Predicate>();
        if (filters != null) {
            for (FilterItem f : filters) {
                criteria.add(f.getPredicate(cb, paths));
            }
            c.where(cb.and(criteria.toArray(new Predicate[0])));
        }

        List<Employee> list;
        list = entityManager().createQuery(c).setFirstResult(start)
                .setMaxResults(limit).getResultList();

        return list;
    }

}
