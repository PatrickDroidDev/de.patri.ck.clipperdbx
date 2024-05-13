package de.patri.ck.clipperdbx;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import de.patri.ck.clipperdbx.common.utils.pAppUtils;

public class Debug extends Activity {

  String[] exceptionType = {"","","","",""};
  String[] errMessage    = new String[] {"","","","",""};

  @Override
  protected void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);
	Intent intent     = getIntent();
	String errMsg     = "";
	String madeErrMsg = "";

	if(intent != null) {
	  errMsg = intent.getStringExtra("error");
	  String[] spilt = errMsg.split("\n");
	  try {
		for(int j = 0; j < exceptionType.length; j++) {
		  if(spilt[0].contains(exceptionType[j])) {
			madeErrMsg = errMessage[j];
			int addIndex = spilt[0].indexOf(exceptionType[j]) + exceptionType[j].length();
			madeErrMsg += spilt[0].substring(addIndex);
			break;
		  }
		}
		if(madeErrMsg.isEmpty()) { madeErrMsg = errMsg; }
	  } catch(Exception e) {
		pAppUtils.log2local(e);
	  }
	}
    AlertDialog.Builder bld = new AlertDialog.Builder(this);
	bld.setTitle("APP ERROR!");
	bld.setMessage(madeErrMsg);
	bld.setNeutralButton("Beende App", new DialogInterface.OnClickListener() {
	  @Override
	  public void onClick(DialogInterface dialog, int which) {
		finish();
	  }
	});
    bld.create().show();
  }

}