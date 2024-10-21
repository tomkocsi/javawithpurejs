package kocsist.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class Node {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator="native")
	@GenericGenerator(name = "native", strategy = "native")
	private Integer id;
	@Column(name="label", length=16)
	private String label;
	private boolean entry;
	private boolean finish;
	@Column(name="x")
	private int X;
	@Column(name="y")
	private int Y;
	
	//this makes relationship bidirectional
	@OneToMany(mappedBy = "fromNode", cascade = CascadeType.ALL) 
	private List<Edge> outgoingEdges = new ArrayList<>();
	
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
	public boolean isEntry() {
		return entry;
	}
	public void setEntry(boolean entry) {
		this.entry = entry;
	}
	public boolean isFinish() {
		return finish;
	}
	public void setFinish(boolean finish) {
		this.finish = finish;
	}
	public int getX() {
		return X;
	}
	public void setX(int x) {
		X = x;
	}
	public int getY() {
		return Y;
	}
	public void setY(int y) {
		Y = y;
	}
	public List<Edge> getOutgoingEdges() {
		return outgoingEdges;
	}
	public void setOutgoingEdges(List<Edge> outgoingEdges) {
		this.outgoingEdges = outgoingEdges;
	}
	
	public void addEdgeToOutgoingEdges(Edge edge) {
		if(edge != null) {
			if(!this.outgoingEdges.contains(edge)) {
				this.outgoingEdges.add(edge);
				//edge.setFromNode(this);
			} else
				System.out.println("\n  <->Edge (" + edge.getLabel() + ") was already added to node (" + this.getLabel() + ")");
		}
	}
	public void removeEdgeFromOutgoingEdges(Edge edge) {
		this.outgoingEdges.remove(edge);
		edge.setFromNode(null);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Node other = (Node) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	public String prettyPrint() {
		StringBuilder sb = new StringBuilder();
		sb.append("-------------").append("\n");
		sb.append(this.label).append(" (as this): ");
		if (this.outgoingEdges!=null) {
			for(Edge e : this.outgoingEdges) {
				sb.append("this -> ");
				sb.append(e.getToNode().getLabel());
				sb.append("; ");
			}
		}
		
		return sb.toString();
	}
}
