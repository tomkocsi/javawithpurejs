package kocsist.DTOmodel;

import kocsist.model.Description;

public class DescDTO {
	private long id;
	private String tag;
	private String text;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public void buildByDesc(Description d) {
		this.id = (long)d.getId();
		this.tag = (d.getTag() == null || "".equals(d.getTag())) ? "" : d.getTag();
		this.text = d.getText();
	}
}
