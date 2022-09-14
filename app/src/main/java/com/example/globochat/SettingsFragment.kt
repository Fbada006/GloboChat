package com.example.globochat

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.navigation.fragment.NavHostFragment
import androidx.preference.EditTextPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager

class SettingsFragment : PreferenceFragmentCompat() {

    companion object {
        private const val TAG = "SettingsFragment"
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings, rootKey)

        val accSettingsPref = findPreference<Preference>(getString(R.string.key_account_settings))

        accSettingsPref?.setOnPreferenceClickListener {
            val navHostFragment =
                activity?.supportFragmentManager?.findFragmentById(R.id.nav_host_frag) as NavHostFragment
            val navController = navHostFragment.navController
            val action = SettingsFragmentDirections.actionSettingsToAccSettings()
            navController.navigate(action)

            true
        }

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())

        val autoReplyTime = sharedPreferences.getString(getString(R.string.key_auto_reply_time), "")

        Log.i(TAG, "Auto reply time: $autoReplyTime")

        val autoDownload =
            sharedPreferences.getBoolean(getString(R.string.key_auto_download), false)

        Log.i(TAG, "Auto download: $autoDownload")

        val statusPref = findPreference<EditTextPreference>(getString(R.string.key_status))

        // Called before the preference value has changed
        // Used for only one type of  preference at a time
        // Used only in fragment where pref is present
        statusPref?.setOnPreferenceChangeListener { preference, newValue ->
            val newStatus = newValue as String

            if (newStatus.contains("bad")) {
                Toast.makeText(
                    context,
                    "Bad status. PLease read the community guidelines",
                    Toast.LENGTH_SHORT
                ).show()

                false
            } else {
                true  //true accept new value and false reject new value
            }
        }
    }
}