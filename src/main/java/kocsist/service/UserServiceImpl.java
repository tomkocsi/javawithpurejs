package kocsist.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kocsist.model.UserInfo;
import kocsist.repository.UserRepo;
import kocsist.service.interfaces.UserService;

@Component
public class UserServiceImpl implements UserService {
	@Autowired
	private UserRepo userRepo;
	@Override
	public UserInfo findByEmail(String email) {
		return this.userRepo.findByEmail(email).orElse(null);
	}

	@Override
	public UserInfo findById(Integer userid) {
		return this.userRepo.findById(userid).orElse(null);
	}

	@Override
	public Integer addUser(UserInfo user) {
		UserInfo u = userRepo.save(user);
		return u.getId();
	}

	@Override
	public void deleteUserById(Integer userid) {
		userRepo.deleteById(userid);
	}

	@Override
	public void updateUserById(Integer userid) {
		UserInfo user = userRepo.findById(userid).get();
		userRepo.save(user);
	}

	@Override
	public void updateUser(UserInfo user) {
		userRepo.save(user);
	}

	@Override
	public long count() {
		return userRepo.count();
	}

	@Override
	public Iterable<UserInfo> getAll() {
		return userRepo.findAll();
	}

}
