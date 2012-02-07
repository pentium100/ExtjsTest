package com.itg.extjstest.domain;

import com.itg.extjstest.util.FilterItem;
import flexjson.JSONSerializer;
import flexjson.transformer.DateTransformer;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.persistence.ManyToOne;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.constraints.Size;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(identifierColumn = "lineId", identifierField = "lineId")
@RooJson
public class MaterialDocItem {

    @ManyToOne
    private MaterialDoc materialDoc;

    @Size(max = 3)
    private String moveType;

    @Size(max = 20)
    private String model_contract;

    @Size(max = 20)
    private String model_tested;

    private Double grossWeight;

    private Double netWeight;

    @Size(max = 50)
    private String warehouse;

    @ManyToOne
    private com.itg.extjstest.domain.MaterialDocItem lineId_in;

    @Size(max = 3000)
    private String remark;

    public static List<com.itg.extjstest.domain.MaterialDocItem> findMaterialDocItemsByFilter(List<com.itg.extjstest.util.FilterItem> filters, int start, int page, int limit) {
        CriteriaBuilder cb = entityManager().getCriteriaBuilder();
        CriteriaQuery<MaterialDocItem> c = cb.createQuery(MaterialDocItem.class);
        Root<MaterialDocItem> root = c.from(MaterialDocItem.class);
        
        Join<MaterialDocItem, MaterialDoc> j = root.join("materialDoc");
        
        Join<MaterialDoc, Contract> contract = j.join("contract");
        HashMap<String, Path> paths = new HashMap<String, Path>();
        paths.put("", root);
        paths.put("materialDoc", j);
        paths.put("contract", contract);
        List<Predicate> criteria = new ArrayList<Predicate>();
        if (filters != null) {
            for (FilterItem f : filters) {
                criteria.add(f.getPredicate(cb, paths));
            }
            c.where(cb.and(criteria.toArray(new Predicate[0])));
        }
        return entityManager().createQuery(c).setFirstResult(start).setMaxResults(limit).getResultList();
    }

    public static String mapToJson(HashMap<java.lang.String, java.lang.Object> map, List<com.itg.extjstest.domain.MaterialDocItem> result) {
        map.put("materialdocitems", result);
        String resultJson = new JSONSerializer().exclude("*.class").include("materialdocitems").include("materialdocitems.materialDoc").include("materialdocitems.materialDoc.docType").transform(new DateTransformer("yyyy-MM-dd HH:mm:ss"), Date.class).serialize(map);
        return resultJson;
    }
}
