package org.babinkuk.registrationlogindemo.repository;

import org.babinkuk.registrationlogindemo.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {
	
	public Role findByName(String name);
}
