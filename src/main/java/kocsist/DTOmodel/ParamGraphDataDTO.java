package kocsist.DTOmodel;

public class ParamGraphDataDTO {
	private GraphDataDTO graphdatadto;
	private String useremail;
	private Long entityid;
	public GraphDataDTO getGraphdatadto() {
		return graphdatadto;
	}
	public void setGraphdatadto(GraphDataDTO graphdatadto) {
		this.graphdatadto = graphdatadto;
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
