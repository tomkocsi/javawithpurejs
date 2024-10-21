package kocsist.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import kocsist.service.interfaces.UserService;

@Controller
public class PageController {
	@Autowired
	private UserService userService;
	
	@RequestMapping("/login")
	public String getLogin() {
		return "login";
	}
	
	@RequestMapping("/mylogout")
	public String getLogout() {
		return "mylogout";
	}
	
	// forrás:
	// https://dev.to/brunodrugowick/the-most-basic-security-for-spring-boot-with-thymeleaf-339h
	
	@RequestMapping("/app")
	public String getApp(Principal user, Model model) {
		if(user != null) {
			Integer userid;
			String useremail = user.getName();
			model.addAttribute("useremail", useremail);	
			if(this.userService.findByEmail(useremail) != null) {
				userid = this.userService.findByEmail(useremail).getId();
				model.addAttribute("userid", userid);	
			}
		}
		else {
			model.addAttribute("useremail", "vk6@hoho.hu");	
			model.addAttribute("hiddenuserid", 90);	
		}
		return "app";
	}
}