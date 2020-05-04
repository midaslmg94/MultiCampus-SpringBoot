package springboot.config.oauth;


import java.util.Collections;

import javax.servlet.http.HttpSession;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import springboot.config.oauth.dto.OAuthAttributes;
import springboot.config.oauth.dto.SessionUser;
import springboot.domain.user.User;
import springboot.domain.user.UserRepository;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User>{
	private final UserRepository userRepository;
	private final HttpSession httpSession;
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		OAuth2UserService delegate = new DefaultOAuth2UserService();
		OAuth2User oauth2User = delegate.loadUser(userRequest);
		
		// 어떤 소셜 로그인 서비스를 이용하고 있는 지 구분
		String registerationId = userRequest.getClientRegistration().getRegistrationId();
		// OAuth2 로그인 시 사용되는 필드
		String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
		
		// DTO와 같은 역할
		OAuthAttributes attributes = OAuthAttributes.of(registerationId, userNameAttributeName, oauth2User.getAttributes());
		User user = saveOrUpdate(attributes);
		
		// 세션을 만들어준다 -> 로그인 했는지 로그아웃했는지 알려줌 
		httpSession.setAttribute("user", new SessionUser(user));
		
		return new DefaultOAuth2User(Collections.singleton(new SimpleGrantedAuthority(user.getRoleKey())),
				attributes.getAttributes(), attributes.getNameAttributeKey());	
	}
	
	
	private User saveOrUpdate(OAuthAttributes attributes) {
		User user = userRepository.findByEmail(attributes.getEmail())
				.map(entity->entity.update(attributes.getName(), attributes.getPicture()))
				.orElse(attributes.toEntity());				
		return userRepository.save(user);	
	}
	
}
