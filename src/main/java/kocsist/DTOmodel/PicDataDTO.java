package kocsist.DTOmodel;

import kocsist.model.Picture;

public class PicDataDTO {
	private Long id;
	private String imagesrc;
	private String userEmail;
	private Long edgeId;
	private String message;
	
	public String getUserEmail() {
		return userEmail;
	}
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	public Long getEdgeId() {
		return edgeId;
	}
	public void setEdgeId(Long edgeId) {
		this.edgeId = edgeId;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long pictureId) {
		this.id = pictureId;
	}
	public String getImagesrc() {
		return imagesrc;
	}
	public void setImagesrc(String imagesrc) {
		this.imagesrc = imagesrc;
	}
	public void buildByPicture(Picture p) {
		this.id = p.getId();
		this.imagesrc = p.getPathOnServer();
		if(p.getUser() != null) {
			this.userEmail = p.getUser().getEmail();
		}
		if(p.getEdge() != null) {
			this.edgeId = p.getEdge().getId();
		}
	}
}
