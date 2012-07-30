package com.itg.extjstest.domain;

import com.itg.extjstest.util.ContractTypeObjectFactory;
import com.itg.extjstest.util.FilterItem;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import flexjson.transformer.DateTransformer;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
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

    @DateTimeFormat(style = "M-")
    private Date arrivalDate;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<AfloatGoodsItem> items = new HashSet<AfloatGoodsItem>();

    private Boolean original;

    private Boolean sourceFee;

    private Boolean destinationFee;

    public static List<com.itg.extjstest.domain.AfloatGoods> findAfloatGoodsByFilter(List<com.itg.extjstest.util.FilterItem> filters, Integer start, Integer page, Integer limit) throws ParseException {
        CriteriaBuilder cb = entityManager().getCriteriaBuilder();
        CriteriaQuery<AfloatGoods> c = cb.createQuery(AfloatGoods.class);
        Root<AfloatGoods> afloatGoods = c.from(AfloatGoods.class);
        HashMap<String, Path> paths = new HashMap<String, Path>();
        paths.put("", afloatGoods);
        List<Predicate> criteria = new ArrayList<Predicate>();
        if (filters != null) {
            for (FilterItem f : filters) {
                criteria.add(f.getPredicate(cb, paths));
            }
            c.where(cb.and(criteria.toArray(new Predicate[0])));
        }
        return entityManager().createQuery(c).setFirstResult(start).setMaxResults(limit).getResultList();
    }

    public static String mapToJson(HashMap<java.lang.String, java.lang.Object> map, List<com.itg.extjstest.domain.AfloatGoods> result) {
        map.put("afloatGoods", result);
        String resultJson = new JSONSerializer().exclude("*.class").include("afloatGoods").include("afloatGoods.items").transform(new DateTransformer("yyyy-MM-dd HH:mm:ss"), Date.class).serialize(map);
        return resultJson;
    }

    public static com.itg.extjstest.domain.AfloatGoods fromJsonToAfloatGoods(String json) {
        return new JSONDeserializer<AfloatGoods>().use(null, AfloatGoods.class).use(ContractType.class, new ContractTypeObjectFactory()).deserialize(json);
    }
}
