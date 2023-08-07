package kr.co.myProject.controller;

import lombok.extern.log4j.Log4j2;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Log4j2
@Controller
public class ChatController {
	
    @GetMapping("board/chat")
    public String chatrooms(Authentication authentication, Model model){
    	
    	String username = authentication.getName();
    	
    	model.addAttribute("username", username);
    	
        return "board/chat";
    }
}
