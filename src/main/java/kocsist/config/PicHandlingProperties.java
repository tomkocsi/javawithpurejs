package kocsist.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("pictures")
public class PicHandlingProperties {
	private String picturefolder;
	private String errorpicurl;
	public String getPicturefolder() {
		return picturefolder;
	}
	public void setPicturefolder(String picturefolder) {
		this.picturefolder = picturefolder;
	}
	public String getErrorpicurl() {
		return errorpicurl;
	}
	public void setErrorpicurl(String errorpicurl) {
		this.errorpicurl = errorpicurl;
	}
}
