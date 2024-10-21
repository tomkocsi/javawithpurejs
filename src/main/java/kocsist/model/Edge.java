package kocsist.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class Edge {
	//https://vladmihalcea.com/why-should-not-use-the-auto-jpa-generationtype-with-mysql-and-hibernate/
	//letoltve : 2021.11.06
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator="native")
	@GenericGenerator(name = "native", strategy = "native")
	private Long id;
	@Column(name="label", length=64)
	private String label;
	private int time1;
	private int time2;
	@ManyToOne(fetch = FetchType.LAZY)
	private Node fromNode;
	@ManyToOne(fetch = FetchType.LAZY)
	private Node toNode;
	
	// unidirectional relationship - not efficient
	// https://vladmihalcea.com/the-best-way-to-map-a-onetomany-association-with-jpa-and-hibernate/
	// @ManyToMany
	// @JoinTable(name = "edge_inventory", joinColumns = @JoinColumn(name = "edge_id"), inverseJoinColumns =
	// @JoinColumn(name = "inventory_id"))
	
	// bidirectional - efficient
	@OneToMany(mappedBy = "edge", cascade = CascadeType.ALL)
	private List<InventoryElement> inventory = new ArrayList<>();
	@OneToMany(mappedBy = "edge", cascade = CascadeType.ALL)
	private List<Picture> pictures = new ArrayList<>();
	//@OneToOne(mappedBy="edge")
	@OneToOne(mappedBy="edge", cascade = CascadeType.ALL)
	private Description desc;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public int getTime1() {
		return time1;
	}
	public void setTime1(int time1) {
		this.time1 = time1;
	}
	public int getTime2() {
		return time2;
	}
	public void setTime2(int time2) {
		this.time2 = time2;
	}
	public Node getFromNode() {
		return fromNode;
	}
	public void setFromNode(Node fromNode) {
		if(fromNode != null) {
			this.fromNode = fromNode;
			this.fromNode.addEdgeToOutgoingEdges(this);
		}
	}
	public Node getToNode() {
		return toNode;
	}
	public void setToNode(Node toNode) {
		this.toNode = toNode;
	}
	public List<InventoryElement> getInventory() {
		return inventory;
	}
	public void setInventory(List<InventoryElement> inventory) {
		this.inventory = inventory;
	}
	public List<Picture> getPictures() {
		return pictures;
	}
	public void setPictures(List<Picture> pictures) {
		this.pictures = pictures;
	}
	public Description getDesc() {
		return desc;
	}
	public void setDesc(Description desc) {
		this.desc = desc;
		if(desc != null) {
			desc.setEdge(this);
		}
	}
	public void addPicture(Picture pic) {
		this.pictures.add(pic);
		pic.setEdge(this);
	}
	public void removePicture(Picture pic) {
		this.pictures.remove(pic);
		pic.setEdge(null);
	}
	public void addInventoryElement(InventoryElement ie) {
		this.inventory.add(ie);
		ie.setEdge(this);
	}
	public void removeInventoryElement(InventoryElement ie) {
		this.inventory.remove(ie);
		ie.setEdge(null);
	}
	public void removeAllInventoryElement(Collection<InventoryElement> list) {
		this.inventory.removeAll(list);
	}
	public void removeAllPictures(Collection<Picture> list) {
		this.pictures.removeAll(list);
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
		Edge other = (Edge) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	public String printMe() {
		StringBuilder sb = new StringBuilder();
		sb.append("id: " + this.getId());
		sb.append(" label: " + this.getLabel());
		sb.append("\nfrom: " + this.getFromNode().getLabel());
		sb.append(" to: " + this.getToNode().getLabel());
		sb.append("\ntime1: " + this.getTime1() + " time2: " + this.getTime2());
		sb.append("\ndesc: "+ this.getDesc().getText());
		sb.append("\ninventory: ");
		if(this.getInventory().size() > 1) {
			for(InventoryElement ie : this.getInventory()) {
				sb.append((ie.getText() + "[" + ie.getId() + "]; "));
			}
		}
		sb.append("\npictures: ");
		if(this.getPictures().size() > 1) {
			for(Picture pic : this.getPictures()) {
				sb.append((pic.getPathOnServer() + "[" + pic.getId() + "]; "));
			}
		}
				return sb.toString();
	}
}
