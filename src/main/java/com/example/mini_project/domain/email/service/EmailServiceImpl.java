package com.example.mini_project.domain.email.service;

import com.example.mini_project.domain.email.dto.EmailCheckDto;
import com.example.mini_project.domain.email.dto.EmailRequestDto;
import com.example.mini_project.domain.user.repository.UserRepository;
import com.example.mini_project.global.exception.DuplicationException;
import com.example.mini_project.global.redis.utils.RedisUtils;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender mailSender;
    private final RedisUtils redisUtils;
    private final UserRepository userRepository;

    public EmailServiceImpl(JavaMailSender mailSender, RedisUtils redisUtils, UserRepository userRepository) {
        this.mailSender = mailSender;
        this.redisUtils = redisUtils;
        this.userRepository = userRepository;
    }

    @Override
    public Boolean isEmailAlreadyExisted(String email) {
        return userRepository.findByEmail(email).isPresent() || redisUtils.getData(email) != null;
    }

    //임의의 6자리 양수를 반환합니다.
    public int makeRandomNumber() {
        Random r = new Random();
        StringBuilder randomNumber = new StringBuilder();
        for(int i = 0; i < 6; i++) {
            randomNumber.append(Integer.toString(r.nextInt(10)));
        }

        return Integer.parseInt(randomNumber.toString());
    }

    //mail을 어디서 보내는지, 어디로 보내는지 , 인증 번호를 html 형식으로 어떻게 보내는지 작성합니다.
    @Override
    public String joinEmail(EmailRequestDto requestDto) {
        String email = requestDto.getEmail();

        int authRandomNumber = makeRandomNumber();
        String authNum = String.valueOf(authRandomNumber);
        redisUtils.setData(email, authNum);
        String setFrom = "chickenchobab@gmail.com"; // email-config에 설정한 자신의 이메일 주소를 입력
        String toMail = email;
        String title = "회원 가입 인증 이메일 입니다."; // 이메일 제목
        String content =
                "돛단배달의 민족에 가입해주셔서 감사합니다." + 	//html 형식으로 작성 !
                        "<br><br>" +
                        "인증 번호는 " + authRandomNumber + "입니다." +
                        "<br>" +
                        "인증번호를 제대로 입력해주세요"; //이메일 내용 삽입
        mailSend(setFrom, toMail, title, content);
        return Integer.toString(authRandomNumber);
    }

    @Override
    public void mailSend(String setFrom, String toMail, String title, String content) {
        //JavaMailSender 객체를 사용하여 MimeMessage 객체를 생성
        MimeMessage message = mailSender.createMimeMessage();

        try {
            // 이메일 메시지와 관련된 설정을 수행
            // true를 전달하여 multipart 형식의 메시지를 지원하고, "utf-8"을 전달하여 문자 인코딩을 설정
            MimeMessageHelper helper = new MimeMessageHelper(message,true,"utf-8");
            helper.setFrom(setFrom); //이메일의 발신자 주소 설정
            helper.setTo(toMail); //이메일의 수신자 주소 설정
            helper.setSubject(title); //이메일의 제목을 설정
            helper.setText(content,true); //이메일의 내용 설정 두 번째 매개 변수에 true를 설정하여 html 설정.
            mailSender.send(message);
        } catch (MessagingException e) {
            // 이메일 서버에 연결할 수 없거나, 잘못된 이메일 주소를 사용하거나, 인증 오류가 발생하는 등 오류
            // 이러한 경우 MessagingException이 발생
            e.printStackTrace();
        }
    }

    @Override
    public boolean checkAuthNum(EmailCheckDto checkDto) {
        String email = checkDto.getEmail();
        String authNumber = checkDto.getAuthNumber();

        if(redisUtils.getData(email) == null){
            return false;
        }
        else return redisUtils.getData(email).equals(authNumber);
    }
}
