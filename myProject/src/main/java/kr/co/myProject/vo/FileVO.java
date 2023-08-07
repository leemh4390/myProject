package kr.co.myProject.vo;

import java.sql.Timestamp;

import org.hibernate.annotations.CreationTimestamp;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FileVO {
	private int fno;
	private int parent;
	private String newName;
	private String oriName;
	private int download;
	@CreationTimestamp
	private Timestamp rdate;
}
