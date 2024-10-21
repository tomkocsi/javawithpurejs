package kocsist.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import kocsist.model.UserInfo;
@Repository
public interface UserRepo extends CrudRepository<UserInfo, Integer> {
	public Optional<UserInfo> findByEmail(String email);
}
