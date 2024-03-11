package com.resort.platform.backnode.mail.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {


  @Autowired
  private JavaMailSender emailSender;

  /**
   * Sends a simple text only email to the given email.
   *
   * @param to email address to which the email message should be sent to
   * @param subject subject of the email
   * @param text text/body of the email
   */
  public void sendSimpleMessage(
      String to, String subject, String text) {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setTo(to);
    message.setSubject(subject);
    message.setText(text);
    emailSender.send(message);
  }

}
