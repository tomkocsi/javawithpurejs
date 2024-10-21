package kocsist.service.interfaces;

import java.util.List;

import org.springframework.stereotype.Service;

import kocsist.model.GraphData;
@Service
public interface GraphDataService {
	public GraphData findById(Integer graphdataid);
	public List<GraphData> getAllByUserId(Integer userid);
	public List<GraphData> findByNameLikeByUserId(String name, Integer userid);
	public GraphData findByNameByUserId(String name, Integer userid);
	public List<GraphData> findByNameAndPublic(String name);
	public List<GraphData> findByNameContainingByUserId(String name, Integer userid);
	public List<GraphData> findByNameLikeAndPublic(String name);
	public List<GraphData> findByNameContainingAndPublic(String name);
	public Integer addGraphData(GraphData graphdata);
	public void deleteGraphDataById(Integer id);
	public void updateGraphData(GraphData graphdata);
	public long count();
	public int countByUserId(Integer userid); 
	public List<GraphData> findAllPublic();
	public Iterable<GraphData> getAll();
	public boolean hasPublicOrOwnWithThisName(String graphname, Integer userid); 
}
