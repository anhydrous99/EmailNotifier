package com.themadphysicist;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

class EmailEngine {
  static void generateAndSendEmail(String host, String port, String user, String password,
                                          String subject, String mailContents, String to) throws MessagingException {
    Properties mailServerProperties;
    Session getMailSession;
    MimeMessage generateMailMessage;

    // Setup Server Properties
    mailServerProperties = System.getProperties();
    mailServerProperties.put("mail.smtp.port", port);
    mailServerProperties.put("mail.smtp.auth", "true");
    mailServerProperties.put("mail.smtp.starttls.enable", "true");

    // Step up mail session
    getMailSession = Session.getDefaultInstance(mailServerProperties, null);
    generateMailMessage = new MimeMessage(getMailSession);
    generateMailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
    generateMailMessage.setSubject(subject);
    generateMailMessage.setContent(mailContents, "text/html");

    // Send Email
    Transport transport = getMailSession.getTransport("smtp");

    // Enter your correct gmail UserID and Password
    // if you have 2FA enabled then provide App Specific Password
    transport.connect(host, user, password);
    transport.sendMessage(generateMailMessage, generateMailMessage.getAllRecipients());
    transport.close();
  }
}
