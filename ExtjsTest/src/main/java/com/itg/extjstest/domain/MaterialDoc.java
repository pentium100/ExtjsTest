package com.itg.extjstest.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import com.itg.extjstest.util.ContractTypeObjectFactory;
import com.itg.extjstest.util.FilterItem;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import flexjson.transformer.DateTransformer;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(identifierColumn = "docNo", identifierField = "docNo")
@RooJson
public class MaterialDoc {

    @ManyToOne
    private Contract contract;

    @Size(max = 50)
    private String deliveryNote;

    @Size(max = 50)
    private String batchNo;

    @Size(max = 50)
    private String plateNum;

    @Size(max = 50)
    private String workingNo;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date docDate;

    @OneToMany(cascade = CascadeType.ALL)
    private Set<MaterialDocItem> items = new HashSet<MaterialDocItem>();

    @ManyToOne
    @NotNull
    private MaterialDocType docType;
    
    
    public static String toJsonArray(Collection<MaterialDoc> collection) {
        return new JSONSerializer().exclude("*.class")
        		.transform(new DateTransformer("yyyy-MM-dd HH:mm:ss"), Date.class)
        		.serialize(collection);
    }

    public static MaterialDoc fromJsonToMaterialDoc(String json) {
        return new JSONDeserializer<MaterialDoc>()
        		.use(null, MaterialDoc.class)
        		.use(ContractType.class, new ContractTypeObjectFactory())
        		.use("contract.contractType", new ContractTypeObjectFactory())
        		.deserialize(json);
    }



	public static String mapToJson(HashMap<String, Object> map,
			List<MaterialDoc> result) {
		map.put("materialdocs", result);
		String resultJson = new JSONSerializer()
				.exclude("*.class")
				//.exclude("materialdocs.items.lineId_in.materialDoc.contract")
                //.exclude("materialdocs.items.lineId_in.materialDoc.items")				
                //.exclude("materialdocs.items.lineId_in.materialDoc.docType")
                .include("materialdocs")
				.include("materialdocs.items")
				.include("materialdocs.contract")
				.include("materialdocs.docType")
                .include("materialdocs.items.lineId_in")
                .include("materialdocs.items.lineId_in.lineId")
                .include("materialdocs.items.lineId_in.version")
		        .exclude("materialdocs.items.lineId_in.*.*")				
				.transform(new DateTransformer("yyyy-MM-dd HH:mm:ss"),Date.class)
				.serialize(map);
		
		return resultJson;


	}
	
	public static List<MaterialDoc> findMaterialDocsByFilter(
			List<FilterItem> filters, Integer start, Integer page, Integer limit) {
               
               
		CriteriaBuilder cb = entityManager().getCriteriaBuilder();
		CriteriaQuery<MaterialDoc> c = cb.createQuery(MaterialDoc.class);
		Root<MaterialDoc> root = c.from(MaterialDoc.class);
		
		Join<MaterialDoc,MaterialDocItem> j = root.join("items");
		
		HashMap<String, Path> paths = new HashMap<String, Path>();
		paths.put("", root);
		paths.put("items", j);
		
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
	
    
}
