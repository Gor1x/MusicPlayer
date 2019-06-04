package com.example.gorix134.musicplayer.activities

import APP_PREFERENCES
import PATH_KEY
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import com.example.gorix134.musicplayer.R
import com.example.gorix134.musicplayer.activities.filesystem.ChooseFolderActivity
import kotlinx.android.synthetic.main.button_activity.*

class ButtonActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.button_activity)
        choosingDirectory.setOnClickListener {
            val intent = Intent(applicationContext, ChooseFolderActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        if (isCorrectPath()) {
            finish()
        }
    }

    private fun isCorrectPath(): Boolean {
        return getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE).getString(PATH_KEY, "") != ""
    }

    override fun onBackPressed() {
        Snackbar.make(constraintButtonActivity, "Вы не можете вернуться, не выбрав папку", Snackbar.LENGTH_SHORT)
            .show()
    }
}
