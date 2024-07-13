package sitpass.sitpassbackend.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import sitpass.sitpassbackend.service.EmailService;

@RequiredArgsConstructor
@Service
public class EmailServiceImpl implements EmailService {

    private final Logger logger = LogManager.getLogger(RegistrationServiceImpl.class);

    private final JavaMailSender emailSender;

    @Override
    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
        logger.info("Email sent to {}", to);
    }
}
