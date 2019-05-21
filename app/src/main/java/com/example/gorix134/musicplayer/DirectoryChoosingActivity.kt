package com.example.gorix134.musicplayer

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class DirectoryChoosingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        choosingDirectory.setOnClickListener {
            val folderDialog: OpenFolderDialog = OpenFolderDialog(this);
            folderDialog.show();
        }
    }


}
