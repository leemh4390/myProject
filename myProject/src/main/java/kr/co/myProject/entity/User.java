package kr.co.myProject.entity;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name="user")
@NoArgsConstructor
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String username;
	private String password;
	private String email;
	private String role;
	@CreationTimestamp
	private Timestamp date;
	
	private String regip;
	private String provider;
	private String providerId;
	
	@Builder
	public User(String username, String password, String email, String role,String regip, Timestamp date, String provider,
			String providerId) {
		this.username = username;
		this.password = password;
		this.email = email;
		this.role = role;
		this.regip = regip;
		this.date = date;
		this.provider = provider;
		this.providerId = providerId;
	}
}
