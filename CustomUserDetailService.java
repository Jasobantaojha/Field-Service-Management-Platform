package com.KeyStone.FieldServiceProject.Security;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.KeyStone.FieldServiceProject.Entity.UserAuth;
import com.KeyStone.FieldServiceProject.Repository.UserAuthRepository;

@Service

public class CustomUserDetailService implements UserDetailsService {
	
	 @Autowired
	    private UserAuthRepository userAuthRepo;

	    @Override
	    public UserDetails loadUserByUsername(String userEmail)
	            throws UsernameNotFoundException {

	        UserAuth user = userAuthRepo.findByUserEmail(userEmail)
	                .orElseThrow(() ->
	                    new UsernameNotFoundException("User not found: " + userEmail));

	        return User.builder()
	                .username(user.getUserEmail())
	                .password(user.getPassword())
	                .authorities(user.getRole().name())
	                .build();
	    }
	}


