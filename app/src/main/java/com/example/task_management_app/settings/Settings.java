package com.example.task_management_app.settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragment;
import androidx.preference.PreferenceFragmentCompat;

import com.example.task_management_app.R;

public class Settings extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        getChildFragmentManager().beginTransaction()
                .replace(R.id.frameLayout, new SettingsFragment())
                .commit();
        return view;

    }

    public static class SettingsFragment extends PreferenceFragmentCompat {


        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            addPreferencesFromResource(R.xml.preference);
            Preference dark_pref = findPreference("checkbox");
            Preference notif_pref = findPreference("checkbox2");
            dark_pref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    if (newValue.toString().equals("true")) {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                        //saveNightModeState(true);
                        //recreate();
                        Toast.makeText(getContext(), "dark mode on",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                        //saveNightModeState(false);
                        //recreate();
                        Toast.makeText(getContext(), "dark mode off",
                                Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }
            });

            notif_pref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference preference) {
                    Intent intent = new Intent();
                    intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
                    intent.putExtra("android.provider.extra.APP_PACKAGE", "com.example.task_management_app");

                    startActivity(intent);
                    Toast.makeText(getContext(), "Notification enabled",
                            Toast.LENGTH_SHORT).show();
                    return true;
                }
            });
        }
    }

}

