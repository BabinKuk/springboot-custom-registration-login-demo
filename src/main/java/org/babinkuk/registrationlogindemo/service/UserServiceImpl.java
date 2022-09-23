package org.babinkuk.registrationlogindemo.service;

import org.babinkuk.registrationlogindemo.dao.UserDao;
import org.babinkuk.registrationlogindemo.entity.Role;
import org.babinkuk.registrationlogindemo.entity.User;
import org.babinkuk.registrationlogindemo.repository.RoleRepository;
import org.babinkuk.registrationlogindemo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

	// need to inject user dao
	@Autowired
	private UserRepository userRepository;
	@Autowired
    private RoleRepository roleRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public User findByUsername(String username) {
		// check the database if the user already exists
		return userRepository.findByUsername(username);
	}

	@Override
	@Transactional
	public void saveUser(UserDao userDao) {
		
		User user = new User();
		// assign user details to the user object
		user.setUsername(userDao.getUsername());
		user.setPassword(passwordEncoder.encode(userDao.getPassword()));
		user.setFirstName(userDao.getFirstName());
		user.setLastName(userDao.getLastName());
		user.setEmail(userDao.getEmail());
		
		// give user default role of "employee"
		Role role = roleRepository.findByName("ROLE_EMPLOYEE");
		if(role == null) {
			role = checkRoleExist();
		}
		
//		user.setRoles(Arrays.asList(roleDao.findRoleByName("ROLE_EMPLOYEE")));
		user.setRoles(Arrays.asList(role));
		
		// save user in the database
		userRepository.save(user);
	}
	
	@Override
	public List<UserDao> getUsers() {
		
		List<User> users = userRepository.findAll();
		
		return users.stream()
                .map((user) -> mapToUserDao(user))
                .collect(Collectors.toList());
	}
	
	private UserDao mapToUserDao(User user) {

		UserDao userDao = new UserDao();
		userDao.setFirstName(user.getFirstName());
		userDao.setLastName(user.getLastName());
		userDao.setEmail(user.getEmail());
		userDao.setPassword(user.getPassword());
		return userDao;
	}

	private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
		return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
	}
	
	private Role checkRoleExist() {
		Role role = new Role();
		role.setName("ROLE_EMPLOYEE");
		return roleRepository.save(role);
	}
}
