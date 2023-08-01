package kr.co.myProject.config.auth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import kr.co.myProject.entity.User;
import lombok.Data;

//시큐리티가 /login 주소 낚아채서 로그인을 진행시킨다.
//로그인을 진행이 완료가 되면 시큐리티 session을 만들어줍니다. (Security ContextHolder)
//오브젝트 => Authentication 객체
//Authentication 안에 User정보가 있어야 됨. 
//User 오브젝트의 타입 => UserDetails 타입 객체

//Security Session => Authentication => UserDetails(PrincipalDetails)

@Data
public class PrincipalDetails implements UserDetails, OAuth2User{
	
	private User user; // 컴포지션
	private Map<String, Object> attributes;
	
	// 일반 로그인
	public PrincipalDetails(User user) {
		this.user = user;
	}
	
	// Oauth 로그인
	public PrincipalDetails(User user, Map<String, Object> attributes) {
		this.user = user;
		this.attributes = attributes;
	}
	
	// 해당 User의 권한을 리턴하는 곳!
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> collect = new ArrayList<>();
		collect.add(new GrantedAuthority() {
			
			@Override
			public String getAuthority() {
				// TODO Auto-generated method stub
				return user.getRole();
			}
		});
		return collect;
	}

	
	@Override
	public String getPassword() {
		// 계정이 갖는 비밀번호
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		// 계정이 갖는 아이디
		return user.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}
	
	@Override
	public boolean isEnabled() {
		
		// 우리 사이트에서 1년동안 회원이 로그인을 하지 않으면 휴면 계정 하기로 함.
		// 현재 시작 - 로그인 한 시간 => 1년을 초과하면  return false; 처리
		// 지금은 할 과정이 아니기에 전부 true 처리
		
		return true;
	}

	@Override
	public Map<String, Object> getAttributes() {
		return attributes;
	}

	@Override
	public String getName() {
		return null;
	}
}