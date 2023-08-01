package kr.co.myProject.config.oauth;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import kr.co.myProject.config.CustomBCryptPasswordEncoder;
import kr.co.myProject.config.auth.PrincipalDetails;
import kr.co.myProject.entity.User;
import kr.co.myProject.repository.UserRepository;

//해당 메서드의 리턴된 오브젝트를 IOC 로 등록
@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService{

	@Autowired
	private CustomBCryptPasswordEncoder customBCryptPasswordEncoder;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private HttpServletRequest servletRequest;
	
	// 후처리 되는 함수
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		
		System.out.println("getRegistrationId : " + userRequest.getClientRegistration().getRegistrationId()); // registrationId 어떤 Oauth 로그인을 했는지?
		System.out.println("getTokenValue : " + userRequest.getAccessToken().getTokenValue());
		System.out.println("getAttributes : " + super.loadUser(userRequest).getAttributes());
		
		OAuth2User oauth2User = super.loadUser(userRequest);
		// 구글 로그인 버튼 클릭 -> 구글 로그인창 -> 로그인을 완료 ->  code 를 리턴(Oauth -> Client라이브러리)
		// -> AccessToken 을 요청
		// userRequest 정보 -> 회원프로필 받아야함(loadUser함수) 호출 -> 구글로부터 회원프로필 받아준다.
		
		String provider = userRequest.getClientRegistration().getRegistrationId();// google
		String providerId = oauth2User.getAttribute("sub"); // 구글의 provider Id
		String username = provider + "_" + providerId; // uid = google_1097428561829716427686
		String password = customBCryptPasswordEncoder.encode("겟인데어"); // password
		String email = oauth2User.getAttribute("email"); // email
		String regip = servletRequest.getRemoteAddr();
		String role = "ROLE_USER";
		
		User userEntity = userRepository.findByUsername(username);
		
		if(userEntity == null) {
			System.out.println(" 구글 로그인이 최초입니다.");
			userEntity = User.builder()
					.username(username)
					.password(password)
					.email(email)
					.role(role)
					.regip(regip)
					.provider(provider)
					.providerId(providerId)
					.build();
			
			userRepository.save(userEntity);
		}else {
			System.out.println(" 구글 로그인을 이미 한 적이 있습니다. ");
		}
		
		return new PrincipalDetails(userEntity,oauth2User.getAttributes());
	}
	
}
