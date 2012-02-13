package com.itg.extjstest.domain;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import flexjson.transformer.DateTransformer;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import com.itg.extjstest.util.FilterItem;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
@RooJson
public class Message {

	@Size(max = 20)
	private String department;

	@Size(max = 4)
	@Pattern(regexp = "(供应|需求|敞口|锁定)")
	private String type;

	@Size(max = 50)
	private String article;

	private Double quantity;

	@Size(max = 40)
	private String departure;

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

	@Size(max = 150)
	private String eta;

	public static String mapToJson(
			HashMap<java.lang.String, java.lang.Object> map,
			List<com.itg.extjstest.domain.Message> messages) {
		map.put("messages", messages);
		String resultJson = new JSONSerializer()
				.exclude("*.class")
				.include("messages")
				.include("messages.specifications")
				.transform(new DateTransformer("yyyy-MM-dd HH:mm:ss"),
						Date.class).serialize(map);
		return resultJson;
	}

	public String validateObject(Locale locale) {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		Set<ConstraintViolation<Message>> violations = validator.validate(this);
		StringBuffer buf = new StringBuffer();
		if (violations.size() != 0) {

			ResourceBundle bundle = ResourceBundle.getBundle(
					"validateMessages", locale);
			for (ConstraintViolation<Message> violation : violations) {

				buf.append("-"
						+ bundle.getString("Message."
								+ violation.getPropertyPath().toString()));

				buf.append(violation.getMessage() + "<BR/>\n");
			}

		}
		return buf.toString();
	}
	
    public static Message fromJsonToMessage(String json) {
        return new JSONDeserializer<Message>().use(null, Message.class).deserialize(json);
    }

	public static List<Message> findMessagesByFilter(List<FilterItem> filters,
			Integer start, Integer page, Integer limit) {
		// TODO Auto-generated method stub
		CriteriaBuilder cb = entityManager().getCriteriaBuilder();
		CriteriaQuery<Message> c = cb.createQuery(Message.class);
		Root<Message> root = c.from(Message.class);
		
	
		HashMap<String, Path> paths = new HashMap<String, Path>();
		paths.put("", root);

		
		List<Predicate> criteria = new ArrayList<Predicate>();

		if (filters != null) {
			for (FilterItem f : filters) {
				criteria.add(f.getPredicate(cb, paths));
			}

			c.where(cb.and(criteria.toArray(new Predicate[0])));
		}

		return entityManager().createQuery(c).setFirstResult(start)
				.setMaxResults(limit).getResultList();

	}

}
