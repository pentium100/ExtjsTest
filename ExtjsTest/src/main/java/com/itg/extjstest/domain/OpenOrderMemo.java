package com.itg.extjstest.domain;

import flexjson.JSONSerializer;
import flexjson.transformer.DateTransformer;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJson
@RooJpaActiveRecord(finders = { "findOpenOrderMemoesByModelEquals" })
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

    public static String mapToJson(HashMap<java.lang.String, java.lang.Object> map, List<com.itg.extjstest.domain.OpenOrderMemo> openOrderMemos) {
        map.put("openOrders", openOrderMemos);
        String resultJson = new JSONSerializer().exclude("*.class").include("openOrders").transform(new DateTransformer("yyyy-MM-dd HH:mm:ss"), Date.class).serialize(map);
        return resultJson;
    }
}
