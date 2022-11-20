package com.itg.extjstest.domain;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import flexjson.transformer.DateTransformer;

import java.util.*;
import javax.persistence.*;
import javax.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.transaction.annotation.Transactional;

@Entity
public class OpenOrderMemo {

    @Size(max = 50)
    private String model;

    @Size(max = 5000)
    private String memo;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date updateTime;

    @Size(max = 50)
    private String updateUser;
    
    @Transient    
    private Date update_time;
    @Transient    
    private String update_user;

    public String getModel() {
        return this.model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getMemo() {
        return this.memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Date getUpdateTime() {
        return this.updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateUser() {
        return this.updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public Date getUpdate_time() {
        return this.update_time;
    }

    public void setUpdate_time(Date update_time) {
        this.update_time = update_time;
    }

    public String getUpdate_user() {
        return this.update_user;
    }

    public void setUpdate_user(String update_user) {
        this.update_user = update_user;
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

    public static OpenOrderMemo fromJsonToOpenOrderMemo(String json) {
        return new JSONDeserializer<OpenOrderMemo>().use(null, OpenOrderMemo.class).deserialize(json);
    }

    public static String toJsonArray(Collection<OpenOrderMemo> collection) {
        return new JSONSerializer().exclude("*.class").serialize(collection);
    }

    public static String toJsonArray(Collection<OpenOrderMemo> collection, String[] fields) {
        return new JSONSerializer().include(fields).exclude("*.class").serialize(collection);
    }

    public static Collection<OpenOrderMemo> fromJsonArrayToOpenOrderMemoes(String json) {
        return new JSONDeserializer<List<OpenOrderMemo>>().use(null, ArrayList.class).use("values", OpenOrderMemo.class).deserialize(json);
    }

    public  static String mapToJson(HashMap<String, Object> map, List<com.itg.extjstest.domain.OpenOrderMemo> openOrderMemos) {
        map.put("openOrders", openOrderMemos);
        String resultJson = new JSONSerializer().exclude("*.class").include("openOrders").transform(new DateTransformer("yyyy-MM-dd HH:mm:ss"), Date.class).serialize(map);
        return resultJson;
    }

}
