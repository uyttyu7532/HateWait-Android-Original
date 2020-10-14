package com.example.hatewait.storeinfo

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.example.hatewait.R

class StoreInfoSettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }
}