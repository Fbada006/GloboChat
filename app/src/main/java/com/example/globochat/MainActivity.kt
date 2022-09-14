package com.example.globochat

import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.preference.PreferenceManager

class MainActivity : AppCompatActivity(), OnSharedPreferenceChangeListener {

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Get NavHost and NavController
        val navHostFrag =
            supportFragmentManager.findFragmentById(R.id.nav_host_frag) as NavHostFragment
        navController = navHostFrag.navController

        // Get AppBarConfiguration
        appBarConfiguration = AppBarConfiguration(navController.graph)

        // Link ActionBar with NavController
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    // Called only after the preference value has changed
    // Used for all preferences
    // Used in any part of the application as long as its active
    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (key == getString(R.string.key_status)) {
            val newStatus = sharedPreferences?.getString(key, "")
            Toast.makeText(this, newStatus, Toast.LENGTH_SHORT).show()
        }

        if (key == getString(R.string.key_auto_reply)) {
            val isAutoReply = sharedPreferences?.getBoolean(key, false)
            if (isAutoReply!!) {
                Toast.makeText(this, "Auto Reply ON", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Auto Reply OFF", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        PreferenceManager.getDefaultSharedPreferences(this)
            .registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        PreferenceManager.getDefaultSharedPreferences(this)
            .unregisterOnSharedPreferenceChangeListener(this)
    }
}
