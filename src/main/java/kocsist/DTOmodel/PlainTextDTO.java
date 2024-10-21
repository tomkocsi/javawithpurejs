package kocsist.DTOmodel;

public class PlainTextDTO {
	private String text = "OK";
	private String errormsg;
	
	public String getErrormsg() {
		return errormsg;
	}

	public void setErrormsg(String errormsg) {
		this.errormsg = errormsg;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}
