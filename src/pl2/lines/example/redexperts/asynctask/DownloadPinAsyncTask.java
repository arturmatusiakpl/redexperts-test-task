package pl2.lines.example.redexperts.asynctask;

import pl2.lines.example.redexperts.listeners.DownloadManagerListener;
import pl2.lines.example.redexperts.utils.DownloadManager;
import android.os.AsyncTask;

public class DownloadPinAsyncTask extends AsyncTask<String, Void, String> {

  private DownloadManagerListener downloadManagerListener;
  private DownloadManager           downloadManager;

  public DownloadPinAsyncTask() {
    downloadManager = new DownloadManager();
  }

  public void setOnDownloadManagerListener(DownloadManagerListener downloadManagerListener) {
    this.downloadManagerListener = downloadManagerListener;
  }

  @Override
  protected String doInBackground(String... params) {
    downloadManager.downloadJson();
    return downloadManager.json;
  }

  @Override
  protected void onPostExecute(String result) {
    if (downloadManagerListener != null)
      downloadManagerListener.downloadResult(result, downloadManager.getError());
  }

}
