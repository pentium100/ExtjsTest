package com.itg.extjstest.domain;

import com.itg.extjstest.util.FilterItem;
import flexjson.JSONSerializer;
import flexjson.transformer.DateTransformer;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.persistence.Tuple;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;
import javax.persistence.criteria.Subquery;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
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

    
    @Column(nullable = true)
    private short direction;

    public static List<com.itg.extjstest.domain.MaterialDocItem> findMaterialDocItemsByFilter(List<com.itg.extjstest.util.FilterItem> filters, int start, int page, int limit) {
        
    	CriteriaBuilder cb = entityManager().getCriteriaBuilder();
        
        List<Predicate> criteria = new ArrayList<Predicate>();
        CriteriaQuery<Tuple> c = cb.createTupleQuery();
        
        Root<MaterialDocItem> root = c.from(MaterialDocItem.class);
        Join<MaterialDocItem, MaterialDoc> header = root.join("materialDoc");
        Join<MaterialDoc, Contract> contract = header.join("contract");
    
        
        
        Subquery<Tuple> subQuery = c.subquery(Tuple.class);
        Root<MaterialDocItem> out = subQuery.from(MaterialDocItem.class);
        subQuery.where(cb.isNotNull(out.get("lineId_in")));

        Expression<Double> weight = out.get("netWeight");
        Expression<Integer> direction = out.get("direction");
        Expression<? extends Number> directionWeight = cb.prod(weight, direction);
        Expression<? extends Number> sumOfdirectionWeight = cb.sum(directionWeight);
        //subQuery.select(sumOfdirectionWeight);
        //Join<MaterialDocItem, MaterialDocItem> out = root.join("lineId_in");
        
        
        criteria.add(cb.isNull(root.get("lineId_in")));   // lineId_in 为空, 表示为首条进仓记录.
        //criteria.add(cb.isNotNull(out.get("lineId_in")));   //        lineId_in 不为空, 表示为后继进出仓记录.
        
        
        //Predicate gt1 = cb.gt(directionWeight, 0);
        //criteria.add(gt1);
        
        
        //c.select(root.get("lineId"), cb.sum(directionWeight));
        c.groupBy(root.get("lineId"));
        c.having(cb.gt(cb.sum(directionWeight), 0));
        HashMap<String, Path> paths = new HashMap<String, Path>();
        paths.put("", root);
        paths.put("materialDoc", header);
        paths.put("contract", contract);
        
        if (filters != null) {
            for (FilterItem f : filters) {
                criteria.add(f.getPredicate(cb, paths));
            }
            c.where(cb.and(criteria.toArray(new Predicate[0])));
        }
        
        
        //List<Object[]> result = entityManager().createQuery(c).setFirstResult(start).setMaxResults(limit).getResultList();
        //List<MaterialDocItem> l = new ArrayList<MaterialDocItem>();
        //for(Object[] o :result){
        	
        //	MaterialDocItem item = new MaterialDocItem();
        	//item.setLineId((Long) o[0]);
        	//item.setNetWeight((Double) o[1]);
        //	l.add(item);
        	
        //}
        
        return null;
        //return entityManager().createQuery(c).setFirstResult(start).setMaxResults(limit).getResultList();
    	
    	//return null;
    			
    }

    public static String mapToJson(HashMap<java.lang.String, java.lang.Object> map, List<com.itg.extjstest.domain.MaterialDocItem> result) {
        map.put("materialdocitems", result);
        String resultJson = new JSONSerializer().exclude("*.class").include("materialdocitems").include("materialdocitems.materialDoc").include("materialdocitems.materialDoc.docType").transform(new DateTransformer("yyyy-MM-dd HH:mm:ss"), Date.class).serialize(map);
        return resultJson;
    }
}
