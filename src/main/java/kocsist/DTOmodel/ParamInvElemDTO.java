package kocsist.DTOmodel;

public class ParamInvElemDTO {
	private InvElemDTO invelemdto;
	private String useremail;
	private Long entityid;
	public InvElemDTO getInvelemdto() {
		return invelemdto;
	}
	public void setInvelemdto(InvElemDTO invelemdto) {
		this.invelemdto = invelemdto;
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
