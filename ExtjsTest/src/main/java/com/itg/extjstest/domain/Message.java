package com.itg.extjstest.domain;

import java.util.Date;
import java.util.HashSet;
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

@RooJavaBean
@RooToString
@RooJpaActiveRecord
@RooJson
public class Message {

    @Size(max = 20)
    private String department;

    @Size(max = 4)
    @Pattern(regexp = "供应|需求|敞口|库存")
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

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date validDate;

    private Boolean isUrgent;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Specification> specifications = new HashSet<Specification>();
}
