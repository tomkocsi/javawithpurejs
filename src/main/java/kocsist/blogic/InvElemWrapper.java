package kocsist.blogic;

import kocsist.model.InventoryElement;

public class InvElemWrapper implements ItemWrapper {
	private final InventoryElement ie;
	public InvElemWrapper(InventoryElement ie) {
		this.ie = ie;
	}
	@Override
	public String getText() {
		return this.ie.getText();
	}
	@Override
	public String getEdgeLabel() {
		return this.ie.getEdge().getLabel();
	}
}
