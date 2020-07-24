package com.example.gestionRH.service.imp;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.gestionRH.entites.Employe;
import com.example.gestionRH.repository.EmployeRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private EmployeRepository appUserDAO;

//    @Autowired
//    private AppRoleDAO appRoleDAO;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Employe appUser = this.appUserDAO.findByUsername(username).get();

		if (appUser == null) {
			throw new UsernameNotFoundException("User " + username + " was not found in the database");
		}

		// [ROLE_USER, ROLE_ADMIN,..]
		String roleNames = appUser.getRole().getName();

		List<GrantedAuthority> grantList = new ArrayList<GrantedAuthority>();
		if (roleNames != null) {
			GrantedAuthority authority = new SimpleGrantedAuthority(roleNames);
			grantList.add(authority);
		}

		UserDetails userDetails = (UserDetails) new User(appUser.getUsername(), //
				appUser.getPassword(), grantList);

		return userDetails;
	}

}