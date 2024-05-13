package de.patri.ck.clipperdbx.common.listener;

import android.annotation.TargetApi;
import android.os.Build;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;

import com.google.android.material.appbar.AppBarLayout;

import de.patri.ck.clipperdbx.R;

public class ToolbarOffsetListener implements AppBarLayout.OnOffsetChangedListener {

  private final Toolbar mToolbar;
  private float mTargetElevation;
  private final AppCompatActivity mActivity;

  public ToolbarOffsetListener(AppCompatActivity appCompatActivity, Toolbar toolbar) {
	mActivity = appCompatActivity;
	mToolbar = toolbar;
	mTargetElevation = mToolbar.getContext().getResources().getDimension(R.dimen.app_bar_elevation);
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  @Override
  public void onOffsetChanged(AppBarLayout appBarLayout, int offset) {
    offset = Math.abs(offset);
	mTargetElevation = Math.max(mTargetElevation, appBarLayout.getTargetElevation());
	if(offset >= appBarLayout.getTotalScrollRange() - mToolbar.getHeight()) {
	  float flexibleSpace = appBarLayout.getTotalScrollRange() - offset;
	  float ratio = 1 - (flexibleSpace / mToolbar.getHeight());
	  float elevation = ratio * mTargetElevation;
	  setToolbarElevation(elevation);
	} else {
	  setToolbarElevation(0);
	}
  }

  private void setToolbarElevation(float targetElevation) {
	ActionBar supportActionBar = mActivity.getSupportActionBar();
	if(supportActionBar != null) {
	  supportActionBar.setElevation(targetElevation);
	} else if(mToolbar != null) {
	  ViewCompat.setElevation(mToolbar, targetElevation);
	}
  }

}
