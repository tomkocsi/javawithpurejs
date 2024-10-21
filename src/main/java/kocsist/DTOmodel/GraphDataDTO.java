package kocsist.DTOmodel;

import kocsist.model.GraphData;

public class GraphDataDTO {
	private String name;
	private Integer id;
	private Integer nodeId;
	private boolean publikus;
	public final Integer getNodeId() {
		return nodeId;
	}
	public final void setNodeId(Integer nodeId) {
		this.nodeId = nodeId;
	}
	public String getName() {
		return name;
	}
	public void setName(String graphName) {
		this.name = graphName;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer graphId) {
		this.id = graphId;
	}
	public boolean isPublikus() {
		return publikus;
	}
	public void setPublikus(boolean publikus) {
		this.publikus = publikus;
	}
	public void buildByGraphData(GraphData gd) {
		this.id = gd.getId();
		this.name = gd.getName();
		if(gd.getEntryNode() != null) {
			this.nodeId = gd.getEntryNode().getId();
		}
		this.publikus = gd.isPublikus();
	}
}
