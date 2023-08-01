package kr.co.myProject.vo;

import java.sql.Timestamp;

import javax.persistence.Id;

import org.hibernate.annotations.CreationTimestamp;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserVO {

	@Id
	private int Id;
	private String username;
	private String password;
	private String email;
	private String role;
	
	@CreationTimestamp
	private Timestamp date;
	
	private String provider;
	private String providerId;
	private int age;
	private String hp;
	private String zip;
	private String addr1;
	private String addr2;
	private String regip;
	
	// 추가필드
	private int count;
}
