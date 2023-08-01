package kr.co.myProject.controller;

import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.co.myProject.config.CustomBCryptPasswordEncoder;
import kr.co.myProject.config.auth.PrincipalDetails;
import kr.co.myProject.entity.User;
import kr.co.myProject.repository.UserRepository;
import kr.co.myProject.service.UserService;
import kr.co.myProject.vo.UserVO;

@Controller
public class UserController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserService service;
	
	@Autowired
	private CustomBCryptPasswordEncoder passwordEncoder;
	
	@GetMapping("login")
	public String user() {
		return "loginForm";
	}
	
	@GetMapping("loginForm")
	public String login() {
		return "loginForm";
	}
	
	@GetMapping("register")
	public String join() {
		return "register";
	}
	
	@PostMapping("register")
	public String join(HttpServletRequest req,UserVO vo) {
		
		String regip = req.getRemoteAddr();
		vo.setRegip(regip);
		
		service.insertUser(vo);
		
		return "redirect:/loginForm";
	}
	
	@ResponseBody
	@GetMapping("user")
	public String user1() {
		return "여기는 유저.. 테스트용 페이지..";
	}
}
