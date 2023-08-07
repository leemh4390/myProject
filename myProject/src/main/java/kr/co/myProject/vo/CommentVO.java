package kr.co.myProject.vo;

import java.sql.Timestamp;

import org.hibernate.annotations.CreationTimestamp;

import lombok.Data;

@Data
public class CommentVO {
	
	private int no;
	private int parent;
	private String username;
	private String content;
	private String regip;
	@CreationTimestamp
	private Timestamp date;
	
	// 추가필드
	private int count;

}
