package com.themadphysicist;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.json.JSONException;
import org.json.JSONObject;

class JSONReader {
  String host;
  String port;
  String user;
  String password;
  String subcsvpath;
  String subject;
  String htmlpath;
  JSONReader(Path configpath) throws JSONException, IOException {
    String jsontxt = new String(Files.readAllBytes(configpath));
    JSONObject json = new JSONObject(jsontxt);
    host = json.getString("host");
    port = json.getString("port");
    user = json.getString("user");
    password = json.getString("password");
    subcsvpath = json.getString("subcsvpath");
    subject = json.getString("emailsubject");
    htmlpath = json.getString("emailhtmlpath");
  }
}
