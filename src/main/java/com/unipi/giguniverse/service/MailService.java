package com.unipi.giguniverse.service;

import com.unipi.giguniverse.exceptions.ApplicationException;
import com.unipi.giguniverse.model.NotificationEmail;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;
    private final MailContentBuilder mailContentBuilder;

    @Async
    public void sendMail(NotificationEmail notificationEmail){
        MimeMessagePreparator messagePreparator = mimeMessage -> {
          MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
          messageHelper.setFrom("test@email.com");
          messageHelper.setTo(notificationEmail.getRecipient());
          messageHelper.setSubject(notificationEmail.getSubject());
          messageHelper.setText(mailContentBuilder.build(notificationEmail.getBody()));
        };

        try{
            mailSender.send(messagePreparator);
            log.info("Activation email sent!");
        }
        catch (MailException e){
            throw new ApplicationException("Exception occurred! Failed to send the" +
                    " email to recipient: "+notificationEmail.getRecipient());
        }
    }
}
