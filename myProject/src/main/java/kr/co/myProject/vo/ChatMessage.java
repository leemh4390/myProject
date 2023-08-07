package kr.co.myProject.vo;

import lombok.Builder;
import lombok.Data;

@Data
public class ChatMessage {
	private String username;
	private String message;
}
