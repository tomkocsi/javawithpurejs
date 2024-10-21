package kocsist.blogic;

import kocsist.model.Description;

public class DescWrapper implements ItemWrapper {
	private final Description item;
	public DescWrapper(Description d) {
		this.item = d;
	}
	@Override
	public String getText() {
		return this.item.getText();
	}
	@Override
	public String getEdgeLabel() {
		return this.item.getEdge().getLabel();
	}
}
