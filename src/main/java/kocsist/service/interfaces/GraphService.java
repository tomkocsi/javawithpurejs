package kocsist.service.interfaces;

import java.util.ArrayList;

import org.springframework.stereotype.Service;

import kocsist.model.Description;
import kocsist.model.Edge;
import kocsist.model.GraphData;
import kocsist.model.InventoryElement;
import kocsist.model.Node;
import kocsist.model.Picture;
import kocsist.model.UserInfo;
@Service
public interface GraphService{
	public ArrayList<Node> getAllNodesByEntryNodeId(Integer nodeid);
	public ArrayList<Node> getAllNodesByEntryNode(Node entrynode);
	public ArrayList<Edge> getAllEdgesByEntryNodeId(Integer nodeid);
	public ArrayList<Edge> getAllEdgesByEntryNode(Node entrynode);
	public ArrayList<Picture> getAllPicturesByEntryNode(Node entrynode);
	public ArrayList<Description> getAllDescriptionsByEntryNode(Node entrynode);
	public ArrayList<InventoryElement> getAllInventoryElementsByEntryNode(Node entrynode);
	public GraphData cloneGraph(GraphData graphdata, Integer newuserid);
	public GraphData createGraph(UserInfo user);
	public void updateEdgesAndNodes(Node entrynode);
	public boolean isSame(Edge e1, Edge e2);
	public void addEdgeToNodeWithSave(Edge e, Node from);
	public void deleteEdgesAndNodesByEntryNode(Node entrynode);
	public ArrayList<Description> getAllDescriptionsPublic();
	public ArrayList<Picture> getAllPicturesPublic();
	public ArrayList<InventoryElement> getAllInventoryPublic();
	public void deleteGraphWithAllComponents(Node entrynode);
	public String getGraphAsText(GraphData graphdata);
	public void removeNodeRefFromEdges(Integer nodeid, Integer graphid);
	public void detachOrDeleteInvElem(Long invelemid, Long edgeid, UserInfo user);
	public void detachOrDeleteInventory(Long edgeid, UserInfo user);
	public void detachOrDeleteDescription(Long edgeid, UserInfo user);
	public void detachPicture(Long picid, Long edgeid, UserInfo user);
	public void detachPictureList(Long edgeid, UserInfo user);
	public void attachInvElem(Long invelemid, Long edgeid);
	public void attachDescription(Long descid, Long edgeid, UserInfo user);
	public void attachPicture(Long picid, Long edgeid);
	public void detachAllComponents(Long edgeid, UserInfo user);
	public Node getEntryNodeOfEdge(Edge e); 
}
