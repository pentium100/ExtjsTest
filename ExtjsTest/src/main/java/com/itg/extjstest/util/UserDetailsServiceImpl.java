package com.itg.extjstest.util;

import com.itg.extjstest.repository.UserDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.itg.extjstest.domain.security.UserDetail;

@Service("userDetailsService") 
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private Assembler assembler;

	@Autowired
	private UserDetailRepository userDetailRepository;

	@Override
	public UserDetails loadUserByUsername(String userName)
			throws UsernameNotFoundException {

		UserDetail userDetail = userDetailRepository.findUserDetailsByUserNameEquals(
				userName).getSingleResult();

		if (userDetail == null)
			throw new UsernameNotFoundException("user not found");

		
        //Md5PasswordEncoder md5 = new Md5PasswordEncoder();  
        //String md5Password = md5.encodePassword("123", null);
        //userDetail.setPassword(md5Password);

		return assembler.buildUserFromUserEntity(userDetail);

		
	}

}
