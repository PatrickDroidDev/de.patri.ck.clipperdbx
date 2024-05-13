package de.patri.ck.clipperdbx.common.utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;

public abstract class PermissionUtils {

  public static boolean verifyPermissions(int[] grantResults) {
    if(grantResults.length < 1){
      return false;
    }
    for(int result : grantResults) {
      if(result != PackageManager.PERMISSION_GRANTED) {
        return false;
      }
    }
    return true;
  }

  public static boolean requestPermissions(Activity activity) {

    boolean requestPermission;

    if(ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_PHONE_STATE) || ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_EXTERNAL_STORAGE) || ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
      requestPermission =  true;
    } else {
      // Contact permissions have not been granted yet. Request them directly.

      requestPermission =  true;
    }
    return requestPermission;
  }

}
