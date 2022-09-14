package com.example.globochat

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.navigation.fragment.NavHostFragment
import androidx.preference.*

class SettingsFragment : PreferenceFragmentCompat() {

    companion object {
        private const val TAG = "SettingsFragment"
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings, rootKey)

        val dataStore = DataStore()

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

        val notificationPref =
            findPreference<SwitchPreferenceCompat>(getString(R.string.key_new_msg_notif))
        notificationPref?.summaryProvider =
            Preference.SummaryProvider<SwitchPreferenceCompat> { switchPref ->
                if (switchPref.isChecked) {
                    "Status: ON"
                } else {
                    "Status: OFF"
                }
            }

        notificationPref?.preferenceDataStore = dataStore

        // Get the value as follows
        val isNotifEnabled = dataStore.getBoolean(getString(R.string.key_new_msg_notif), false)
    }

    // Note this class disables shared prefs for the preferences whch are using it
    inner class DataStore : PreferenceDataStore() {


        override fun getBoolean(key: String?, defValue: Boolean): Boolean {
            if (key == "key_new_msg_notif") {
                // Save wherever you want to
                Log.i(TAG, "getBoolean in data store for key $key has been executed")
            }

            return defValue
        }

        override fun putBoolean(key: String?, value: Boolean) {
            if (key == "key_new_msg_notif") {
                // Save wherever you want to
                Log.i(TAG, "putBoolean in data store for key $key: with new value $value ")
            }
        }
    }
}