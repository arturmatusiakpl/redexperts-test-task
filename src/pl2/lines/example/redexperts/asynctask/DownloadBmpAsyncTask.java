package pl2.lines.example.redexperts.asynctask;

import java.io.IOException;
import java.net.URL;

import pl2.lines.example.redexperts.listeners.DownloadBmpListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

public class DownloadBmpAsyncTask extends AsyncTask<String, Void, Bitmap> {

  private DownloadBmpListener downloadBmpListener;

  public void setOnDownloadBmpListener(DownloadBmpListener downloadBmpListener) {
    this.downloadBmpListener = downloadBmpListener;
  }

  @Override
  protected Bitmap doInBackground(String... params) {
    Bitmap bmp = null;
    try {
      URL newurl = new URL(params[0]);
      bmp = BitmapFactory.decodeStream(newurl.openConnection().getInputStream());
    } catch (IOException e) {
      e.printStackTrace();
    }
    return bmp;
  }

  @Override
  protected void onPostExecute(Bitmap result) {
    if (downloadBmpListener != null)
      downloadBmpListener.downloaded(result);
  }

}