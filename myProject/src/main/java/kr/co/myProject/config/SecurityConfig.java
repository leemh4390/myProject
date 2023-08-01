package kr.co.myProject.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import kr.co.myProject.config.oauth.PrincipalOauth2UserService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private CustomBCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private PrincipalOauth2UserService principalOauth2UserService;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http.csrf().disable(); 
		
		http.authorizeRequests()
		.antMatchers("/user/**").authenticated()
		.antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
		.anyRequest().permitAll()
		.and()
		.formLogin()
		.loginPage("/loginForm")
		.loginProcessingUrl("/login") // login 주소가 호출이 되면 시큐리티가 낚아채서 대신 로그인을 진행
		.defaultSuccessUrl("/")
		.and()
		.oauth2Login()
		.loginPage("/loginForm")
		.userInfoEndpoint()
		.userService(principalOauth2UserService);
		// 구글 로그인이 완료된 뒤의 후처리가 필요함
		// 1. 코드받기(인증) 2. 엑세스토큰(권한) 3. 사용자 프로필 정보를 가져옴 4. 그 정보를 토대로 회원가입을 자동으로
		// 구글 로그인이 완료되면 코드가 아닌 엑세스토큰 + 사용자 프로필 정보를 받아옴
	}
}
