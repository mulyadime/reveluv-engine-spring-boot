package com.mulyadime.reveluv.configuration;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	private String users_query = "SELECT userid, password, is_active "
    		+ " FROM app_user WHERE is_active = 1 AND userid = ?";
	
	private String roles_query = "SELECT au.userid, ar.role_name "
    		+ " FROM app_user au "
    		+ " INNER JOIN app_user_role aur ON (au.pk_app_user = aur.fk_app_user) "
    		+ " INNER JOIN app_role ar ON (aur.fk_app_role = ar.pk_app_role) "
    		+ " WHERE au.is_active = 1 AND au.userid = ?";

	@Autowired
	private DataSource dataSource;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth
			.jdbcAuthentication()
			.usersByUsernameQuery(users_query)
			.authoritiesByUsernameQuery(roles_query)
			.dataSource(dataSource)
			.passwordEncoder(bCryptPasswordEncoder);
		
	}
	
	public void configure(WebSecurity web) {
		web
			.ignoring()
			.antMatchers("/resources/**", "/static/**", "/assets/**");
	}
	
	protected void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests()
			.antMatchers(CoreConstant.URI.HOME).permitAll()
			.antMatchers(CoreConstant.URI.LOGIN).permitAll()
			.antMatchers(CoreConstant.URI.REGISTER).permitAll()
			.antMatchers(CoreConstant.URI.DASHBOARD + "/**").hasAuthority(CoreConstant.Role.USER).anyRequest()
			.authenticated().and().csrf().disable().formLogin()
			.loginPage(CoreConstant.URI.LOGIN)
			.failureUrl(CoreConstant.URI.LOGIN + "?errors=true")
			.defaultSuccessUrl(CoreConstant.URI.DASHBOARD + "/home")
			.usernameParameter("userid")
			.passwordParameter("password")
			.and().logout()
			.logoutRequestMatcher(new AntPathRequestMatcher(CoreConstant.URI.LOGOUT))
			.logoutSuccessUrl(CoreConstant.URI.HOME).and().exceptionHandling()
			.accessDeniedPage(CoreConstant.URI.ACCESS_DENIED);
		
	}

}
