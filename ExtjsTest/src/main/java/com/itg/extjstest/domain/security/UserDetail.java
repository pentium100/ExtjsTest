package com.itg.extjstest.domain.security;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import com.itg.extjstest.domain.MaterialDoc;

import flexjson.JSONSerializer;
import flexjson.transformer.DateTransformer;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(finders = { "findUserDetailsByUserNameEquals" })
@RooJson
public class UserDetail implements Serializable {

	private static final long serialVersionUID = -1679307029304236940L;

	private String userName;

	@NotNull
	@Size(max = 45)
	private String password;

	private Boolean enabled;

	@ManyToMany(cascade = CascadeType.ALL)
	private Set<SecurityRole> roles = new HashSet<SecurityRole>();

	private Integer userLevel;

	@Size(max = 50)
	private String fullName;

	public static String mapToJson(HashMap<String, Object> map,
			List<UserDetail> result) {
		map.put("userDetails", result);
		String resultJson = new JSONSerializer()
				.exclude("*.class")
				.include("userDetails")
				.include("userDetails.roles")
				.transform(new DateTransformer("yyyy-MM-dd HH:mm:ss"),
						Date.class).serialize(map);

		return resultJson;

	}

}
