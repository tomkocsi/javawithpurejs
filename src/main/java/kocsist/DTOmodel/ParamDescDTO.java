package kocsist.DTOmodel;

public class ParamDescDTO {
	private DescDTO descdto;
	private String useremail;
	private Long entityid;
	public DescDTO getDescdto() {
		return descdto;
	}
	public void setDescdto(DescDTO descdto) {
		this.descdto = descdto;
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
