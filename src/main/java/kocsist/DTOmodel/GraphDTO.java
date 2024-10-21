package kocsist.DTOmodel;

import java.util.List;

public class GraphDTO {
	private String graphDataName;
	private Integer graphDataId;
	private List<EdgeDTO> edges;
	private List<NodeDTO> nodes;
	private int nodeR;
	public int getNodeR() {
		return nodeR;
	}
	public void setNodeR(int nodeR) {
		this.nodeR = nodeR;
	}
	public String getGraphDataName() {
		return graphDataName;
	}
	public void setGraphDataName(String graphDataName) {
		this.graphDataName = graphDataName;
	}
	public Integer getGraphDataId() {
		return graphDataId;
	}
	public void setGraphDataId(Integer graphDataId) {
		this.graphDataId = graphDataId;
	}
	public List<EdgeDTO> getEdges() {
		return edges;
	}
	public void setEdges(List<EdgeDTO> edges) {
		this.edges = edges;
	}
	public List<NodeDTO> getNodes() {
		return nodes;
	}
	public void setNodes(List<NodeDTO> nodes) {
		this.nodes = nodes;
	}
}
