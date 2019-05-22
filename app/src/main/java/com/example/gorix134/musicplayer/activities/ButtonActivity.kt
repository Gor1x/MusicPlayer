package com.example.gorix134.musicplayer.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.gorix134.musicplayer.R
import kotlinx.android.synthetic.main.button_activity.*

class ButtonActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.button_activity)
        choosingDirectory.setOnClickListener {
            val intent = Intent(applicationContext, ChooseFolderActivity::class.java);
            startActivity(intent);
        }
    }

}
