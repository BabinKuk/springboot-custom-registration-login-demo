package org.babinkuk.registrationlogindemo.repository;

import org.babinkuk.registrationlogindemo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
	
	public User findByEmail(String email);
	
	public User findByUsername(String username);
}
