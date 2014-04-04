package pl2.lines.example.redexperts;

import java.util.Observable;
import java.util.Observer;

import pl2.lines.example.redexperts.asynctask.DownloadPinAsyncTask;
import pl2.lines.example.redexperts.listeners.DownloadManagerListener;
import pl2.lines.example.redexperts.model.Pin;
import pl2.lines.example.redexperts.utils.DownloadManager.DOWNLOAD_ERROR;
import pl2.lines.example.redexperts.utils.GpsManager;
import pl2.lines.example.redexperts.utils.Json;
import pl2.lines.example.redexperts.utils.Json.JSON_ERROR;
import pl2.lines.example.redexperts.utils.Network;
import pl2.lines.example.redexperts.utils.PlayServices;
import pl2.lines.example.redexperts.view.Formatters;
import pl2.lines.example.redexperts.view.MarkerBalloonAdapter;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends Activity implements DownloadManagerListener, Observer, OnMarkerClickListener {

  private final String         intentActionGps = "android.location.PROVIDERS_CHANGED";

  private DownloadPinAsyncTask downloadAsyncTask;
  private Json                 json;
  private GoogleMap            googleMap;
  private GpsManager           gpsManager;
  private TextView             tvDistance;
  private Formatters           formatters;
  private AlertDialog.Builder  alertDialogBuilder;
  private AlertDialog          alertDialog;
  private PlayServices         playServices;
  private boolean              isPlaySevicesAvailable;
  private JSON_ERROR           jsonError;
  private IntentFilter         intentFilter;
  private BroadcastReceiver    gpsStatusReceiver;
  private MarkerBalloonAdapter markerBalloonAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    formatters = new Formatters(this);

    playServices = new PlayServices(this);
    isPlaySevicesAvailable = playServices.checkPlayServices();
    json = new Json();

    tvDistance = (TextView) findViewById(R.id.tvDistance);
    markerBalloonAdapter = new MarkerBalloonAdapter(getLayoutInflater());

    if (isPlaySevicesAvailable) {
      googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
      googleMap.setMyLocationEnabled(true);
      googleMap.setInfoWindowAdapter(markerBalloonAdapter);
      googleMap.setOnMarkerClickListener(this);
    }

    gpsManager = new GpsManager(this);
    gpsManager.isGpsEnabled();

    intentFilter = new IntentFilter();
    intentFilter.addAction(intentActionGps);

    checkGpsStatusAndSetText();
    initAlertDialog();
    initGpsStatusReceiver();
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {

    switch (item.getItemId()) {
    case R.id.action_download:
      if (Network.isNetworkAvailable(this))
        startDownloadAsyncTask();
      else
        showAlertDialog();
      break;
    }
    return true;
  }

  private void startDownloadAsyncTask() {
    if (downloadAsyncTask == null) {
      downloadAsyncTask = new DownloadPinAsyncTask();
      downloadAsyncTask.setOnDownloadManagerListener(this);
      downloadAsyncTask.execute("");
    }
  }

  private void initAlertDialog() {
    alertDialogBuilder = new AlertDialog.Builder(this);
    alertDialogBuilder.setMessage(getString(R.string.no_connection));
    alertDialogBuilder.setTitle(getString(R.string.no_connection_short));
    alertDialogBuilder.setCancelable(true);
    alertDialogBuilder.setNeutralButton("OK", null);
    alertDialog = alertDialogBuilder.create();
  }

  private void initGpsStatusReceiver() {
    gpsStatusReceiver = new BroadcastReceiver() {
      @Override
      public void onReceive(Context context, Intent intent) {
        checkGpsStatusAndSetText();
      }
    };
  }

  private void showAlertDialog() {
    if (alertDialog != null && !alertDialog.isShowing())
      alertDialog.show();
  }

  @Override
  protected void onResume() {
    registerReceiver(gpsStatusReceiver, intentFilter);
    gpsManager.setGpsResumed();
    gpsManager.getLocation();
    super.onResume();
  }

  @Override
  protected void onPause() {
    unregisterReceiver(gpsStatusReceiver);
    gpsManager.setGpsPaused();
    gpsManager.stopGPS();
    super.onPause();
  }

  @Override
  protected void onDestroy() {
    gpsManager.removeObserver();
    super.onDestroy();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.main, menu);
    return true;
  }

  @Override
  public void downloadResult(String result, DOWNLOAD_ERROR downloadErr) {
    downloadAsyncTask = null;
    if (downloadErr == DOWNLOAD_ERROR.ER_OK) {
      json.parse(result);
      if ((jsonError = json.getError()) == JSON_ERROR.ER_OK)
        addPinAndClearMap(json.getPin());
      else
        Toast.makeText(this, formatters.translateErrorCode(json.getError()), Toast.LENGTH_SHORT).show();
    } else
      Toast.makeText(this, formatters.translateErrorCode(downloadErr), Toast.LENGTH_SHORT).show();
  }

  private void addPinAndClearMap(Pin pin) {
    if (isPlaySevicesAvailable && googleMap != null) {
      googleMap.clear();
      googleMap.addMarker(new MarkerOptions().position(new LatLng(pin.location.latitude, pin.location.longitude)).title(pin.text));
    }
  }

  @Override
  public void update(Observable arg0, Object obj) {
    if (jsonError != null && jsonError == JSON_ERROR.ER_OK)
      tvDistance.setText(formatters.formatDistance(gpsManager.getDistanceBetween(json.getPin().location.latitude, json.getPin().location.longitude)));
    else
      tvDistance.setText(getString(R.string.cant_determinate_distance));
  }

  private void checkGpsStatusAndSetText() {
    if (!gpsManager.isGpsEnabled())
      tvDistance.setText(getString(R.string.gps_turned_off));
    else
      tvDistance.setText(getString(R.string.getting_fix));
  }

  @Override
  public boolean onMarkerClick(Marker marker) {
    markerBalloonAdapter.downloadBitmap(json.getPin());
    return false;
  }

  @Override
  public void onConfigurationChanged(Configuration newConfig) {
    super.onConfigurationChanged(newConfig);
  }
}
