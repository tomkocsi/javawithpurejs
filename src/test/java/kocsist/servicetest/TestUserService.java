package kocsist.servicetest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.Random;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import kocsist.model.UserInfo;
import kocsist.service.interfaces.UserService;

@DataJpaTest
public class TestUserService {
	@Autowired
	private UserService userService;
	
	private long usercount;
	private Random myrandom = new Random();
	private UserInfo randomuser; 
	
	@BeforeEach
	@Rollback(false)
	public void setUp() {
		int krand = 2001 + myrandom.nextInt(8000);
		for (int k = 0; k < 8; k++) {
			UserInfo u = new UserInfo();
			krand = 2001 + myrandom.nextInt(8000);
			u.setEmail("test" + krand + "test.hu");
			u.setName("test" + krand );
			u.setPassword("testpwd");
			this.userService.addUser(u);
			if (k == 5) {
				this.randomuser = u;
			}
		}
		this.usercount = userService.count();
	}
	
	@Test
	@Rollback(false)
	public void testSaveUser() {
		Integer userid;
		String username = "ServiceTester";  
		this.usercount = userService.count();
		
		String uniqeEmail = String.format("test%d0%d@test.hu", usercount, this.myrandom.nextInt(2000));
		UserInfo u = new UserInfo();
		u.setEmail(uniqeEmail);
		u.setName(username);
		u.setPassword("testpwd");
		userid = userService.addUser(u);
		assertEquals(userService.findById(userid).getEmail(), uniqeEmail);
	}
	
	@Test
	public void testDeleteUserById() {
		long countbefore = this.userService.count();
		System.out.println(" *************  testDeleteUserById()   ***************");
		System.out.println("  " + this.randomuser + " deleted.");
		this.userService.deleteUserById(this.randomuser.getId());
		long countafter = this.userService.count();
		assertEquals(countafter, countbefore - 1);
	}
	
}
