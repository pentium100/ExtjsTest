package com.itg.extjstest.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.itg.extjstest.domain.security.UserDetail;

@Service("userDetailsService") 
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private Assembler assembler;

	@Override
	public UserDetails loadUserByUsername(String userName)
			throws UsernameNotFoundException {

		UserDetail userDetail = UserDetail.findUserDetailsByUserNameEquals(
				userName).getSingleResult();

		if (userDetail == null)
			throw new UsernameNotFoundException("user not found");

		return assembler.buildUserFromUserEntity(userDetail);

		
	}

}
