package com.xiaomi.xhome.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.Toast;

import com.miot.api.MiotManager;
import com.miot.common.people.People;
import com.xiaomi.xhome.R;
import com.xiaomi.xhome.XConfig;
import com.xiaomi.xhome.XHomeApplication;

import java.io.File;

public class SettingsActivity extends ActionBarActivity {

    public static final String KEY_ACCOUNT = "key_account";
    public static final String KEY_SPACE = "key_space";
    public static final String TAG = "Settings";

    public static class SettingsFragment extends PreferenceFragment {
        private Handler mHandler = new Handler();
        private Preference.OnPreferenceChangeListener mOnPreferenceChangeListener = new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                if (XConfig.PREF_KEY_THEME.equals(preference.getKey())) {
                    ListPreference list = (ListPreference) preference;
                    onThemeListChanged((String) o, list);
                }
                return false;
            }
        };

        private void onThemeListChanged(String o, ListPreference list) {
            final String value = o;
            list.setSummary(value);
            list.setValue(value);

            AlertDialog.Builder builder = new AlertDialog.Builder(SettingsFragment.this.getActivity());
            final AlertDialog dialog = builder.setTitle(R.string.switching_theme).setCancelable(false).show();
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    XHomeApplication.getInstance().switchTheme(value);
                    Toast.makeText(SettingsFragment.this.getActivity(), R.string.switch_theme_ok, Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }, 50);
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            getPreferenceManager().setSharedPreferencesName(XHomeApplication.SHARED_PREF_NAME);

            // Load the preferences from an XML resource
            addPreferencesFromResource(R.xml.pref_settings);

            Preference pr = findPreference(KEY_ACCOUNT);
            pr.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Intent intent = new Intent(preference.getContext(), AccountManagerActivity.class);
                    startActivity(intent);
                    return true;
                }
            });

            pr = findPreference(KEY_SPACE);
            pr.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Intent intent = new Intent(preference.getContext(), SpacesActivity.class);
                    startActivity(intent);
                    return true;
                }
            });


            File themesPath = new File(XHomeApplication.getInstance().getThemesPath());
//            String[] themes = {"default"};
            String[] themes = themesPath.list();
            String logStr = "found theme files: ";
            for (String t : themes) {
                logStr += t + " ";
            }
            Log.d(TAG, logStr);

            ListPreference theme_list = (ListPreference) findPreference(XConfig.PREF_KEY_THEME);
            theme_list.setEntries(themes);
            theme_list.setEntryValues(themes);
            theme_list.setOnPreferenceChangeListener(mOnPreferenceChangeListener);
            String theme = XHomeApplication.getInstance().getDashboard().getTheme();
            theme_list.setSummary(theme);
            theme_list.setValue(theme);
        }

        @Override
        public void onResume() {
            super.onResume();

            String userId = getString(R.string.account_not_login);

            People people = MiotManager.getPeople();
            if (people != null) {
                userId = people.getUserId();
            }

            String name = XHomeApplication.getInstance().getConfig().getString(XConfig.CONFIG_USER_NAME, null);
            Preference pr = findPreference(KEY_ACCOUNT);
            pr.setSummary(name != null ? name + "  " + userId : userId);

            pr = findPreference(KEY_SPACE);
            pr.setSummary(XHomeApplication.getInstance().getDashboard().getSpace());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }

}
