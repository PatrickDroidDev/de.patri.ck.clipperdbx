package de.patri.ck.clipperdbx.preferences;

import android.content.Context;
import android.content.SharedPreferences;

public class SettingsManager {

  Context context;

  SettingsManager(Context context) {
    this.context = context;
  }

  public void saveSettings(String mail, String pass) {
	SharedPreferences sharedPreferences = context.getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
    SharedPreferences.Editor editor = sharedPreferences.edit();
    editor.putString("mail", mail);
    editor.putString("pass", pass);
    editor.commit();
  }
  
  public String getEmail() {
    SharedPreferences sharedPreferences = context.getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
    return sharedPreferences.getString("mail", "");
  }
  
  public boolean isUserLogedOut() {
	  
    SharedPreferences sharedPreferences = context.getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);

    boolean isEmailEmpty = sharedPreferences.getString("mail", "").isEmpty();
    boolean isPasswordEmpty = sharedPreferences.getString("pass", "").isEmpty();
  
    return isEmailEmpty || isPasswordEmpty;
  }

}
