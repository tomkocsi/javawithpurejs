package kocsist.DTOmodel;

public class ParamPicDTO {
	private PicDataDTO picdto;
	private String useremail;
	private Long entityid;
	public PicDataDTO getPicdto() {
		return picdto;
	}
	public void setPicdto(PicDataDTO picdto) {
		this.picdto = picdto;
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
