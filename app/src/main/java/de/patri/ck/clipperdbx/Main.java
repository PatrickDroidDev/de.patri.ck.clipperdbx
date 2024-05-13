package de.patri.ck.clipperdbx;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import de.patri.ck.clipperdbx.app.Config;
import de.patri.ck.clipperdbx.common.utils.PermissionUtils;
import de.patri.ck.clipperdbx.common.utils.pAppUtils;
import de.patri.ck.clipperdbx.fragmente.ClipperItem;
import de.patri.ck.clipperdbx.fragmente.ClipperSet;
import de.patri.ck.clipperdbx.fragmente.ClipperDetails;
import de.patri.ck.clipperdbx.fragmente.Login;
import de.patri.ck.clipperdbx.fragmente.Start;
import de.patri.ck.clipperdbx.preferences.Settings;
import de.patri.ck.clipperdbx.common.listener.OnClipperSelectedListener;

public class Main extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener, NavigationView.OnNavigationItemSelectedListener, OnClipperSelectedListener {

  CoordinatorLayout rootLayout;

  AppBarLayout appBarLayout;
  CollapsingToolbarLayout collapsingToolbar;
  Toolbar toolbar;

  DrawerLayout drawer;
  ActionBarDrawerToggle toggle;
  NavigationView nView;

  boolean fab_status = false;
  FloatingActionButton fabMenu, fabLogin, fabExit;
  Animation show_fabLogin, hide_fabLogin, show_fabExit, hide_fabExit;

  private Menu menu;
  private static long back_pressed;

  @Override
  protected void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    requestPermissions();

    if(pAppUtils.isMarshmellow()) {
      getWindow().setStatusBarColor(getResources().getColor(R.color.transparent, this.getTheme()));
    } else if(pAppUtils.isLollipop()) {
      getWindow().setStatusBarColor(getResources().getColor(R.color.transparent));
    }
    rootLayout = findViewById(R.id.rootLayout);

    appBarLayout = findViewById(R.id.appBar);
    appBarLayout.addOnOffsetChangedListener(this);

    collapsingToolbar = findViewById(R.id.collapsingToolbar);
    collapsingToolbar.setTitleEnabled(true);

    toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    ActionBar actionbar = getSupportActionBar();
    if(actionbar != null) {
      actionbar.setDisplayShowTitleEnabled(true);
      actionbar.setDisplayHomeAsUpEnabled(true);
      actionbar.setHomeButtonEnabled(true);
      actionbar.setSubtitle(getString(R.string.app_name));
    }

