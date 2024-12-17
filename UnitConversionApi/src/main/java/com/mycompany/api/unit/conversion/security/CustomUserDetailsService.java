package com.mycompany.api.unit.conversion.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.mycompany.api.unit.conversion.repo.User;
import com.mycompany.api.unit.conversion.repo.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired UserRepository repo;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		User user = repo.findByUsername(username);
		
		if(user == null) throw new UsernameNotFoundException(" No User Found with username: " + username);
		
		return new CustomDetails(user);
	}

}
