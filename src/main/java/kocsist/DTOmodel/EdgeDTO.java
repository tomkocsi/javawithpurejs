package kocsist.DTOmodel;

import kocsist.model.Description;
import kocsist.model.Edge;
import kocsist.model.InventoryElement;
import kocsist.model.Picture;

public class EdgeDTO {
	private Long id;
	private String label;
	private Integer fromNodeId;
	private Integer toNodeId;
	private int time1;
	private int time2;
	private Long descriptionId;
	private int[] pictureIds;
	private int[] inventoryElementIds;
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
	public Integer getFromNodeId() {
		return fromNodeId;
	}
	public void setFromNodeId(Integer fromNodeId) {
		this.fromNodeId = fromNodeId;
	}
	public Integer getToNodeId() {
		return toNodeId;
	}
	public void setToNodeId(Integer toNodeId) {
		this.toNodeId = toNodeId;
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
	public Long getDescriptionId() {
		return descriptionId;
	}
	public void setDescriptionId(Long descriptionId) {
		this.descriptionId = descriptionId;
	}
	public int[] getPictureIds() {
		return pictureIds;
	}
	public void setPictureIds(int[] pictureIds) {
		this.pictureIds = pictureIds;
	}
	public int[] getInventoryElementIds() {
		return inventoryElementIds;
	}
	public void setInventoryElementIds(int[] inventoryElementIds) {
		this.inventoryElementIds = inventoryElementIds;
	}
	public void buildByEdge(Edge e) {
		this.id = e.getId();
		this.label = e.getLabel();
		this.time1 = e.getTime1();
		this.time2 = e.getTime2();
		Description d = e.getDesc();
		if(d != null) {
			this.descriptionId = e.getDesc().getId();
		}
		if(e.getInventory() != null && e.getInventory().size() > 0) {
			int s = e.getInventory().size();
			this.inventoryElementIds = new int[s];
			int k = 0;
			for(InventoryElement ie : e.getInventory()) {
				this.inventoryElementIds[k++] = ie.getId().intValue();
			}
		}
		if(e.getPictures() != null && e.getPictures().size() > 0) {
			int s = e.getPictures().size();
			this.pictureIds = new int[s];
			int k = 0;
			for(Picture p : e.getPictures()) {
				this.pictureIds[k++] = p.getId().intValue();
			}
		}
		this.fromNodeId = e.getFromNode().getId();
		this.toNodeId = e.getToNode().getId();
	}
}
