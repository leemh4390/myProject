package kr.co.myProject.vo;

import lombok.Data;

@Data
public class LikeVO {
	private int no;
	private int parent;
	private String username;
	private int type;
	
	// 추가 필드
	private int count;
}
