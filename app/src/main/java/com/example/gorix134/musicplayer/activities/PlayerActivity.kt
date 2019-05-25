package com.example.gorix134.musicplayer.activities

import APP_PREFERENCES
import PATH_KEY
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.design.widget.BottomSheetBehavior
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.example.gorix134.musicplayer.R
import kotlinx.android.synthetic.main.bottom_sheet.*
import kotlinx.android.synthetic.main.player_panel.*


class PlayerActivity : AppCompatActivity() {


    private var currentPath = "" //Empty if it is a first run
    private lateinit var bottomBehavior: BottomSheetBehavior<ConstraintLayout>;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
        initBottomSheet();
    }

    private fun initBottomSheet() {
        bottomBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomBehavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_EXPANDED)
                    playerPanel.alpha = 0F;
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                playerPanel.alpha = 1 - slideOffset;
            }
        })

        if (playerPanel == null) {
            Log.d("BUGS", "it is null");
        }

        playerPanel.addOnLayoutChangeListener { view, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
            bottomBehavior.peekHeight = bottom;
        }
    }

    override fun onResume() {
        super.onResume()
        checkPreferencesForFolder();
    }

    private fun checkPreferencesForFolder() {
        @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
        currentPath = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
            .getString(PATH_KEY, "");

        if (currentPath == "") {
            val intent = Intent(applicationContext, ButtonActivity::class.java)
            startActivity(intent)
        }
    }

}