    drawer = findViewById(R.id.drawer_layout);
    toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.open, R.string.close);
    toggle.setDrawerIndicatorEnabled(true);
    if(drawer != null) {
      drawer.addDrawerListener(toggle);
    }
    toggle.syncState();

    nView = findViewById(R.id.nav_view);
    nView.setNavigationItemSelectedListener(this);

    fabMenu  = findViewById(R.id.fab);
    fabLogin = findViewById(R.id.fabLogin);
    fabExit  = findViewById(R.id.fabExit);

    show_fabLogin = AnimationUtils.loadAnimation(getApplication(), R.anim.fab2_show);
    hide_fabLogin = AnimationUtils.loadAnimation(getApplication(), R.anim.fab2_hide);
    show_fabExit  = AnimationUtils.loadAnimation(getApplication(), R.anim.fab1_show);
    hide_fabExit  = AnimationUtils.loadAnimation(getApplication(), R.anim.fab1_hide);

    fabMenu.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if(!fab_status) {
          expandFAB();
          fab_status = true;
        } else {
          hideFAB();
          fab_status = false;
        }
      }
    });
    fabLogin.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        selectFragment(Config.APP_LOGiN);
      }
    });
    fabExit.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if(fab_status) {
          hideFAB();
		  fab_status = false;
        }
        finish();
      }
    });
    Fragment fragment = new Start();
    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
    ft.setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out);
    ft.disallowAddToBackStack().add(R.id.content_main, fragment, fragment.toString()).commit();
  }

  private void requestPermissions() {
    boolean requestPermission = PermissionUtils.requestPermissions(this);
    if(requestPermission) {
      ActivityCompat.requestPermissions(Main.this, Config.PERMISSIONS, Config.REQ_PERMS);
    } else {
      ActivityCompat.requestPermissions(this, Config.PERMISSIONS, Config.REQ_PERMS);
    }
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    if(requestCode == (Config.REQ_PERMS)) {
      if(PermissionUtils.verifyPermissions(grantResults)) {
        Toast.makeText(this, getString(R.string.permission_available), Toast.LENGTH_SHORT).show();
      } else {
        // pAppUtils.snack(this, vi, getString(R.string.permissions_not_granted));
      }
    } else {
      super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
  }

  private CollapsingToolbarLayoutState state;
  private enum CollapsingToolbarLayoutState { EXPANDED, COLLAPSED, INTERNEDIATE }

  @Override
  public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

    boolean isShow = false;
    int scrollRange = -1;

    if(scrollRange == -1) {
      scrollRange = appBarLayout.getTotalScrollRange();
    }
    if(verticalOffset == 0) {
      if(state != CollapsingToolbarLayoutState.EXPANDED) {
        state = CollapsingToolbarLayoutState.EXPANDED;
      }
      collapsingToolbar.setExpandedTitleGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
      toggle.setDrawerIndicatorEnabled(true);
    } else if(Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
      if(state != CollapsingToolbarLayoutState.COLLAPSED) {
        state = CollapsingToolbarLayoutState.COLLAPSED;
        toggle.setDrawerIndicatorEnabled(true);
       }
    } else {
      if(state != CollapsingToolbarLayoutState.INTERNEDIATE) {
        collapsingToolbar.setTitle("");
        state = CollapsingToolbarLayoutState.INTERNEDIATE;
        toggle.setDrawerIndicatorEnabled(false);
      }
    }
    if(scrollRange + verticalOffset == 0) {
      isShow = true;
      zeigeMenu(R.id.action_suche, true);
    } else if(isShow) {
      isShow = false;
      zeigeMenu(R.id.action_suche, false);
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    this.menu = menu;
    getMenuInflater().inflate(R.menu.main, menu);
    zeigeMenu(R.id.action_suche, false);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();
    if(id == android.R.id.home) {
      if(drawer.isDrawerOpen(GravityCompat.START)) {
        drawer.closeDrawer(GravityCompat.START);
      } else {
        drawer.openDrawer(GravityCompat.START);
      }
      onBackPressed();
    }
    return super.onOptionsItemSelected(item);
  }

  public void zeigeMenu(int id, boolean action) {
    MenuItem item = menu.findItem(id);
    item.setVisible(action);
  }

  @Override
  public boolean onNavigationItemSelected(@NonNull MenuItem item) {
    int selectedID = item.getItemId();
    if(selectedID == R.id.nav_start) {
      selectFragment(Config.APP_START);
    } else if(selectedID == R.id.nav_items) {
      selectFragment(Config.APP_CLPR);
    } else if(selectedID == R.id.nav_sets) {
      selectFragment(Config.APP_SETS);
    } else if(selectedID == R.id.nav_about) {
      selectFragment(Config.APP_ABOUT);
    } else if(selectedID == R.id.nav_settings) {
      startActivity(new Intent(this, Settings.class));
    } else if(selectedID == R.id.fabLogin) {
      selectFragment(Config.APP_LOGiN);
    } else if(selectedID == 88) {
      selectFragment(Config.APP_DETAiL);
    } else if(selectedID == R.id.nav_exit) {
      finish();
    }
    item.setCheckable(true);
    item.setChecked(true);
    drawer.closeDrawer(GravityCompat.START);

    return true;
  }

  public void selectFragment(int selectedID) {

    String subtitle   = null;
    Fragment fragment = null;

    if(selectedID == Config.APP_START) {
      fragment = new Start();              subtitle = Config.TITLE_START;
    } else if(selectedID == Config.APP_CLPR) {
      fragment = new ClipperItem();        subtitle = Config.TITLE_iTEMS;
    } else if(selectedID == Config.APP_SETS) {
      fragment = new ClipperSet();         subtitle = Config.TITLE_SETS;
    } else if(selectedID == Config.APP_CONFiG) {
      fragment = new Fragment();           subtitle = Config.TITLE_CONFiG;
    } else if(selectedID == Config.APP_ABOUT) {
      fragment = new Fragment();           subtitle = Config.TITLE_ABOUT;   pAppUtils.about(this);
    } else if(selectedID == Config.APP_LOGiN) {
      fragment = new Login();              subtitle = Config.TITLE_LOGiN;
    } else if(selectedID == Config.APP_DETAiL) {
      fragment = new ClipperDetails();  subtitle = Config.TITLE_DETAiL;
    }
    if(fab_status) {
      hideFAB();
      fab_status = false;
    }
    if(subtitle != null) { setSubTitle(subtitle); }
    if(fragment != null) { selectFragment(fragment); }
  }

  public void selectFragment(Fragment fragment) {
    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
    ft.setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out);
    ft.addToBackStack(fragment.toString()).replace(R.id.content_main, fragment, fragment.toString()).commit();
  }

  @Override
  public void onBackPressed() {
    if(fab_status) {
      hideFAB();
      fab_status = false;
    }
    if(drawer.isDrawerOpen(GravityCompat.START)) {
      drawer.closeDrawer(GravityCompat.START);
    }
    if(getSupportFragmentManager().getBackStackEntryCount() >= 1) {
      getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    } else {
      if(back_pressed + 2000 > System.currentTimeMillis()) {
        finish();
      } else {
        Toast.makeText(getApplicationContext(), "Nochmal zum Beenden!", Toast.LENGTH_SHORT).show();
        back_pressed = System.currentTimeMillis();
      }
      super.onBackPressed();
    }
  }

  private void expandFAB() {
    FrameLayout.LayoutParams layoutParamsExit  = (FrameLayout.LayoutParams) fabExit.getLayoutParams();
    FrameLayout.LayoutParams layoutParamsLogin = (FrameLayout.LayoutParams) fabLogin.getLayoutParams();
    layoutParamsExit.rightMargin   += (int) (fabExit.getWidth()   * 1.7);
    layoutParamsExit.bottomMargin  += (int) (fabExit.getHeight()  * 0.25);
    layoutParamsLogin.rightMargin  += (int) (fabLogin.getWidth()  * 1.5);
    layoutParamsLogin.bottomMargin += (int) (fabLogin.getHeight() * 1.5);
    fabExit.setLayoutParams(layoutParamsExit);
    fabLogin.setLayoutParams(layoutParamsLogin);
    fabExit.startAnimation(show_fabExit);
    fabLogin.startAnimation(show_fabLogin);
    fabExit.setClickable(true);
    fabLogin.setClickable(true);
  }

  private void hideFAB() {
    FrameLayout.LayoutParams layoutParamsExit  = (FrameLayout.LayoutParams) fabExit.getLayoutParams();
    FrameLayout.LayoutParams layoutParamsLogin = (FrameLayout.LayoutParams) fabLogin.getLayoutParams();
    layoutParamsExit.rightMargin   -= (int)(fabExit.getWidth()   * 1.7);
    layoutParamsExit.bottomMargin  -= (int)(fabExit.getHeight()  * 0.25);
    layoutParamsLogin.rightMargin  -= (int)(fabLogin.getWidth()  * 1.5);
    layoutParamsLogin.bottomMargin -= (int)(fabLogin.getHeight() * 1.5);
    fabExit.setLayoutParams(layoutParamsExit);
    fabLogin.setLayoutParams(layoutParamsLogin);
    fabExit.startAnimation(hide_fabExit);
    fabLogin.startAnimation(hide_fabLogin);
    fabExit.setClickable(false);
    fabLogin.setClickable(false);
  }

  public void setSubTitle(String subtitle) {
    collapsingToolbar = findViewById(R.id.collapsingToolbar);
    collapsingToolbar.setTitle(subtitle);
  }

  @Override
  public void onClipperSelect(int _id) {
    Bundle args = new Bundle();
    ClipperDetails csd = new ClipperDetails();
    args.putInt("_id", _id);
    csd.setArguments(args);
    selectFragment(csd);
  }

  @Override
  protected void onPostCreate(Bundle savedInstanceState) {
    super.onPostCreate(savedInstanceState);
    toggle.syncState();
  }
    
  @Override
  public void onConfigurationChanged(Configuration newConfig) {
    super.onConfigurationChanged(newConfig);
    toggle.onConfigurationChanged(newConfig);
  }

}
