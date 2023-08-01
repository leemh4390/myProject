package kr.co.myProject.config.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import kr.co.myProject.entity.User;
import kr.co.myProject.repository.UserRepository;

// 시큐리티 설정에서 loginProcessingUrl("/login")
// /login 요청이 오면 자동으로 UserDetailsService 타입으로 IoC 되어 있는 loadByUsername 함수가 실행
@Service
public class PrincipalDetailsService implements UserDetailsService{

	@Autowired
	private UserRepository userRepository;
	
	// 시큐리티 session = Authentication = UserDetails
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		User userEntity = userRepository.findByUsername(username);
		
		if(userEntity != null) {
			return new PrincipalDetails(userEntity);
		}
		
		return null;
	}

}
