package com.example.task_management_app.settings;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.provider.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.preference.ListPreference;
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
        SharedPreferences shpref;
        SharedPreferences.Editor myedit;
        int REQUEST_CODE_ALERT_RINGTONE;

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            addPreferencesFromResource(R.xml.preference);
            Preference dark_pref = findPreference("checkbox");
            Preference for_pref = findPreference("time_format");
            Preference sett_pref = findPreference("settings");
            shpref=getActivity().getApplicationContext().getSharedPreferences("Myprefs" , Context.MODE_PRIVATE);
            myedit = shpref.edit();
            dark_pref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    if (newValue.toString().equals("true")) {
                        myedit.putBoolean("dark_mode",true);
                        myedit.apply();
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                        //saveNightModeState(true);
                        //recreate();
                        Toast.makeText(getContext(), "dark mode on",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        myedit.putBoolean("dark_mode",false);
                        myedit.apply();
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                        //saveNightModeState(false);
                        //recreate();
                        Toast.makeText(getContext(), "dark mode off",
                                Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }
            });
            for_pref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    if (newValue.toString().equals("true")) {
                        myedit.putBoolean("for_mode",true);
                        myedit.apply();
                    } else {
                        myedit.putBoolean("for_mode",false);
                        myedit.apply();
                    }
                    return true;
                }
            });
            final ListPreference listPreference = (ListPreference) findPreference("not_time");
            if(listPreference.getValue()==null) {
                listPreference.setValueIndex(0);
            }
            listPreference.setSummary(listPreference.getEntry().toString());
            listPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    listPreference.setValue(newValue.toString());
                    preference.setSummary(listPreference.getEntry());
                    myedit.putInt("time_reminder",Integer.parseInt(newValue.toString()));
                    myedit.apply();
                    System.out.println(Integer.parseInt(newValue.toString()));
                    return true;
                }
            });
            sett_pref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Intent intent = new Intent();
                    intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
                    intent.putExtra("android.provider.extra.APP_PACKAGE", "com.example.task_management_app");
                    startActivity(intent);
                   return true;
                }
            });

        }



    }

}

