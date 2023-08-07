package kr.co.myProject.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

import kr.co.myProject.vo.ChatMessage;

@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {

	ObjectMapper objectMapper = new ObjectMapper();
	List<WebSocketSession> list = Collections.synchronizedList(new ArrayList<>());

		@Override
		public void afterConnectionEstablished(WebSocketSession session) throws Exception {
			
	        System.out.println("접속===========================");
	        System.out.println("session.getId() : " + session.getId());
	        System.out.println("session.getName() : " + session.getPrincipal().getName());
	        
	        
	        
	        System.out.println("접속===========================");
	        list.add(session);
		}
		
		@Override
		protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
			
			// 사용자 아이디와 메시지 출력
	        System.out.println("작성자는 : " + session.getPrincipal().getName() + " 댓글 내용은 : " + message.getPayload());
	        
	        String username = session.getPrincipal().getName();
	        
			ChatMessage clientMessage = objectMapper.readValue(message.getPayload(), ChatMessage.class);
			
			ChatMessage chatMessage = new ChatMessage();
			
			chatMessage.setUsername(username);
			chatMessage.setMessage(clientMessage.getMessage());
			
			String json = objectMapper.writeValueAsString(chatMessage);
			
	        for(WebSocketSession wss : list){
	            wss.sendMessage(new TextMessage(json));
	        }
		}
		
		
	    // 웹 소켓이 연결이 클로즈될때 호출되는 메소드
	    @Override
	    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
	        System.out.println("접속 close===========================");
	        System.out.println(session.getId());
	        System.out.println("접속 close===========================");
	        list.remove(session);
	    }
}
