package pl2.lines.example.redexperts.view;

import pl2.lines.example.redexperts.R;
import pl2.lines.example.redexperts.asynctask.DownloadBmpAsyncTask;
import pl2.lines.example.redexperts.listeners.DownloadBmpListener;
import pl2.lines.example.redexperts.model.Pin;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.Marker;

public class MarkerBalloonAdapter implements InfoWindowAdapter, DownloadBmpListener {

  private TextView             text;
  private ImageView            image;
  private View                 balloon;
  private DownloadBmpAsyncTask bmpAsyncTask;
  private Pin                  pin;
  private Bitmap               bmp;
  private Marker               markerShowingInfo;

  public MarkerBalloonAdapter(LayoutInflater inflater) {
    balloon = inflater.inflate(R.layout.marker_balloon, null);
    text = (TextView) balloon.findViewById(R.id.tvText);
    image = (ImageView) balloon.findViewById(R.id.ivImage);
  }

  @Override
  public View getInfoWindow(Marker marker) {
    return (null);
  }

  @Override
  public View getInfoContents(Marker marker) {
    markerShowingInfo = marker;
    if (pin != null)
      text.setText(pin.text);

    if (bmp != null)
      image.setImageBitmap(bmp);

    return balloon;
  }

  public void downloadBitmap(Pin pin) {
    this.pin = pin;
    if (pin != null && bmpAsyncTask == null) {
      bmpAsyncTask = new DownloadBmpAsyncTask();
      bmpAsyncTask.setOnDownloadBmpListener(this);
      bmpAsyncTask.execute(pin.image);
    }
  }

  @Override
  public void downloaded(Bitmap bmp) {
    this.bmp = bmp;
    bmpAsyncTask = null;
    if (markerShowingInfo != null && markerShowingInfo.isInfoWindowShown()) {
      markerShowingInfo.showInfoWindow();
    }
  }

}
