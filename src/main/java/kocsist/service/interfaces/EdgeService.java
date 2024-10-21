package kocsist.service.interfaces;


import org.springframework.stereotype.Service;

import kocsist.model.Edge;
import kocsist.model.UserInfo;
@Service
public interface EdgeService {
	public Edge findById(Long edgeid);
	public Long addEdge(Edge edge);
	public void deleteEdgeById(Long edgeid);
	public void deleteEdgeById(Long edgeid, boolean persistcomponents);
	public void deleteEdgeById(Long edgeid, boolean persistcomponents, UserInfo user);
	public void updateEdge(Edge edge);
	public Iterable<Edge> getAll();
}
