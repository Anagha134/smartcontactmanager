package com.smart.controller;

import java.util.Random;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.smart.helper.Message;
import com.smart.service.EmailService;

@Controller
public class ForgotController {

	//generate otp of 4 digit
	Random random = new Random(1000);
	@Autowired
	private EmailService emailService;
	
	
	
	
	
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
		String message="OTP ="+otp+""; 
		
		String to=email;
		
		boolean flag = this.emailService.sendEmail(subject, message, to);
		
		if(flag) {
			session.setAttribute("otp", otp);
			return "verify_otp";
			
		}else {
			
			session.setAttribute("message","check your email id !!");
			return "forgot_email_form";
		}
		
		
	}
}


