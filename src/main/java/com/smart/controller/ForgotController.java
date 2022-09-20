package com.smart.controller;

import java.util.Random;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.smart.dao.UserRepository;
import com.smart.entities.User;
import com.smart.helper.Message;
import com.smart.service.EmailService;

@Controller
public class ForgotController {

	//generate otp of 4 digit
	Random random = new Random(1000);
	@Autowired
	private EmailService emailService;
	
	
	@Autowired
	private UserRepository userRepository;
	
	
    //email id form open handler
	@RequestMapping("/forgot")
	public String openEmailform() {
		return "forgot_email_form";
	}
	
	
	@PostMapping("/send-otp")
	public String sendOTP(@RequestParam("email")String email,HttpSession session) {
		
		System.out.println("Email"+email);
		
		
		int otp = random.nextInt(999999);
		
		System.out.println("OTP"+otp);
		
		//code to send otp to email...
		
		String subject="OTP from SCM";
		String message=""
				+"<div style='border:1px solid #e2e2e2; padding:20px'>"
				+"<h1>"
				+"OTP is"
				+"<b>"+otp
				+"</b>"
				+"</h1>"
				+"</div>";
		
		String to=email;
		
		boolean flag = this.emailService.sendEmail(subject, message, to);
		
		if(flag) {
			session.setAttribute("myotp", otp);
			session.setAttribute("email", email);
			return "verify_otp";
			
		}else {
			
			session.setAttribute("message","check your email id !!");
			return "forgot_email_form";
		}
		
		
	}
	
	//verify otp
	@PostMapping("/verify-otp")
	public String verifyOtp(@RequestParam("otp")int otp,HttpSession session) {
		
		
	    int myotp = (int)session.getAttribute("myotp");
	    String email=(String)session.getAttribute("email");
	    
	    if(myotp == otp) {
	    	//password change form
	    	
	    	User user = this.userRepository.getUserByUserName(email);
	    	if(user == null) {
	    		//send error message 
	    		session.setAttribute("message","User does not exits With this Email !!");
				return "forgot_email_form";
	    	    	
	    		
	    	}else {
	    		//send change password form
	    	}
	    	
	    	
	    	return "password_change_form";
	    }else {
	    	session.setAttribute("message", "You have Entered Wrong OTP!!!");
	    	return "verify_otp";
	    }
	    
	    
		
	}
}


