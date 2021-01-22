package com.example.task_management_app.settings;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            addPreferencesFromResource(R.xml.preference);
            Preference dark_pref = findPreference("checkbox");
            Preference for_pref = findPreference("time_format");
            Preference vib_pref = findPreference("vibrate");
            shpref=getActivity().getApplicationContext().getSharedPreferences("Myprefs" , Context.MODE_PRIVATE);
            final SharedPreferences.Editor myedit = shpref.edit();
            dark_pref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    if (newValue.toString().equals("true")) {
                        myedit.putBoolean("dark_mode",true);
                        myedit.apply();
                        //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                        //saveNightModeState(true);
                        //recreate();
                        Toast.makeText(getContext(), "dark mode on",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        myedit.putBoolean("dark_mode",false);
                        myedit.apply();
                        //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
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
            vib_pref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    if (newValue.toString().equals("true")) {
                        myedit.putBoolean("vib_mode",true);
                        myedit.apply();
                    } else {
                        myedit.putBoolean("vib_mode",false);
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
            final ListPreference listPreference3 = (ListPreference) findPreference("not_duration");
            if(listPreference.getValue()==null) {
                listPreference.setValueIndex(0);
            }
            listPreference3.setSummary(listPreference3.getEntry().toString());
            listPreference3.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    listPreference3.setValue(newValue.toString());
                    preference.setSummary(listPreference3.getEntry());
                    return true;
                }
            });

        }
    }

}

