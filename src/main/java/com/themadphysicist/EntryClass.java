package com.themadphysicist;

import javax.mail.MessagingException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.util.List;

public class EntryClass {
  public static void main(String[] args) {
    String jcp = "config.json";
    try {
      JSONReader jsonReader = new JSONReader(Paths.get(jcp));
      List<Subscriber> subscribers = CSVReader.parseCSV(jsonReader.subcsvpath);
      String emailContents = new String(Files.readAllBytes(Paths.get(jsonReader.htmlpath)));
      EmailEngine.SendEmails(subscribers, jsonReader, emailContents);
    } catch (IOException | MessagingException | GeneralSecurityException ex) {
      ex.printStackTrace(System.err);
    }
  }
}
