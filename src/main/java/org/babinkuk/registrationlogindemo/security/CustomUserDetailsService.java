package org.babinkuk.registrationlogindemo.security;

import java.util.stream.Collectors;

import org.babinkuk.registrationlogindemo.entity.User;
import org.babinkuk.registrationlogindemo.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomUserDetailsService implements UserDetailsService {
	
	private UserRepository userRepository;

	public CustomUserDetailsService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		User user = userRepository.findByUsername(username);
		
		if (user == null) {
			throw new UsernameNotFoundException("Invalid username or password.");
		}

		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
				user.getRoles().stream()
						.map((role) -> new SimpleGrantedAuthority(role.getName()))
						.collect(Collectors.toList()));
	}

}
