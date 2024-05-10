package edu.byuh.cis.cs203.grid.ui;

import android.content.Context;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.preference.SwitchPreference;
import android.widget.CheckBox;

import java.net.ConnectException;

import edu.byuh.cis.cs203.grid.R;

public class Prefs extends PreferenceActivity {

    @Override
    public void onCreate(Bundle b) {
        //create preference page
        super.onCreate(b);
        PreferenceScreen ps = getPreferenceManager().createPreferenceScreen(this);

        //music switch preference
        SwitchPreference music = new SwitchPreference(this);
        music.setTitle(R.string.musicSwitchPreference_title);
        music.setSummaryOn(R.string.musicSwitchPreference_summaryOn);
        music.setSummaryOff(R.string.musicSwitchPreference_summaryOff);
        music.setDefaultValue(true);
        music.setKey("MUSIC_PREF");

        //speed list preference
        ListPreference speed = new ListPreference(this);
        speed.setTitle(R.string.speedListPreference_title);
        speed.setSummary(R.string.speedListPreference_summary);
        speed.setKey("SPEED_PREF");
        speed.setEntries(R.array.speedListPreference_entries);
        String[] values = {"50", "100", "300"};
        speed.setEntryValues(values);
        speed.setDefaultValue("100");

        //sound effect checkbox preference
        CheckBoxPreference soundEffect = new CheckBoxPreference(this);
        soundEffect.setTitle(R.string.soundEffectCheckBoxPreference_title);
        soundEffect.setSummaryOn(R.string.musicSwitchPreference_summaryOn);
        soundEffect.setSummaryOff(R.string.soundEffectCheckBoxPreference_summaryOff);
        soundEffect.setChecked(true);
        soundEffect.setKey("EFFECT_PREF");

        ps.addPreference(soundEffect);
        ps.addPreference(music);
        ps.addPreference(speed);
        setPreferenceScreen(ps);
    }

    /**
     * getter for Sound
     * @param c
     * @return
     */
    public static boolean soundOn(Context c) {
        return PreferenceManager.getDefaultSharedPreferences(c).getBoolean("MUSIC_PREF", true);
    }

    /**
     * getter for Speed
     * @param c
     * @return
     */
    public static int getSpeed(Context c) {
        return Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(c).getString("SPEED_PREF", "100"));
    }

    /**
     * getter for Sound Effect
     * @param c
     * @return
     */
    public static boolean soundEffectOn(Context c) {
        return PreferenceManager.getDefaultSharedPreferences(c).getBoolean("EFFECT_PREF", true);
    }
}
