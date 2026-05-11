package aulab.it.the_aulab_chronicle.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Override
    @Async
    public void sendSimpleEmail(String to, String object, String text) {
       
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("test@aulabcronichle.it");
        message.setTo(to);
        message.setSubject(object);
        message.setText(text);
        mailSender.send(message);

    }

}
