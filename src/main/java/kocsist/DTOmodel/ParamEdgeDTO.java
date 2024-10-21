package kocsist.DTOmodel;

public class ParamEdgeDTO {
	private EdgeDTO edgedto;
	private String useremail;
	private Long entityid;
	public Long getEntityid() {
		return entityid;
	}
	public void setEntityid(Long entityid) {
		this.entityid = entityid;
	}
	public EdgeDTO getEdgedto() {
		return edgedto;
	}
	public void setEdgedto(EdgeDTO edgedto) {
		this.edgedto = edgedto;
	}
	public String getUseremail() {
		return useremail;
	}
	public void setUseremail(String useremail) {
		this.useremail = useremail;
	}
}
