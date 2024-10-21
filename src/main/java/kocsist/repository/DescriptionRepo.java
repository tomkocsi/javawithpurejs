package kocsist.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import kocsist.model.Description;
@Repository
public interface DescriptionRepo extends CrudRepository<Description, Long> {
	// custom query
	public List<Description> findByUserId(Integer userid);
	public List<Description> findByUserIdAndEdgeIdIsNull(Integer userid);
}
