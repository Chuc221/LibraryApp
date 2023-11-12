package com.example.libraryapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.libraryapp.R
import com.example.libraryapp.util.Constants
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var navigationController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment

        navigationController = navHostFragment.navController

        if (readLanguageCurrentFromCache() != null){
            if (readLanguageCurrentFromCache().equals("enm")){
                setLocale("enm")
            } else {
                setLocale("")
            }
        }
    }

    private fun readLanguageCurrentFromCache(): String? {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(application)
        return sharedPreferences.getString(Constants.LANGUAGE_CACHE_KEY, "")
    }

    private fun setLocale(lang: String) {
        val mLocale = Locale(lang)
        val resources = resources
        val configuration = resources.configuration
        configuration.locale = mLocale
        resources.updateConfiguration(configuration, resources.displayMetrics)
    }
}