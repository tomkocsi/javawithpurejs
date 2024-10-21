package kocsist.blogic;

import kocsist.model.Picture;

public class PicWrapper implements ItemWrapper {
	private final Picture item;
	public PicWrapper(Picture p) {
		this.item = p;
	}
	@Override
	public String getText() {
		return Integer.toString(this.item.getSize());
	}

	@Override
	public String getEdgeLabel() {
		return this.item.getEdge().getLabel();
	}

}
