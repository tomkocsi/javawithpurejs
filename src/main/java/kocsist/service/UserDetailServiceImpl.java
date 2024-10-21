package kocsist.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import kocsist.model.UserInfo;
import kocsist.repository.UserRepo;
import kocsist.security.MyUserDetails;

@Service
public class UserDetailServiceImpl implements UserDetailsService{
	@Autowired
	private UserRepo userRepo;
		
	// forrás:
	// https://github.com/koushikkothagal/spring-security-jpa/blob/master/src/main/java/io/javabrains/springsecurityjpa/MyUserDetailsService.java
		
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<UserInfo> user = this.userRepo.findByEmail(username);
		user.orElseThrow(() -> new UsernameNotFoundException("Not found: " + username));
		return new MyUserDetails(user.get().getEmail(), user.get().getPassword());
	}
}
