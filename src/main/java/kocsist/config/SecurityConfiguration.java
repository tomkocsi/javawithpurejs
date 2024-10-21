package kocsist.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter{
	@Autowired
	UserDetailsService userDetailsService;
	
	@Bean
	public BCryptPasswordEncoder  passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());		
	}
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
		.antMatchers("/login").permitAll()		
		.antMatchers("/mylogout").permitAll()
		.antMatchers("/api/registrate").permitAll()
		.antMatchers("/api/downloadpic").hasAnyRole("USER")
		.antMatchers("/api/uploadpics").hasAnyRole("USER")
		.antMatchers("/api/dataexchange/**").hasAnyRole("USER")
		.antMatchers("/app").hasAnyRole("USER")
		.and()
		.formLogin()
		.loginPage("/login")
		.permitAll()
		.and()
		.logout()
		.logoutSuccessUrl("/mylogout")
		.permitAll()
		.invalidateHttpSession(true)
		.deleteCookies("JSESSIONID")
		.and()
		.csrf().disable();
		
	}
}
