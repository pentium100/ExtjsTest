package com.itg.extjstest.util;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.itg.extjstest.domain.security.SecurityRole;
import com.itg.extjstest.domain.security.UserDetail;



@Service("assembler")
public class Assembler {
	
	 @Transactional(readOnly = true)
	  User buildUserFromUserEntity(UserDetail userDetail) {

	    String username = userDetail.getUserName();
	    String password = userDetail.getPassword();
	    boolean enabled = userDetail.getEnabled();
	    boolean accountNonExpired = userDetail.getEnabled();
	    boolean credentialsNonExpired = userDetail.getEnabled();
	    boolean accountNonLocked = userDetail.getEnabled();

	    Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
	    
	    for (SecurityRole role : userDetail.getRoles()) {
	      authorities.add(new SimpleGrantedAuthority(role.getRoleName()));
	    }

	    
		User user = new User(username, password, enabled,
	      accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
	    return user;
	  }

}
