package pl2.lines.example.redexperts.view;

import java.text.DecimalFormat;

import pl2.lines.example.redexperts.R;
import pl2.lines.example.redexperts.utils.DownloadManager.DOWNLOAD_ERROR;
import pl2.lines.example.redexperts.utils.Json.JSON_ERROR;
import android.content.Context;

public class Formatters {

  private static final int printKmOver = 990;
  private DecimalFormat    decimalFormat;
  private Context          context;

  public Formatters(Context context) {
    decimalFormat = new DecimalFormat();
    this.context = context;
  }

  public String formatDistance(double distance) {
    if (distance > printKmOver)
      return context.getString(R.string.distnace_kilometers, decimalFormat.format(distance / 1000));
    return context.getString(R.string.distnace_meters, decimalFormat.format(distance));
  }

  public String translateErrorCode(DOWNLOAD_ERROR err) {
    switch (err) {
    case IO:
      return context.getString(R.string.error_io);
    case TIMEOUT:
      return context.getString(R.string.error_timeout);
    case URL:
      return context.getString(R.string.error_url);
    default:
      return "";
    }

  }

  public String translateErrorCode(JSON_ERROR err) {
    if(err == JSON_ERROR.JSON)
      return context.getString(R.string.error_json);
    return "";
  }
}
