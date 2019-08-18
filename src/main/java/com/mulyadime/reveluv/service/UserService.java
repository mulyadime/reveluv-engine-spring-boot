package com.mulyadime.reveluv.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.mulyadime.reveluv.model.Role;
import com.mulyadime.reveluv.model.User;
import com.mulyadime.reveluv.repository.RoleRepository;
import com.mulyadime.reveluv.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserService extends ServiceLocator {
	
	@Autowired
	protected UserRepository userRepository;
	
	@Autowired
	protected RoleRepository roleRepository;
	
	@Autowired
	protected BCryptPasswordEncoder passwordEncoder;
	
	protected User user;
	
	protected HashMap<String, String> params = new HashMap<>();
	
	public void save(Object data) {
		user = (User) data;
		user.setActive(true);
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		
		Role role = roleRepository.findByRole("ADMIN");
		user.setRoles(new HashSet<Role>(Arrays.asList(role)));
		
		userRepository.insert(user);
		
		user = findUserByUserid(user.getUserid());
		roleRepository.saveUserRole(user, role);
		
	}

	public int findUserExists(String userid) {
		resetParameterValue(params);
		params.put(UserRepository.Field.USERID[0], userid);
		return userRepository.getCount(params);
		
	}

	

	public User findUserByUserid(String userid) {
		params.clear();
		params.put(UserRepository.Field.USERID[0], userid);
		User result = userRepository.findByWhereClause(params);
		
		return result;
	}

	public boolean isRequired(String value) {
		if (value.equalsIgnoreCase(""))
			return true;
		
		return false;
	}

}
