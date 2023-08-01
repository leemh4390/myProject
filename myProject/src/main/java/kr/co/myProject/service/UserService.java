package kr.co.myProject.service;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.myProject.config.CustomBCryptPasswordEncoder;
import kr.co.myProject.dao.UserDAO;
import kr.co.myProject.vo.UserVO;

@Service
public class UserService {
	
	@Autowired
	private UserDAO dao;
	
	@Autowired
	private CustomBCryptPasswordEncoder passwordEncoder;
	
	public void insertUser(UserVO vo) {
		vo.setPassword(passwordEncoder.encode(vo.getPassword())); // 패스워드 암호화 후 db 저장
		
	    Timestamp currentDate = new Timestamp(System.currentTimeMillis());
	    vo.setDate(currentDate);
		
		dao.insertUser(vo);
	}; 

}
