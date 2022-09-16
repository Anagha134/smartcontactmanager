package com.smart.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.smart.dao.UserRepository;
import com.smart.entities.User;

@Controller
@RequestMapping("/user") //created a user handler url pattern
public class UserController {
	
	@Autowired
	private UserRepository userRepository;
	
	
	@RequestMapping("/index")
	public String dashboard(Model model,Principal principal) {
		
		String userName= principal.getName();
		System.out.println("username "+userName);
		//get the user using username(email) from database
		
		User user = userRepository.getUserByUserName(userName);
		
		System.out.println("USER "+user);
		
		
		//send data to user_dashboard file
		model.addAttribute("user",user);
		
		return "normal/user_dashboard";
	}
}
