package com.themadphysicist;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.Message;
import com.google.api.client.repackaged.org.apache.commons.codec.binary.Base64;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

class EmailEngine {
  private static final int DailySendLimit = 100;
  private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
  private static final List<String> SCOPES = Collections.singletonList(GmailScopes.GMAIL_SEND);
  private static final String APPLICATION_NAME = "EmailNotifier";
  private static final String TOKENS_DIRECTORY_PATH = "tokens";

  private static Message createMessageWithEmail(MimeMessage email) throws MessagingException, IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    email.writeTo(baos);
    String encodedEmail = Base64.encodeBase64URLSafeString(baos.toByteArray());
    Message message = new Message();
    message.setRaw(encodedEmail);
    return message;
  }

  private static void sendMessage(Gmail service, String userId, MimeMessage email)
      throws MessagingException, IOException {
    Message message = createMessageWithEmail(email);
    message = service.users().messages().send(userId, message).execute();
    System.out.println("Message id: " + message.getId());
  }

  private static GoogleClientSecrets getClientSecrets(final JSONReader reader) {
    GoogleClientSecrets secrets = new GoogleClientSecrets();
    GoogleClientSecrets.Details details = new GoogleClientSecrets.Details();
    details.setFactory(JSON_FACTORY);
    details.setClientId(reader.client_id);
    details.set("project_id", reader.project_id);
    details.setAuthUri(reader.auth_uri);
    details.setTokenUri(reader.token_uri);
    details.set("auth_provider_x509_cert_uri", reader.auth_provider_x509_cert_url);
    details.setClientSecret(reader.client_secret);
    details.setRedirectUris(reader.redirect_uris);
    secrets.setWeb(details);
    return secrets;
  }

  private static Credential getCredentials(final JSONReader reader, final NetHttpTransport HTTP_TRANSPORT)
      throws IOException {
    GoogleClientSecrets clientSecrets = getClientSecrets(reader);
    GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
        HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
        .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
        .setAccessType("offline")
        .build();
    LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
    return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
  }

  static void SendEmails(List<Subscriber> subscribers, JSONReader reader, String emailContents)
      throws GeneralSecurityException, MessagingException, IOException {
    final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
    Gmail service = new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(reader, HTTP_TRANSPORT))
        .setApplicationName(APPLICATION_NAME)
        .build();
    Properties properties = System.getProperties();
    Session session = Session.getDefaultInstance(properties);
    MimeMessage message = new MimeMessage(session);
    message.setSubject(reader.subject);
    message.setContent(emailContents, "text/html");
    for (Subscriber subscriber : subscribers) {
      MimeMessage ToSend = new MimeMessage(message);
      ToSend.addRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(subscriber.getEmail()));
      sendMessage(service, reader.user, ToSend);
    }
  }
}
