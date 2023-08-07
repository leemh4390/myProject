package kr.co.myProject.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.co.myProject.config.CustomBCryptPasswordEncoder;
import kr.co.myProject.mail.EmailService;
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
	private EmailService emailService;
	
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
	@GetMapping("register/countUsername")
	public Map<String, Integer> checkUsername(@RequestParam("username") String username){
		
		int count = service.selectCountUsername(username);
		
		Map<String, Integer> result = new HashMap<>();
		
		result.put("result", count);
		
		return result;
	}
	
	
	
	@ResponseBody
	@GetMapping("register/emailAuth")
	public Map<String, String> emailAuth(@RequestParam("authEmail") String authEmail) {
		
		String code = emailService.joinEmail(authEmail);
		
		Map<String, String> result = new HashMap<>();
		
		result.put("result", code);
		
		return result;
	}
	
	@ResponseBody
	@GetMapping("user")
	public String user1() {
		return "여기는 유저.. 테스트용 페이지..";
	}
}
