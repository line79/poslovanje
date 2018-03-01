package com.sargije.web.poslovanje.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	DataSource dataSource;
	@Autowired
	PersistentTokenRepository persistentTokenRepository;
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.jdbcAuthentication().dataSource(dataSource);
		//.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		PasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder;
	}
	
	@Override
	  protected void configure(HttpSecurity http) throws Exception {
		 http.authorizeRequests()
        .antMatchers("/h2-console/**").permitAll()        
       .antMatchers("/public").permitAll()
        .anyRequest().access("hasAuthority('admin')")
        .and().formLogin().permitAll()
        .and().logout().logoutUrl("/logout").permitAll();
		
		http.rememberMe(). 
		tokenRepository(persistentTokenRepository).
		rememberMeParameter("remember-me-param").
		rememberMeCookieName("my-remember-me").
		tokenValiditySeconds(86400);
		
		http.csrf().disable();
		http.headers().frameOptions().disable();
	  }
}
