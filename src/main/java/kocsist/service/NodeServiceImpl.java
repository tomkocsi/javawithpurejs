package kocsist.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import kocsist.model.GraphData;
import kocsist.model.Node;
import kocsist.repository.GraphDataRepo;
import kocsist.repository.NodeRepo;
import kocsist.service.interfaces.NodeService;

@Component
public class NodeServiceImpl implements NodeService {
	@Autowired
	private NodeRepo nodeRepo;
	@Autowired
	private GraphDataRepo gdRepo;
	
	@Override
	public Node findById(Integer nodeid) {
		return this.nodeRepo.findById(nodeid).orElse(null);
	}

	@Override
	public Integer addNode(Node node) {
		Node mynode = nodeRepo.save(node);
		return mynode.getId();
	}

	@Override
	@Transactional
	public void deleteNodeById(Integer nodeid) {
		GraphData gd = gdRepo.findByEntryNodeId(nodeid);
		if(gd != null) {
			gdRepo.delete(gd);
		}
		nodeRepo.deleteById(nodeid);
	}

	@Override
	public void updateNode(Node node) {
		nodeRepo.save(node);
	}
	@Override
	public Iterable<Node> getAll(){
		return nodeRepo.findAll();
	}
}
