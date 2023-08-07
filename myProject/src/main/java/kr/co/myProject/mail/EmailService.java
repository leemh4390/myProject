package kr.co.myProject.mail;

import java.util.Random;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {
	
	@Autowired
	private JavaMailSender mailSender;
	private int authNumber;
	
    // 인증번호 및 임시 비밀번호 생성 메서드
    public String createCode() {
        Random random = new Random();
        StringBuffer key = new StringBuffer();

        for (int i = 0; i < 8; i++) {
            int index = random.nextInt(4);

            switch (index) {
                case 0: key.append((char) ((int) random.nextInt(26) + 97)); break;
                case 1: key.append((char) ((int) random.nextInt(26) + 65)); break;
                default: key.append(random.nextInt(9));
            }
        }
        return key.toString();
    }
	
	
	public String joinEmail(String email) {
		String authNum = createCode();
		
		// System.out.println("authNum : " + authNum); // 인증번호 확인하기..
		
		String setFrom = "mySpringService@spring.com";
		String toMail = email;
		String title = "안녕하세요. mySpringService 입니다.";
		String content = "<h3>안녕하세요. mySpringService 입니다.</h3><br/>" +
						 "<p>요청하신 인증번호 입니다.</p>"+
						 "<p>인증번호는 "+ authNum + "입니다.</p>"+
						 "<p>해당 인증번호를 인증번호 확인란에 기입하여 주세요.</p>";
		mailSend(setFrom, toMail, title, content);
		
		System.out.println("authNum : " + authNum);
		
		return authNum;
	}
	
	//이메일 전송 메소드
	public void mailSend(String setFrom, String toMail, String title, String content) { 
		
		MimeMessage message = mailSender.createMimeMessage();
		// true 매개값을 전달하면 multipart 형식의 메세지 전달이 가능.문자 인코딩 설정도 가능하다.
		
		try {
			MimeMessageHelper helper = new MimeMessageHelper(message,true,"utf-8");
			helper.setFrom(setFrom);
			helper.setTo(toMail);
			helper.setSubject(title);
			// true 전달 > html 형식으로 전송 , 작성하지 않으면 단순 텍스트로 전달.
			helper.setText(content,true);
			mailSender.send(message);
			
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
}
