package kocsist.DTOmodel;

import kocsist.model.Edge;
import kocsist.model.Node;

public class NodeDTO {
	private Integer id;
	private String label;
	private Integer X;
	private Integer Y;
	private boolean isFinish;
	private boolean isEntry;
	private int[] outgoingEdges;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public Integer getX() {
		return X;
	}
	public void setX(Integer x) {
		X = x;
	}
	public Integer getY() {
		return Y;
	}
	public void setY(Integer y) {
		Y = y;
	}
	public boolean isFinish() {
		return isFinish;
	}
	public void setFinish(boolean isFinish) {
		this.isFinish = isFinish;
	}
	public boolean isEntry() {
		return isEntry;
	}
	public void setEntry(boolean isEntry) {
		this.isEntry = isEntry;
	}
	
	public int[] getOutgoingEdges() {
		return outgoingEdges;
	}
	public void setOutgoingEdges(int[] outgoingEdges) {
		this.outgoingEdges = outgoingEdges;
	}
	public void buildByNode(Node n) {
		this.id = (int)n.getId();
		this.label = (n.getLabel() == null || "".equals(n.getLabel())) ? "" : n.getLabel();
		this.X = n.getX();
		this.Y = n.getY();
		this.isEntry = n.isEntry();
		this.isFinish = n.isFinish();
		
		if(n.getOutgoingEdges() != null) {
			int edgenum = n.getOutgoingEdges().size();
			if(edgenum > 0) {
				this.outgoingEdges = new int[edgenum];
				int k = 0;
				for(Edge e : n.getOutgoingEdges()) {
					this.outgoingEdges[k++] = e.getId().intValue();
				}
			}
		}
	}
}
