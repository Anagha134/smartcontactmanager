package com.smart.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.smart.dao.ContactRepository;
import com.smart.dao.UserRepository;
import com.smart.entities.Contact;
import com.smart.entities.User;
import com.smart.helper.Message;

@Controller
@RequestMapping("/user") //created a user handler url pattern
public class UserController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ContactRepository contactRepository;
	
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
    @GetMapping("/add-contact")
	public String openAddcontactform(Model model) {
		
		model.addAttribute("title", "Add Contact");
		model.addAttribute("contact",new Contact());
		
		return "normal/add_contact_form";
	}
	
	
	//processing add contact form 
	@PostMapping("/process-contact")
	//all the data will be store in contact obj when the feilds will be matched in form's and contact class variables
	public String processContact(@ModelAttribute Contact contact,@RequestParam("profileImage") MultipartFile file,Principal principal, HttpSession session) {
		
		//use to remove the details principal
		try {
		String name = principal.getName();
		User user = this.userRepository.getUserByUserName(name);
		
		//processing and uplaoding file
		
		if(file.isEmpty()) {
			//if file is empty then try our message
			System.out.println("File is empty");
			
			
			
		}
		else {
			//upload the file to folder and update filename to contact details 
			contact.setImage(file.getOriginalFilename());
			
			//to find the path of file
			File savefile = new ClassPathResource("static/img").getFile();
			
			Path path = Paths.get(savefile.getAbsolutePath()+File.separator+file.getOriginalFilename());
			
			//
			Files.copy(file.getInputStream(),path ,StandardCopyOption.REPLACE_EXISTING);
			System.out.println("Image is uploaded");
		}
		//contact ko user dena hai 
		contact.setUser(user);
		
		//adding the contact in user  list
		user.getContacts().add(contact);
		
		//value is save in database
		this.userRepository.save(user);
		
		
		System.out.println("DATA "+ contact);
		
		System.out.println("Added to data base ");
		
		
		//message success.........................
		session.setAttribute("message", new Message("Your Contact is added!!!","success"));
		
		
		
		}catch(Exception e) {
			System.out.println("ERROR "+e.getMessage());
			e.printStackTrace();
			
			//message error..................
			session.setAttribute("message", new Message("Something went Wrong, Try Again!!!","danger"));
			
			
			
		}
		
		return "normal/add_contact_form";
	}
	
	//show contacts handlers
	
	//per page =5 contacts  (n)
	//current page =0 (page)
	@GetMapping("/show-contacts/{page}")
	public String showContacts(@PathVariable ("page")Integer page ,Model m,Principal principal) {
		//want to display the list of contacts from database
		m.addAttribute("title","Show User Contacts");
		//first find the which user is login with help of principal
		String userName = principal.getName();
	
		User user = this.userRepository.getUserByUserName(userName);
		
		//pageable is parent class of Page Request.
		Pageable pageable = PageRequest.of(page,8);
		//and then display all contacts of that user's from database
		Page<Contact> contacts = this.contactRepository.findContactsByUser(user.getId(),pageable);
		
		//display all contacts
		m.addAttribute("contacts",contacts);
		//current pages
	    m.addAttribute("currentpage",page);
	    //get total pages 
		m.addAttribute("totalPages",contacts.getTotalPages());
		
		return "normal/show_contacts";
	}
	
	
}
