package pl2.lines.example.redexperts.utils;

import pl2.lines.example.redexperts.R;
import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

public class PlayServices {
  static final int REQUEST_CODE_RECOVER_PLAY_SERVICES = 1001;

  private Context context;

  public PlayServices(Context context) {
    this.context = context;
  }

  public boolean checkPlayServices() {
    int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
    if (status != ConnectionResult.SUCCESS) {
      if (GooglePlayServicesUtil.isUserRecoverableError(status)) {
        showErrorDialog(status);
      } else {
        Toast.makeText(context, context.getString(R.string.device_unsupported), Toast.LENGTH_LONG).show();
      }
      return false;
    }
    return true;
  }

  void showErrorDialog(int code) {
    GooglePlayServicesUtil.getErrorDialog(code, (Activity)context, REQUEST_CODE_RECOVER_PLAY_SERVICES).show();
  }
}
