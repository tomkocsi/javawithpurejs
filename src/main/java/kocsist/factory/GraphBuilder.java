package kocsist.factory;

import java.util.ArrayList;

import kocsist.DTOmodel.EdgeDTO;
import kocsist.DTOmodel.NodeDTO;
import kocsist.model.GraphData;
import kocsist.model.Node;

public interface GraphBuilder {
	public GraphData build(int node_R);
	public int getBoundaryHeight();
	public int getBoundaryWidth();
	public Node getEntryNode();
	public ArrayList<NodeDTO> getNodeDTOs();
	public ArrayList<EdgeDTO> getEdgeDTOs();
	
}
