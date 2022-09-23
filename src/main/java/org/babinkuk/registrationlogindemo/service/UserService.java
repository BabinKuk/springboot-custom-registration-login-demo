package org.babinkuk.registrationlogindemo.service;

import java.util.List;

import org.babinkuk.registrationlogindemo.dao.UserDao;
import org.babinkuk.registrationlogindemo.entity.User;

public interface UserService {

	public User findByUsername(String username);

	public void saveUser(UserDao userDao);
	
	public List<UserDao> getUsers();
}
