package de.patri.ck.clipperdbx.common.utils;

import android.app.Activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.content.pm.PackageManager;
import android.content.pm.PackageInfo;
import android.text.SpannableStringBuilder;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.text.method.LinkMovementMethod;
import android.app.AlertDialog;
import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import de.patri.ck.clipperdbx.Main;
import de.patri.ck.clipperdbx.R;

public final class pAppUtils {
  
  private static final String TAG = "pAppUtils";
	
  private static Context context;

  public pAppUtils(Context context) {
    pAppUtils.context = context;
  }
  
  public static boolean isNetworkConnected() {
    ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
  }

  public static boolean isNumeric(String str) { 
    try {  
      Double.parseDouble(str);  
      return true;
    } catch(NumberFormatException e) {  
      return false;  
    }  
  }
  
  public String getDateTime() {
	SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
	Date date = new Date();

	return dateFormat.format(date);
  }
	
  
  public static void log2local(Exception e) {
	  
	SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
	Date date = new Date();
    
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);
    e.printStackTrace(pw);
    String text = sw.toString();
	FileUtil.writeFile(FileUtil.getExternalCacheDir() + File.separator + "_logcat_"+dateFormat.format(date)+".txt", text);
  }

  public static String formatFileSize(long size) {
    String hrSize = null;
    double b = size;
    double k = size/1024.0;
    double m = ((size/1024.0)/1024.0);
    double g = (((size/1024.0)/1024.0)/1024.0);
    double t = ((((size/1024.0)/1024.0)/1024.0)/1024.0);

    DecimalFormat dec = new DecimalFormat("0.00");
    if ( t>1 ) {
      hrSize = dec.format(t).concat(" TB");
    } else if ( g>1 ) {
      hrSize = dec.format(g).concat(" GB");
    } else if ( m>1 ) {
      hrSize = dec.format(m).concat(" MB");
    } else if ( k>1 ) {
      hrSize = dec.format(k).concat(" KB");
    } else {
      hrSize = dec.format(b).concat(" Bytes");
    }
    return hrSize;
	}
  
  public static boolean isLollipop() {
    return android.os.Build.VERSION.SDK_INT >= 21;
  }
  public static boolean isMarshmellow() {
    return android.os.Build.VERSION.SDK_INT >= 23;
  }
  
  public static void snack(Context context, View view, String msg) {
    Snackbar snack = Snackbar.make(view, msg, Snackbar.LENGTH_LONG);
    View vSnack = snack.getView();
    vSnack.setBackgroundColor(ContextCompat.getColor(context, R.color.weiss));
    TextView tvSnack = vSnack.findViewById(com.google.android.material.R.id.snackbar_text);
    tvSnack.setTextColor(ContextCompat.getColor(context, R.color.black));
    snack.show();
  }
  
  public static void about(Main act) {
	  FragmentManager fm = act.getSupportFragmentManager();
	  FragmentTransaction ft = fm.beginTransaction();
	  Fragment prev = fm.findFragmentByTag(TAG);
	  if(prev != null) { ft.remove(prev); }
	    ft.addToBackStack(TAG);
	  new AboutDialog().show(ft, TAG);
  }
  
  public static class  AboutDialog extends DialogFragment {

	private static final String VERSION_UNAVAILABLE = "N/A";

	public AboutDialog() { }

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
	  PackageManager pm  = getActivity().getPackageManager();
	  String packageName = getActivity().getPackageName();
	  String versionName;
	  try {
		  PackageInfo info = pm.getPackageInfo(packageName, 0);
		  versionName = info.versionName;
	  } catch(PackageManager.NameNotFoundException e) {
		  versionName = VERSION_UNAVAILABLE;
	  }
	  SpannableStringBuilder aboutBody = new SpannableStringBuilder();
	  aboutBody.append(Html.fromHtml(getString(R.string.about_body, versionName)));

	  LayoutInflater li = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	  View v = li.inflate(R.layout.dialog_about, null);
	  TextView tvAbout = v.findViewById(R.id.tvAbout);
	  tvAbout.setText(aboutBody);
	  tvAbout.setMovementMethod(new LinkMovementMethod());

	  return new AlertDialog.Builder(getActivity()).setTitle(R.string.about).setView(v).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int whichButton) {
		  dialog.dismiss();
		}
	  }).create();
	}
  }

  public static int getStatusBarHeight(Activity context) {
	  int result = 0;
	  int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
	  if(resourceId > 0) {
	    result = context.getResources().getDimensionPixelSize(resourceId);
	  }
	  return result;
  }
	
  public static int getToolbarHeight(Context context) {
    final TypedArray styledAttributes = context.getTheme().obtainStyledAttributes(new int[]{android.R.attr.actionBarSize});
    int toolbarHeight = (int) styledAttributes.getDimension(0, 0);
    styledAttributes.recycle();
    return toolbarHeight;
  }
  
}