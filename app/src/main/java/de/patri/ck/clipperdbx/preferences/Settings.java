package de.patri.ck.clipperdbx.preferences;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.view.MenuItem;

import de.patri.ck.clipperdbx.R;

public class Settings extends AppCompatPreference {
  
  private static final String TAG = Settings.class.getSimpleName();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if(getSupportActionBar() != null) {
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    getFragmentManager().beginTransaction().replace(android.R.id.content, new MainPreferenceFragment()).commit();
  }

  public static class MainPreferenceFragment extends PreferenceFragment {
    @Override
    public void onCreate(final Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      addPreferencesFromResource(R.xml.pref_main);

      // bindPreferenceSummaryToValue(findPreference(pApp.PREF_KEY_THEME));
      // bindPreferenceSummaryToValue(findPreference(getString(R.string.pref_summary_theme)));
    }
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if(item.getItemId() == android.R.id.home) {
      super.onBackPressed();
      // NavUtils.navigateUpFromSameTask(this);
    }
    return super.onOptionsItemSelected(item);
  }

  private static void bindPreferenceSummaryToValue(Preference preference) {
    preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);
    sBindPreferenceSummaryToValueListener.onPreferenceChange(preference, PreferenceManager.getDefaultSharedPreferences(preference.getContext()).getString(preference.getKey(), ""));
  }
  
  private static final Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
      String stringValue = newValue.toString();

      if(preference instanceof ListPreference) {
        ListPreference listPreference = (ListPreference) preference;
        int index = listPreference.findIndexOfValue(stringValue);
        preference.setSummary(index >= 0 ? listPreference.getEntries()[index] : null);
      } else {
        preference.setSummary(stringValue);
      }
      return true;
    }
  };

}
