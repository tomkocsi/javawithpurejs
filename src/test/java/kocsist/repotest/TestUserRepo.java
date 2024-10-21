package kocsist.repotest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import kocsist.model.UserInfo;
import kocsist.repository.UserRepo;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class TestUserRepo {
	@Autowired
	private UserRepo ur;
	
	private long usercount;
	private static int serialnum; 
	
	@BeforeEach
	//@Rollback(false)
	public void setUp() {
		usercount = ur.count();
		serialnum = (int)usercount + 1;
	}
	@Test
	//@Rollback(false)
	public void testCountUser() {
		UserInfo u = new UserInfo();
		u.setName("vki"+serialnum++);
		u.setEmail("vk"+serialnum+"@hoho.hu");
		u.setPassword("pwd"+serialnum);
		ur.save(u);
		assertEquals(ur.count(), this.usercount + 1);
	}
}
