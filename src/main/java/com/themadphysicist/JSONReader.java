package com.themadphysicist;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
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
  String client_id;
  String project_id;
  String auth_uri;
  String token_uri;
  String auth_provider_x506_cert_url;
  String client_secret;
  List<String> redirect_uris;
  JSONReader(Path configpath) throws JSONException, IOException {
    String jsontxt = new String(Files.readAllBytes(configpath));
    JSONObject json = new JSONObject(jsontxt);
    subcsvpath = json.getString("subcsvpath");
    subject = json.getString("emailsubject");
    htmlpath = json.getString("emailhtmlpath");
    client_id = json.getString("client_id");
    project_id = json.getString("project_id");
    auth_uri = json.getString("auth_uri");
    token_uri = json.getString("token_uri");
    auth_provider_x506_cert_url = json.getString("auth_provider_x506_cert_url");
    client_secret = json.getString("client_secret");
    redirect_uris = new ArrayList<>();
    JSONArray arr = json.getJSONArray("redirect_uris");
    for (int i = 0; i < arr.length(); i++)
      redirect_uris.add(arr.getString(i));
  }
}
