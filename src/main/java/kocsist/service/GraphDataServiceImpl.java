package kocsist.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kocsist.model.GraphData;
import kocsist.repository.GraphDataRepo;
import kocsist.service.interfaces.GraphDataService;

@Component
public class GraphDataServiceImpl implements GraphDataService {
	@Autowired
	private GraphDataRepo graphDataRepo;
		
	@Override
	public GraphData findById(Integer graphdataid) {
		return this.graphDataRepo.findById(graphdataid).orElse(null);
	}

	@Override
	public List<GraphData> getAllByUserId(Integer userid) {
		return graphDataRepo.findByUserId(userid);
	}

	@Override
	public List<GraphData> findByNameLikeByUserId(String name, Integer userid) {
		List<GraphData> mylist = graphDataRepo.findByNameLike(name);
		if (mylist!=null) {
			return mylist
					.stream()
					.filter(gd -> 
					gd.getUser().getId().equals(userid) && !gd.isPublikus())
					.collect(Collectors.toList());
		}
		return null;
	}

	@Override
	public List<GraphData> findByNameContainingByUserId(String name, Integer userid) {
		List<GraphData> mylist = graphDataRepo.findByNameContaining(name);
		if (mylist!=null) {
			return mylist
					.stream()
					.filter(gd -> 
					gd.getUser().getId().equals(userid) && !gd.isPublikus())
					.collect(Collectors.toList());
		}
		return null;
	}

	@Override
	public List<GraphData> findByNameLikeAndPublic(String name) {
		List<GraphData> mylist = graphDataRepo.findByNameLike(name);
		if (mylist!=null) {
			return mylist
					.stream()
					.filter(gd -> gd.isPublikus())
					.collect(Collectors.toList());
		}
		return null;
	}
	
	@Override
	public GraphData findByNameByUserId(String name, Integer userid) {
		List<GraphData> mylist = graphDataRepo.findByName(name);
		if (mylist != null) {
			List<GraphData> filteredlist = mylist.stream().filter(gd -> 
					gd.getUser().getId().equals(userid))
					.collect(Collectors.toList());
			if(filteredlist != null && filteredlist.size() > 0) {
				return filteredlist.get(0);
			}
		}
		return null;
	}

	@Override
	public List<GraphData> findByNameAndPublic(String name) {
		List<GraphData> mylist = graphDataRepo.findByName(name);
		if (mylist!=null) {
			return mylist
					.stream()
					.filter(gd -> 
					gd.isPublikus())
					.collect(Collectors.toList());
		}
		return null;
	}
	
	@Override
	public List<GraphData> findByNameContainingAndPublic(String name) {
		List<GraphData> mylist = graphDataRepo.findByNameContaining(name);
		if (mylist!=null) {
			return mylist
					.stream()
					.filter(gd -> gd.isPublikus())
					.collect(Collectors.toList());
		}
		return null;
	}

	@Override
	public Integer addGraphData(GraphData graphdata) {
		GraphData gd = graphDataRepo.save(graphdata);
		return gd.getId();
	}

	@Override
	public void deleteGraphDataById(Integer id) {
		graphDataRepo.deleteById(id);
	}

	@Override
	public void updateGraphData(GraphData graphdata) {
		graphDataRepo.save(graphdata);
	}

	@Override
	public long count() {
		return graphDataRepo.count();
	}

	@Override
	public int countByUserId(Integer userid) {
		return graphDataRepo.findByUserId(userid).size();
	}
	
	@Override
	public List<GraphData> findAllPublic() {
		return graphDataRepo.findByPublikus(true);
	}

	@Override
	public Iterable<GraphData> getAll() {
		return graphDataRepo.findAll();
	}

	@Override
	public boolean hasPublicOrOwnWithThisName(String graphname, Integer userid) {
		ArrayList<String> publicorownnames = new ArrayList<String>();
		List<GraphData> publicgd = this.findAllPublic();
		if(publicgd != null && publicgd.size() > 0) {
			for(GraphData gd : publicgd) {
				String name = gd.getName();
				if(name != null && !"".equals(name)) {
					publicorownnames.add(name);
				}
			}
		}
		List<GraphData> usergd = this.getAllByUserId(userid);
		if(usergd != null && usergd.size() > 0) {
			for(GraphData gd : usergd) {
				String name = gd.getName();
				if(name != null && !"".equals(name)) {
					publicorownnames.add(name);
				}
			}
		}
		if(publicorownnames.size() > 0) {
			return publicorownnames.contains(graphname); 
		} else {
			return false;
		}
	}
}
