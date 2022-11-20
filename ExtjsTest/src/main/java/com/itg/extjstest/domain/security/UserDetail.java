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
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;


import com.itg.extjstest.domain.MaterialDoc;

import flexjson.JSONSerializer;
import flexjson.transformer.DateTransformer;
import com.itg.extjstest.domain.Menu;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.TypedQuery;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.ArrayList;
import java.util.Collection;



@RooJavaBean
@RooToString
@RooJpaActiveRecord(finders = { "findUserDetailsByUserNameEquals" })
@RooJson
@Entity
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

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Boolean getEnabled() {
		return this.enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public Set<SecurityRole> getRoles() {
		return this.roles;
	}

	public void setRoles(Set<SecurityRole> roles) {
		this.roles = roles;
	}

	public Integer getUserLevel() {
		return this.userLevel;
	}

	public void setUserLevel(Integer userLevel) {
		this.userLevel = userLevel;
	}

	public String getFullName() {
		return this.fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}


	public void setVersion(Integer version) {
		this.version = version;
	}
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
	public String toJson() {
		return new JSONSerializer().exclude("*.class").serialize(this);
	}

	public String toJson(String[] fields) {
		return new JSONSerializer().include(fields).exclude("*.class").serialize(this);
	}



	public static UserDetail fromJsonToUserDetail(String json) {
		return new JSONDeserializer<UserDetail>().use(null, UserDetail.class).deserialize(json);
	}

	public static String toJsonArray(Collection<UserDetail> collection) {
		return new JSONSerializer().exclude("*.class").serialize(collection);
	}

	public static String toJsonArray(Collection<UserDetail> collection, String[] fields) {
		return new JSONSerializer().include(fields).exclude("*.class").serialize(collection);
	}

	public static Collection<UserDetail> fromJsonArrayToUserDetails(String json) {
		return new JSONDeserializer<List<UserDetail>>().use(null, ArrayList.class).use("values", UserDetail.class).deserialize(json);
	}


}
