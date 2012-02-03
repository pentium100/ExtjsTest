package com.itg.extjstest.domain;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import flexjson.JSONSerializer;
import flexjson.transformer.DateTransformer;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
@RooJson
public class Message {

    @Size(max = 20)
    private String department;

    @Size(max = 4)
    @Pattern(regexp = "供应|�?��|敞口|库存")
    private String type;

    @Size(max = 50)
    private String article;

    private Double quantity;

    @Size(max = 40)
    private String departure;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date eta;

    @Size(max = 50)
    private String supplier;

    @Size(max = 50)
    private String owner;

    private Double costPrice;

    private Double suggestedPrice;

    private String remark;

    private Boolean isUrgent;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Specification> specifications = new HashSet<Specification>();

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date validBefore;

	public static String mapToJson(HashMap<String, Object> map,
			List<Message> messages) {
		map.put("messages", messages);
		String resultJson = new JSONSerializer()
				.exclude("*.class")
				.include("messages")
				.include("messages.specfications")
				.transform(new DateTransformer("yyyy-MM-dd HH:mm:ss"),Date.class)
				.serialize(map);

		return resultJson;
	}
}