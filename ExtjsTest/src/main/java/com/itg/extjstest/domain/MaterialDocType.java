package com.itg.extjstest.domain;

import javax.persistence.*;
import javax.validation.constraints.Size;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
public class MaterialDocType {

    @Size(max = 4)
    private String docType_txt;

    public String getDocType_txt() {
        return this.docType_txt;
    }

    public void setDocType_txt(String docType_txt) {
        this.docType_txt = docType_txt;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Version
    @Column(name = "version")
    private Integer version;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getVersion() {
        return this.version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }


    public String toJson() {
        return new JSONSerializer().exclude("*.class").serialize(this);
    }

    public String toJson(String[] fields) {
        return new JSONSerializer().include(fields).exclude("*.class").serialize(this);
    }

    public static MaterialDocType fromJsonToMaterialDocType(String json) {
        return new JSONDeserializer<MaterialDocType>().use(null, MaterialDocType.class).deserialize(json);
    }

    public static String toJsonArray(Collection<MaterialDocType> collection) {
        return new JSONSerializer().exclude("*.class").serialize(collection);
    }

    public static String toJsonArray(Collection<MaterialDocType> collection, String[] fields) {
        return new JSONSerializer().include(fields).exclude("*.class").serialize(collection);
    }

    public static Collection<MaterialDocType> fromJsonArrayToMaterialDocTypes(String json) {
        return new JSONDeserializer<List<MaterialDocType>>().use(null, ArrayList.class).use("values", MaterialDocType.class).deserialize(json);
    }

}
