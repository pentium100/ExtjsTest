package com.itg.extjstest.domain;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;
import com.itg.extjstest.domain.ContractType;
import javax.persistence.Enumerated;
import com.itg.extjstest.domain.Supplier;
import javax.persistence.ManyToOne;
import java.util.Date;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.Set;
import com.itg.extjstest.domain.ContractItem;
import java.util.HashSet;
import javax.persistence.ManyToMany;
import javax.persistence.CascadeType;

@RooJavaBean
@RooToString
@RooEntity
public class Contract {

    @Enumerated
    private ContractType contractType;

    @ManyToOne
    private Supplier supplier;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date eta;

    @ManyToMany(cascade = CascadeType.ALL)
    private Set<ContractItem> items = new HashSet<ContractItem>();
}
