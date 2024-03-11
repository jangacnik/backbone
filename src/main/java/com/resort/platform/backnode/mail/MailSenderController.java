package com.resort.platform.backnode.mail;

import com.resort.platform.backnode.mail.models.PasswordResetRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/mail")
public class MailSenderController {

  @Autowired
  private JavaMailSender emailSender;



  @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
  @PostMapping("/pass/reset")
  public ResponseEntity<Void> resetPassword(@RequestBody PasswordResetRequest passwordResetRequest) {
    sendSimpleMessage("gacnik.jan@gmail.com", "Password reset", "Test");
    return ResponseEntity.ok(null);
  }

  public void sendSimpleMessage(
      String to, String subject, String text) {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setFrom("noreply@vestlia.com");
    message.setTo(to);
    message.setSubject(subject);
    message.setText(text);
    emailSender.send(message);
  }

}
