package com.example.bach0.hustplant.Setting;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.RequiresApi;
import android.util.DisplayMetrics;

import com.example.bach0.hustplant.MainActivity;
import com.example.bach0.hustplant.R;

import java.util.Locale;

public class SettingFragment extends PreferenceFragment
    implements SharedPreferences.OnSharedPreferenceChangeListener {
  @Override
  public void onCreate(Bundle saveInstanceState) {
    super.onCreate(saveInstanceState);
    addPreferencesFromResource(R.xml.setting_reference);
  }

  @RequiresApi(api = Build.VERSION_CODES.M)
  @Override
  public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
    Preference preference = findPreference(key);

    /* update summary */
    if (key.equals("language")) {
      preference.setSummary(((ListPreference) preference).getEntry());
      Locale myLocale = new Locale(((ListPreference) preference).getValue());
      Resources res = getResources();
      DisplayMetrics dm = res.getDisplayMetrics();
      Configuration conf = res.getConfiguration();
      conf.locale = myLocale;
      res.updateConfiguration(conf, dm);
      Intent refresh = new Intent(this.getActivity(), MainActivity.class);
      startActivity(refresh);
    }
  }

  @Override
  public void onResume() {
    super.onResume();
    getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
  }

  @Override
  public void onPause() {
    getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    super.onPause();
  }
}
