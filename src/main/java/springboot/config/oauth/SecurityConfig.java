package springboot.config.oauth;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import lombok.RequiredArgsConstructor;
import springboot.domain.user.Role;


// Spring Security 설정 활성화
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	// 구글 로그인을 통해 가져온 정보를 기반으로 가입,수정, 세션 저장하는 기능 제공 
	
	private final CustomOAuth2UserService customOAuth2UserService;
	
	protected void configure(HttpSecurity http) throws Exception {
		// csrf : 크로스 사이트 요청 위조가 가능하도록 설정		
		http.csrf().disable() // h2-console 화면을 사용하기 위해 설정해제.(개발용도)
		.headers().frameOptions().disable()
		.and()
			// url별로 권한 관리를 지정
			.authorizeRequests()
			// 권한 관리 대상을 지정
			.antMatchers("/","/css/**", "/images/**", "/js/**", "/h2-console/**").permitAll()
			.antMatchers("/api/v1/**").hasRole(Role.USER.name())
			// 설정된 값을 제외한 나머지에 대해서는 인증 받은 사용자만 허용
			.anyRequest().authenticated()
		.and()
			// 로그아웃 기능 정의
			.logout()
				.logoutSuccessUrl("/")
		.and()
			.oauth2Login()
				.userInfoEndpoint()
					// 소셜 로그인에 성공했을 때 후속 조치를 구현한 구현체를 등록
					.userService(customOAuth2UserService);
	}
}
