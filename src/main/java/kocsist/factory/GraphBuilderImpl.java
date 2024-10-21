package kocsist.factory;

import java.util.ArrayList;
import java.util.Random;

import kocsist.DTOmodel.EdgeDTO;
import kocsist.DTOmodel.NodeDTO;
import kocsist.model.Edge;
import kocsist.model.GraphData;
import kocsist.model.Node;
import kocsist.model.UserInfo;
import kocsist.service.interfaces.EdgeService;
import kocsist.service.interfaces.GraphDataService;
import kocsist.service.interfaces.GraphService;
import kocsist.service.interfaces.NodeService;

abstract class GraphBuilderImpl implements GraphBuilder {
	private Node entryNode;
	private Node finishNode;
	protected ArrayList<Edge> edges = new ArrayList<Edge>();;
	private ArrayList<Node> nodes = new ArrayList<Node>();
	protected ArrayList<EdgeDTO> edgeDTOs = new ArrayList<EdgeDTO>();;
	private ArrayList<NodeDTO> nodeDTOs = new ArrayList<NodeDTO>();
	protected ArrayList<Position> positions;
	protected int boundaryWidth = 640;
	protected int boundaryHeight = 480;
	public static Random rand = new Random();
	protected String graphName;
	private int YOffset = -20;
	private EdgeService edgeService;
	private NodeService nodeService;
	private GraphDataService graphDataService;
	private GraphService graphService;
	private final String emptydesctext = "#üres";
	private int nodeNum;
	private UserInfo user;
	
	public int getNodeNum() {
		return this.nodeNum;
	}

	public UserInfo getUser() {
		return user;
	}

	protected final int getYOffset() {
		return YOffset;
	}

	protected final void setYOffset(int yoffset) {
		this.YOffset = yoffset;
	}

	public final int getBoundaryWidth() {
		return boundaryWidth;
	}

	public final void setBoundaryWidth(int boundaryWidth) {
		this.boundaryWidth = boundaryWidth;
	}

	public final int getBoundaryHeight() {
		return boundaryHeight;
	}

	public final void setBoundaryHeight(int boundaryHeight) {
		this.boundaryHeight = boundaryHeight;
	}

	public final Node getEntryNode() {
		return entryNode;
	}
	
	public final Node getFinishNode() {
		return finishNode;
	}

	public final ArrayList<Edge> getEdges() {
		return edges;
	}

	public final ArrayList<Node> getNodes() {
		return nodes;
	}

	public final ArrayList<EdgeDTO> getEdgeDTOs() {
		return edgeDTOs;
	}

	public final ArrayList<NodeDTO> getNodeDTOs() {
		return nodeDTOs;
	}

	public final void setGraphName(String graphName) {
		this.graphName = graphName;
	}
	
	public String getEmptydesctext() {
		return emptydesctext;
	}
	
	public EdgeService getEdgeService() {
		return edgeService;
	}

	public void setEdgeService(EdgeService edgeService) {
		this.edgeService = edgeService;
	}
	
	public GraphDataService getGraphDataService() {
		return graphDataService;
	}

	public void setGraphDataService(GraphDataService graphDataService) {
		this.graphDataService = graphDataService;
	}
	
	public GraphService getGraphService() {
		return graphService;
	}

	public void setGraphService(GraphService graphService) {
		this.graphService = graphService;
	}
	
	public GraphBuilderImpl(EdgeService es, NodeService ns, GraphDataService gds, 
			GraphService gs, int nodeNum, UserInfo u) {
		this.setEdgeService(es);
		this.nodeService = ns;
		this.setGraphDataService(gds);
		this.graphService = gs;
		this.nodeNum = nodeNum;
		this.user = u;
	}

	public abstract GraphData build(int node_R);
	protected abstract ArrayList<Position> populatePositions(int node_R);
	protected void populateNodeList(int node_R) {
		if(this.user != null) {
			if(this.nodeNum < 2) {
				this.nodeNum = 2;
			}
			if(graphName == null || "".equals(graphName)) {
				graphName = this.user.getEmail() + "_" + rand.nextInt(5000) + "plan";
			}
			this.positions = this.populatePositions(node_R);
			int k = 0;
			while(k < this.nodeNum) {
				Node n = new Node();
				n.setX(positions.get(k).getX());
				n.setY(positions.get(k).getY());
				n.setLabel(positions.get(k).getLabel());
				this.nodes.add(n);
				++k;
			}
			this.nodes.get(0).setEntry(true);
			this.nodes.get(k-1).setFinish(true);
			for (Node n : this.nodes) {
				this.nodeService.addNode(n);
			} 
		}
	}
	protected final void populateNodeDTOList() {
		for (Node n : this.nodes) {
			NodeDTO ndto = new NodeDTO();
			ndto.buildByNode(n);
			this.nodeDTOs.add(ndto);
		} 
	}
	protected final void populateEdgeDTOList() {
		for (Edge e : this.edges) {
			EdgeDTO edto = new EdgeDTO();
			edto.buildByEdge(e);
			this.edgeDTOs.add(edto);
		}
	}
}
