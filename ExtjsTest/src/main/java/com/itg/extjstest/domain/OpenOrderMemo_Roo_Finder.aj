// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.itg.extjstest.domain;

import com.itg.extjstest.domain.OpenOrderMemo;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

privileged aspect OpenOrderMemo_Roo_Finder {
    
    public static TypedQuery<OpenOrderMemo> OpenOrderMemo.findOpenOrderMemoesByModelEquals(String model) {
        if (model == null || model.length() == 0) throw new IllegalArgumentException("The model argument is required");
        EntityManager em = OpenOrderMemo.entityManager();
        TypedQuery<OpenOrderMemo> q = em.createQuery("SELECT o FROM OpenOrderMemo AS o WHERE o.model = :model", OpenOrderMemo.class);
        q.setParameter("model", model);
        return q;
    }
    
}
