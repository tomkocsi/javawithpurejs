package kocsist.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kocsist.DTOmodel.UserDTO;
import kocsist.model.UserInfo;
import kocsist.service.interfaces.UserService;

@RestController
public class UserController {
	@Autowired
	private UserService userService;
	@Autowired
	private BCryptPasswordEncoder pwdEncoder;
	
	@PostMapping(value = "/userdata")
	public ResponseEntity<UserDTO> getUserData(@RequestParam("username") String uemail, @RequestParam("password") String pwd) {
		if(uemail != null && !"".equals(uemail) && pwd !=null && !"".equals(pwd)) {
			UserInfo user = this.userService.findByEmail(uemail);
			if(user != null ) {
				if(this.pwdEncoder.encode(pwd).equals(user.getPassword())) {
					UserDTO userdto = new UserDTO();
					userdto.setEmail(uemail);
					userdto.setId(user.getId());
					userdto.setMessage("OK");
					return new ResponseEntity<UserDTO>(userdto, HttpStatus.OK);
				} else {
					UserDTO userdto = new UserDTO();
					userdto.setEmail(uemail);
					userdto.setMessage("Helytelen vagy hiányzó emailcím vagy jelszó");
					return new ResponseEntity<UserDTO>(userdto, HttpStatus.OK);
				}
			} else {
				UserDTO userdto = new UserDTO();
				userdto.setEmail(uemail);
				userdto.setMessage("NEW");
				
				UserInfo newuser = new UserInfo();
				newuser.setEmail(uemail);
				newuser.setPassword(this.pwdEncoder.encode(pwd));
				
				userdto.setId(this.userService.addUser(newuser));
				
				return new ResponseEntity<UserDTO>(userdto, HttpStatus.OK);
			}
		} else {
			UserDTO userdto = new UserDTO();
			userdto.setMessage("Helytelen vagy hiányzó emailcím vagy jelszó");
			return new ResponseEntity<UserDTO>(userdto, HttpStatus.OK);
		}
	}
}
