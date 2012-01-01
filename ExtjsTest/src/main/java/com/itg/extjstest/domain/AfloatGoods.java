package com.itg.extjstest.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
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
public class AfloatGoods {

    @ManyToOne
    private Contract contract;

    @Size(max = 50)
    private String plateNum;

    @Size(max = 50)
    private String dispatch;

    @Size(max = 50)
    private String destination;

    @DateTimeFormat(style = "M-")
    private Date transportDate;

    @DateTimeFormat(style = "M-")
    private Date dispatchDate;

    @DateTimeFormat(style = "M-")
    private Date eta;
    
	@Size(max = 500)
	private String remark;
    

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date arrivalDate;

    @OneToMany(cascade = CascadeType.ALL)
    private Set<AfloatGoodsItem> items = new HashSet<AfloatGoodsItem>();
}
