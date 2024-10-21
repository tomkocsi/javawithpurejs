package kocsist.service.interfaces;

import org.springframework.stereotype.Service;

import kocsist.model.UserInfo;
@Service
public interface UserService {
	public UserInfo findByEmail(String email);
	public UserInfo findById(Integer userid);
	public Integer addUser(UserInfo user);
	public void deleteUserById(Integer userid);
	public void updateUserById(Integer userid);
	public void updateUser(UserInfo user);
	public long count();
	public Iterable<UserInfo> getAll(); 
}
