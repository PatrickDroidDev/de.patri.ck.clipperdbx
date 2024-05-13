package de.patri.ck.clipperdbx.app;

import android.Manifest;

public class Config {
	
  public static final int REQ_PERMS  = 13;
  public static String[] PERMISSIONS = { Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE };

  public static final int CACHE_SiZE   = 1024 * 1024 * 250;
  public static final String CACHE_DIR = "/img_cache";
  public static final String DATA_DIR  = "data";

  public static final String TITLE_START  = "Startseite";
  public static final String TITLE_iTEMS  = "Einzelne";
  public static final String TITLE_PLATT  = "Platten";
  public static final String TITLE_SETS   = "Collection";
  public static final String TITLE_DETAiL = "Clipper Details";
  public static final String TITLE_ABOUT  = "About App";
  public static final String TITLE_CONFiG = "Einstellungen";
  public static final String TITLE_LOGiN  = "Login";
  public static final String TITLE_KARTE  = "Karte";

  public static final int APP_START   = 0;
  public static final int APP_CLPR    = 1;
  public static final int APP_SETS    = 2;
  public static final int APP_DETAiL  = 5;
  public static final int APP_ABOUT   = 6;
  public static final int APP_CONFiG  = 7;
  public static final int APP_LOGiN   = 8;
  public static final int APP_KARTE   = 9;

  public static final String BASE_URL = "https://pfaucms.bplaced.net";
  public static final String IMG_URL  = "https://pfaucms.bplaced.net/assets/img/clipper";
  public static final String API_URL  = "https://pfaucms.bplaced.net/api";

  public static final String PREF_CLiPPERDB   = "pref_clipperdb";
  public static final String PREF_KEY_THEME   = "pref_key_theme";
  public static final String PREF_KEY_UPDATE  = "pref_key_update";
  public static final String PREF_KEY_PRiVACY = "pref_key_privacy";
  public static final String PREF_KEY_TERMS   = "pref_key_terms";
  public static final String PREF_KEY_FAQ     = "pref_key_faq";

}