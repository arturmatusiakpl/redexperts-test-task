package pl2.lines.example.redexperts.listeners;

import pl2.lines.example.redexperts.utils.DownloadManager.DOWNLOAD_ERROR;

public interface DownloadManagerListener {

  public void downloadResult(String result, DOWNLOAD_ERROR downloadErr);
}
