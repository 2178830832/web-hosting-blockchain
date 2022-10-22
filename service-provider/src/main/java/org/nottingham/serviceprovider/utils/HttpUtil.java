package org.nottingham.serviceprovider.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.apache.catalina.Host;

public class HttpUtil {

  private static final String USER_AGENT = "Mozilla/5.0";
  private static final String HOST_IP = "http://10.201.112.80".concat(":8090");

  public static void main(String[] args) {
    HttpUtil http = new HttpUtil();
    HttpsURLConnection.setDefaultHostnameVerifier(new NullHostNameVerifier());

    try {
      SSLContext sc;
      sc = SSLContext.getInstance("TLS");
      sc.init(null, trustAllCerts, new SecureRandom());
      HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
    } catch (NoSuchAlgorithmException | KeyManagementException e) {
      e.printStackTrace();
    }

    System.out.println("Send Http GET request");
    http.sendGet();

//    System.out.println("Send Http POST request");
//    http.sendPost();

  }


  private void sendGet() {
    String url = HOST_IP;
    try {
      System.out.println(responseHelper(new URL(url), "", "get"));
    } catch (IOException e) {
      e.printStackTrace();
      System.out.println("Failed to send get request");
    }
  }


  private void sendPost() {
    String url = HOST_IP.concat("/request/index");
    String urlParameters = "JSON.toJSONString(new HttpUtil())";
    try {
      System.out.println(responseHelper(new URL(url), urlParameters, "post"));
    } catch (IOException e) {
      e.printStackTrace();
      System.out.println("Failed to send post request");
    }
  }

  private static StringBuffer responseHelper(URL url, String urlParameters, String method)
      throws IOException {
    StringBuffer response = new StringBuffer();
    System.out.println("Sending" + " '" + method + " '" + " request to URL: " + url);
    if (method.equals("get")) {
      HttpURLConnection connect = (HttpURLConnection) url.openConnection();
      connect.setRequestProperty("User-Agent", USER_AGENT);
      BufferedReader in = new BufferedReader(
          new InputStreamReader(connect.getInputStream()));

      String inputLine;
      while ((inputLine = in.readLine()) != null) {
        response.append(inputLine);
      }
      in.close();
    } else if (method.equals("post")) {
      HttpsURLConnection connect = (HttpsURLConnection) url.openConnection();
      connect.setRequestMethod("POST");
      connect.setRequestProperty("User-Agent", USER_AGENT);
      connect.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
      connect.setDoOutput(true);

      DataOutputStream wr = new DataOutputStream(connect.getOutputStream());
      wr.writeBytes(urlParameters);
      wr.flush();
      wr.close();

      BufferedReader in = new BufferedReader(
          new InputStreamReader(connect.getInputStream()));

      String inputLine;
      while ((inputLine = in.readLine()) != null) {
        response.append(inputLine);
      }
      in.close();
    }
    return response;
  }

  static TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType) {
      // TODO Auto-generated method stub
    }

    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType) {
      // TODO Auto-generated method stub
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
      // TODO Auto-generated method stub
      return null;
    }
  }};

  public static class NullHostNameVerifier implements HostnameVerifier {

    /*
     * (non-Javadoc)
     *
     * @see javax.net.ssl.HostnameVerifier#verify(java.lang.String,
     * javax.net.ssl.SSLSession)
     */
    @Override
    public boolean verify(String arg0, SSLSession arg1) {
      // TODO Auto-generated method stub
      return true;
    }
  }

}