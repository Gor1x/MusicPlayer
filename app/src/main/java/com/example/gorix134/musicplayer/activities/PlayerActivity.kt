package com.example.gorix134.musicplayer.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.gorix134.musicplayer.R

class PlayerActivity : AppCompatActivity() {

    private val APP_PREFERENCES = "APP_PREFERENCES";
    private val PATH_KEY = "PATH_KEY";

    private var currentPath = ""; //Empty if it is a first run


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        checkPreferencesForFolder();
    }

    private fun checkPreferencesForFolder() {
        val preferences = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
        currentPath = preferences.getString(PATH_KEY, "");
        if (currentPath == "") {
            val intent = Intent(applicationContext, ButtonActivity::class.java);
            startActivity(intent);
        }
    }
}
