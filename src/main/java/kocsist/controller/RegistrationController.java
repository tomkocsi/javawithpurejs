package kocsist.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kocsist.DTOmodel.UserDTO;
import kocsist.model.UserInfo;
import kocsist.service.interfaces.UserService;

@RestController
public class RegistrationController {
	@Autowired
	private UserService userService;
	@Autowired
	private BCryptPasswordEncoder pwdEncoder;
	
	@PostMapping("/api/registrate")
	public ResponseEntity<UserDTO> getResponseMessage(@RequestParam(name = "useremail") String email,
													 @RequestParam(name = "password") String pwd){
		UserDTO userdto = new UserDTO(); 
		if(email != null && !"".equals(email) && pwd != null && !"".equals(pwd)) {
			UserInfo user = this.userService.findByEmail(email);
			if(user != null) {
				userdto.setEmail(email);
				userdto.setMessage("Ez az emailcím már regisztrált!");
			} else {
				user = new UserInfo();
				user.setEmail(email);
				user.setPassword(pwdEncoder.encode(pwd));
				this.userService.addUser(user);
				userdto.setMessage("OK");
			}
		} else {
			userdto.setMessage("Nem adtál meg adatokat!");
		}
		return new ResponseEntity<UserDTO>(userdto, HttpStatus.OK); 
	}
}
