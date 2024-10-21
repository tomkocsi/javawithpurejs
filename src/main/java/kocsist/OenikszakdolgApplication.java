package kocsist;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//import org.springframework.web.servlet.config.annotation.CorsRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
// az alábbi annotácio nelkul nem talalja a service
// interfeszeket
@ComponentScan(basePackages = {"kocsist", "kocsist.service"})
@SpringBootApplication
public class OenikszakdolgApplication {

	public static void main(String[] args) {
		SpringApplication.run(OenikszakdolgApplication.class, args);
	}
	
	@Bean
    public WebMvcConfigurer corsConfigurer(){
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedMethods("GET","POST","PUT","DELETE")
                        .allowedHeaders("*")
                        .allowedOrigins("*");
            }
        };
    }

}
