package com.example.gorix134.musicplayer.activities

import APP_PREFERENCES
import PATH_KEY
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.gorix134.musicplayer.R

class PlayerActivity : AppCompatActivity() {


    private var currentPath = "" //Empty if it is a first run

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
    }

    private fun checkPreferencesForFolder() {
        if (getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
                .getString(PATH_KEY, "") == ""
        ) {
            val intent = Intent(applicationContext, ButtonActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        checkPreferencesForFolder()
    }
}
