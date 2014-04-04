package pl2.lines.example.redexperts.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;

public class DownloadManager {

  private final static String urlFile   = "https://dl.dropboxusercontent.com/u/6556265/test.json";
  private final static int    timeoutMs = 5000;
  private DOWNLOAD_ERROR err = DOWNLOAD_ERROR.ER_OK;

  public int statusCode;
  public String json = "";
  
  public enum DOWNLOAD_ERROR {
    ER_OK, URL, TIMEOUT, IO
  };

  public DOWNLOAD_ERROR getError() {
    return err;
  }
  
  public void downloadJson() {
    HttpURLConnection urlConnection = null;
    try {
      URL urlToRequest = new URL(urlFile);
      urlConnection = (HttpURLConnection) urlToRequest.openConnection();
      urlConnection.setConnectTimeout(timeoutMs);
      urlConnection.setReadTimeout(timeoutMs);
      
      statusCode = urlConnection.getResponseCode();
      
      buildStringFromInputStream(urlConnection.getInputStream());
      
      err = DOWNLOAD_ERROR.ER_OK;
    } catch (MalformedURLException e) {
      e.printStackTrace();
      err = DOWNLOAD_ERROR.URL;
    } catch (SocketTimeoutException e) {
      e.printStackTrace();
      err = DOWNLOAD_ERROR.TIMEOUT;
    } catch (IOException e) {
      e.printStackTrace();
      err = DOWNLOAD_ERROR.IO;
    } finally {
      if (urlConnection != null) {
        urlConnection.disconnect();
      }
    }
  }

  private void buildStringFromInputStream(InputStream is) throws IOException {
    StringBuilder stringBuilder = new StringBuilder();
    InputStreamReader in2 = new InputStreamReader(is);
    int read;
    char[] buff = new char[1024];
    while ((read = in2.read(buff)) != -1) {
      stringBuilder.append(buff, 0, read);
    }
    
    json = stringBuilder.toString();
  }
}
