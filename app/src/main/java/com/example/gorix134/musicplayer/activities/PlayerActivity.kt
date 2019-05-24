package com.example.gorix134.musicplayer.activities

import APP_PREFERENCES
import PATH_KEY
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.example.gorix134.musicplayer.R
import kotlinx.android.synthetic.main.activity_player.*
import kotlinx.android.synthetic.main.bottom_sheet.*


class PlayerActivity : AppCompatActivity() {


    private var currentPath = "" //Empty if it is a first run

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
        val bottomBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomBehavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {

            override fun onStateChanged(bottomSheet: View, newState: Int) {

            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                cardView.animate().scaleX(1F - slideOffset).scaleY(1F - slideOffset).setDuration(0).start()
            }
        })

        cardView.setOnClickListener {
            bottomBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
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
