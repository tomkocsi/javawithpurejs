package kocsist.DTOmodel;

public class ParamDTO {
	private String useremail;
	private Long entityid;
	private Long componentid;
	private String textparam;
	public String getUseremail() {
		return useremail;
	}
	public void setUseremail(String useremail) {
		this.useremail = useremail;
	}
	public Long getEntityid() {
		return entityid;
	}
	public void setEntityid(Long entityid) {
		this.entityid = entityid;
	}
	public Long getComponentid() {
		return componentid;
	}
	public void setComponentid(Long componentid) {
		this.componentid = componentid;
	}
	public String getTextparam() {
		return textparam;
	}
	public void setTextparam(String textparam) {
		this.textparam = textparam;
	}
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("user: ");
		sb.append(this.useremail);
		sb.append(" Entity: ");
		sb.append(this.entityid);
		sb.append(" Text: ");
		sb.append(this.textparam);
		return sb.toString();
	}
}
