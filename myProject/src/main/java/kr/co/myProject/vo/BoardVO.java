package kr.co.myProject.vo;

import java.sql.Timestamp;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class BoardVO {
	private int board_no;
	private int board_parent;
	private String board_title;
	private String board_writer;
	private int board_hit;
	private int board_file;
	private MultipartFile board_fname;
	@CreationTimestamp
	private Timestamp board_date;
	private String board_content;
	private int board_like;
	 
	// 추가필드
	private FileVO fileVO;
	private int fno;
	private String oriName;
	private String newName;
	private int count;
	
}
