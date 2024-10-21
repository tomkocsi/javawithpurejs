package kocsist.blogictest;

import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import kocsist.config.PicHandlingProperties;


public class TestPicHandlingProperties {
	@Autowired
	private PicHandlingProperties picHandlingProps;
	@Test
	public void testPicHandlingPropsIsNull() {
		/*
		String configdata = this.picHandlingProps.getPicturefolder(); 
		System.out.println();
		System.out.println("****************** testPicHandlingPropsIsNull()  ******************");
		System.out.println();
		System.out.println(configdata);
		*/
		assertNull(this.picHandlingProps);
	}
	
}
