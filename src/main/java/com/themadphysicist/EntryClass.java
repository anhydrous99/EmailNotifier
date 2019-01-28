package com.themadphysicist;

import javax.mail.MessagingException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class EntryClass {
  public static void main(String[] args) {
    String jcp = "config.json";
    try {
      JSONReader jsonReader = new JSONReader(Paths.get(jcp));
      List<Subscriber> subscribers = CSVReader.parseCSV(jsonReader.subcsvpath);
      String emailContents = new String(Files.readAllBytes(Paths.get(jsonReader.htmlpath)));
      for (Subscriber subscriber : subscribers) {
        System.out.println("Sending new post email to: " + subscriber.getEmail());
        EmailEngine.generateAndSendEmail(jsonReader.host, jsonReader.port, jsonReader.user, jsonReader.password,
            jsonReader.subject, emailContents, subscriber.getEmail());
      }
    } catch (IOException | MessagingException ex) {
      ex.printStackTrace(System.err);
    }
  }
}
