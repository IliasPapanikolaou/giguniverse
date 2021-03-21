package com.unipi.giguniverse.service;

import com.unipi.giguniverse.exceptions.ApplicationException;
import com.unipi.giguniverse.model.NotificationEmail;
import com.unipi.giguniverse.model.Ticket;
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
          messageHelper.setFrom("giguniverse@email.com");
          messageHelper.setTo(notificationEmail.getRecipient());
          messageHelper.setSubject(notificationEmail.getSubject());
          messageHelper.setText(mailContentBuilder.build(notificationEmail.getBody()),true);
        };

        try{
            mailSender.send(messagePreparator);
            log.info("Activation Email sent!");
        }
        catch (MailException e){
            throw new ApplicationException("Exception occurred! Failed to send the" +
                    " email to recipient: "+notificationEmail.getRecipient());
        }
    }

    @Async
    public void sendTicketEMail(Ticket ticket){
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom("giguniverse@email.com");
            messageHelper.setTo(ticket.getTicketHolderEmail());
            messageHelper.setSubject("Αγορά Εισιτηρίου απο GigUniverse");
            messageHelper.setText(mailContentBuilder.buildTicketMail(ticket),true);
        };

        try{
            mailSender.send(messagePreparator);
            log.info("Ticket Email sent!");
        }
        catch (MailException e){
            throw new ApplicationException("Exception occurred! Failed to send the" +
                    " email to recipient: "+ticket.getTicketHolderEmail());
        }
    }
}
