package com.itg.extjstest.domain;

import com.itg.extjstest.util.ContractTypeObjectFactory;
import com.itg.extjstest.util.FilterItem;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import flexjson.transformer.DateTransformer;

import java.util.ArrayList;
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
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Fetch;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
@RooJson
public class Contract {

	@NotNull
	@Column(unique = true)
	@Size(max = 30)
	private String contractNo;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
	private Set<ContractItem> items = new HashSet<ContractItem>();

	@Enumerated
	private ContractType contractType;

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(style = "M-")
	private Date lastShippingDate;

	@Size(max = 50)
	private String supplier;

	@Size(max = 50)
	private String payTerm;

	@Size(max = 500)
	private String remark;

	public String toJson() {
		return new JSONSerializer()
				.exclude("*.class")
				.transform(new DateTransformer("yyyy-MM-dd HH:mm:ss"),
						Date.class).serialize(this);
	}

	public static String toJsonArray(
			Collection<com.itg.extjstest.domain.Contract> collection) {
		return new JSONSerializer()
				.exclude("*.class")
				.transform(new DateTransformer("yyyy-MM-dd HH:mm:ss"),
						Date.class).include("items").serialize(collection);
	}

	public static com.itg.extjstest.domain.Contract fromJsonToContract(
			String json) {
		return new JSONDeserializer<Contract>().use(null, Contract.class)
				.use(ContractType.class, new ContractTypeObjectFactory())
				.deserialize(json);
	}

	public static List<Contract> findContractsByFilter(
			List<FilterItem> filters, Integer start, Integer page, Integer limit) {
               
               
		CriteriaBuilder cb = entityManager().getCriteriaBuilder();
		CriteriaQuery<Contract> c = cb.createQuery(Contract.class);
		Root<Contract> rootContract = c.from(Contract.class);
		
		Join<Contract,ContractItem> j = rootContract.join("items");
		
		HashMap<String, Path> paths = new HashMap<String, Path>();
		paths.put("", rootContract);
		paths.put("items", j);
		
		List<Predicate> criteria = new ArrayList<Predicate>();

		if (filters != null) {
			for (FilterItem f : filters) {
				
				if(f.getField().equals("contractType")){
					f.setType("sList");
				}
				
				
				criteria.add(f.getPredicate(cb, paths));
			}

			c.where(cb.and(criteria.toArray(new Predicate[0])));
		}

		return entityManager().createQuery(c).setFirstResult(start)
				.setMaxResults(limit).getResultList();

	}

}
