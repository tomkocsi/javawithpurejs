package kocsist.DTOmodel;

import kocsist.model.InventoryElement;

public class InvElemDTO {
	private long id;
	private String text;
	public final long getId() {
		return id;
	}
	public final void setId(long id) {
		this.id = id;
	}
	public final String getText() {
		return text;
	}
	public final void setText(String text) {
		this.text = text;
	}
	public void buildByInvElem(InventoryElement ie) {
		if(ie != null) {
			this.id = ie.getId();
			this.text = ie.getText();
		}
	}
}
