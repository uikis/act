package com.example.act.util;

import com.example.act.permission.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.internet.MimeMessage;

@Component
public class Email {

    @Autowired
    private JavaMailSender mailSender;

    public void sendSimpleMail(User user) {
        MimeMessage message = null;
        try {
            message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom("m13882055119@163.com");
            helper.setTo(user.getEmail());
            helper.setSubject("标题：点击内容链接重置密码");
            String tt = user.getToken();
            StringBuffer sb = new StringBuffer();
            sb.append("<a href = 'http://www.localhost:8080/resetPwd?token=" + tt + "'>http://www.localhost:8080/resetPwd?token=" + tt + " </a>");
            helper.setText(sb.toString(), true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mailSender.send(message);
    }
}


