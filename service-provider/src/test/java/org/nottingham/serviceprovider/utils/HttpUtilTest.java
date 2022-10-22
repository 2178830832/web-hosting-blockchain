package org.nottingham.serviceprovider.utils;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Desktop;
import java.awt.Desktop.Action;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import org.junit.jupiter.api.Test;

class HttpUtilTest {

  @Test
  void testBrowser() {
    if (Desktop.isDesktopSupported()) {
      try {
        URI uri = URI.create("http://localhost:8090");
        Desktop desktop = Desktop.getDesktop();
        if (desktop.isSupported(Action.BROWSE)) desktop.browse(uri);
        System.out.println("Opened in the default browser: " + uri);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

}