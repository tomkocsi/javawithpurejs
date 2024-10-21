package kocsist.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import kocsist.model.GraphData;
@Repository
public interface GraphDataRepo extends CrudRepository<GraphData, Integer> {
	// custom method
	List<GraphData> findByUserId(Integer userid);
	// custom method
	GraphData findByEntryNodeId(Integer nodeid);
	// custom method
	List<GraphData> findByName(String name);
	// custom method
	List<GraphData> findByNameLike(String graphnamelike);
	// custom method
	List<GraphData> findByNameContaining(String graphnamecontaining);
	// custom method - tested
	List<GraphData> findByNameLikeAndUserId(String graphnamelike, Integer userid);
	// custom method - tested
	List<GraphData> findByPublikus(boolean ispublic);
}
