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
public class Specification {

    @Size(max = 50)
    private String specification;

    private Double typical;

    private Double reject;

    public String getSpecification() {
        return this.specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public Double getTypical() {
        return this.typical;
    }

    public void setTypical(Double typical) {
        this.typical = typical;
    }

    public Double getReject() {
        return this.reject;
    }

    public void setReject(Double reject) {
        this.reject = reject;
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

    public static Specification fromJsonToSpecification(String json) {
        return new JSONDeserializer<Specification>().use(null, Specification.class).deserialize(json);
    }

    public static String toJsonArray(Collection<Specification> collection) {
        return new JSONSerializer().exclude("*.class").serialize(collection);
    }

    public static String toJsonArray(Collection<Specification> collection, String[] fields) {
        return new JSONSerializer().include(fields).exclude("*.class").serialize(collection);
    }

    public static Collection<Specification> fromJsonArrayToSpecifications(String json) {
        return new JSONDeserializer<List<Specification>>().use(null, ArrayList.class).use("values", Specification.class).deserialize(json);
    }



}
