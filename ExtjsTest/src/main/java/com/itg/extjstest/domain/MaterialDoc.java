package com.itg.extjstest.domain;

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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import com.itg.extjstest.util.ContractTypeObjectFactory;

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
				.include("materialdocs")
				.include("materialdocs.items")
				.transform(new DateTransformer("yyyy-MM-dd HH:mm:ss"),Date.class)
				.serialize(map);
		
		return resultJson;


	}
    
}
