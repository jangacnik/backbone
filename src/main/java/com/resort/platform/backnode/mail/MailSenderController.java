package com.resort.platform.backnode.mail;

import com.resort.platform.backnode.mail.models.PasswordResetRequest;
import com.resort.platform.backnode.mail.service.MailService;
import com.resort.platform.backnode.mail.service.PasswordResetService;
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
  private MailService mailService;
  @Autowired
  private PasswordResetService passwordResetService;

  private static String SIMPLE_PASSWORD_RESET_TEMPLATE = "New password for your account: %s";


  @PostMapping("/pass/reset")
  public ResponseEntity<Void> resetPassword(@RequestBody PasswordResetRequest passwordResetRequest) {
    String pass = passwordResetService.resetPassword(passwordResetRequest.getEmail());

    String body =  String.format(SIMPLE_PASSWORD_RESET_TEMPLATE, pass);
    mailService.sendSimpleMessage(passwordResetRequest.getEmail(), "Password reset", body);
    return ResponseEntity.ok(null);
  }



}
