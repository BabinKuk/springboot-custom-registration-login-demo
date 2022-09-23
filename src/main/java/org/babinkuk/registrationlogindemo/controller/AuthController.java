package org.babinkuk.registrationlogindemo.controller;

import java.util.List;

import javax.validation.Valid;

import org.babinkuk.registrationlogindemo.dao.UserDao;
import org.babinkuk.registrationlogindemo.entity.User;
import org.babinkuk.registrationlogindemo.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {
	
	private UserService userService;
	
	public AuthController(UserService userService) {
    	this.userService = userService;
    }
    
	// handler method to handle home page request
	@GetMapping({"/", "/index"})
	public String home() {
		
		return "index";
	}
	
	// handler method to handle login request
	@GetMapping("/login")
	public String login(){
		
		return "login";
	}
	
	// handler method to handle user registration form request
	@GetMapping("/register")
	public String showRegistrationForm(Model model) {
		
		// create model object to store form data
		UserDao user = new UserDao();
		model.addAttribute("user", user);
		return "register";
    }
	
	// handler method to handle user registration form submit request
	@PostMapping("/register/save")
	public String registration(@Valid @ModelAttribute("user") UserDao userDao, BindingResult result, Model model) {
		
		User existingUser = userService.findByUsername(userDao.getUsername());
        
		if (existingUser != null && existingUser.getUsername() != null && !existingUser.getUsername().isEmpty()) {
        	result.rejectValue("username", null, "There is already an account registered with the same username");
        }
        
        if (result.hasErrors()) {
			model.addAttribute("user", userDao);
			return "/register";
        }
        
		userService.saveUser(userDao);
		
		System.out.println("register success");
		
		//return "redirect:/register?success";
		return "registration-confirmation";
    }
	
	// handler method to handle list of users
	@GetMapping("/users")
	public String users(Model model){
		
		List<UserDao> users = userService.getUsers();
		model.addAttribute("users", users);
		return "users";
	}

}
