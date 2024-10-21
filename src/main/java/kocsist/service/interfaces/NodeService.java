package kocsist.service.interfaces;


import org.springframework.stereotype.Service;

import kocsist.model.Node;
@Service
public interface NodeService {
	public Node findById(Integer nodeid);
	public Integer addNode(Node node);
	public void deleteNodeById(Integer nodeid);
	public void updateNode(Node node);
	public Iterable<Node> getAll();
}
