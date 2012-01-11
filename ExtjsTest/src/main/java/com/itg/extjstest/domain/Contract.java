package com.itg.extjstest.domain;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import flexjson.JSONSerializer;
import flexjson.transformer.DateTransformer;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
@RooJson
public class Contract {

    @NotNull
    @Column(unique = true)
    @Size(max = 30)
    private String contractNo;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<ContractItem> items = new HashSet<ContractItem>();

    @Enumerated
    private ContractType contractType;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date lastShippingDate;
    
    public String toJson() {
        return new JSONSerializer().exclude("*.class")
        		.transform(new DateTransformer("yyyy-MM-dd'T'HH:mm:ss+0800"), Date.class)
        		.serialize(this);
    }
    
    

    public static String toJsonArray(Collection<Contract> collection) {
        return new JSONSerializer().exclude("*.class")
        		.transform(new DateTransformer("yyyy-MM-dd'T'HH:mm:ss+0800"), Date.class)
        		.include("items")
        		.serialize(collection);
    }
}
