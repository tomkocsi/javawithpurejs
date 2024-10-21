package kocsist.DTOmodel;

public class ParamNodeDTO {
	private NodeDTO nodedto;
	private String useremail;
	private Long entityid;
	public NodeDTO getNodedto() {
		return nodedto;
	}
	public void setNodedto(NodeDTO nodedto) {
		this.nodedto = nodedto;
	}
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
}
