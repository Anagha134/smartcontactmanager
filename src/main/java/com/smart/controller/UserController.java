package com.smart.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.smart.dao.UserRepository;
import com.smart.entities.Contact;
import com.smart.entities.User;

@Controller
@RequestMapping("/user") //created a user handler url pattern
public class UserController {
	
	@Autowired
	private UserRepository userRepository;
	
	//method for adding common data to response
	@ModelAttribute
	public void  addcommonData(Model m,Principal principal) {
		String userName= principal.getName();
		System.out.println("username "+userName);
		//get the user using username(email) from database
		
		User user = userRepository.getUserByUserName(userName);
		
		System.out.println("USER "+user);
		
		
		//send data to user_dashboard file
		m.addAttribute("user",user);
	}
	
	
	//dashboard home
	@RequestMapping("/index")
	public String dashboard(Model model,Principal principal) {
		model.addAttribute("title", "User Dashboard");
		return "normal/user_dashboard";
	}
	
	//open add from handler
	@GetMapping("add-contact")
	public String openAddcontactform(Model model) {
		
		model.addAttribute("title", "Add Contact");
		model.addAttribute("contact",new Contact());
		
		return "normal/add_contact_form";
	}
	
	
	
}
