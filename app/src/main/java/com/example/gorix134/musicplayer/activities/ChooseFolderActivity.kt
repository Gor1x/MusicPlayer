package com.example.gorix134.musicplayer.activities

import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.example.gorix134.musicplayer.FileName
import com.example.gorix134.musicplayer.FilesAdapter
import com.example.gorix134.musicplayer.R
import kotlinx.android.synthetic.main.activity_choose_folder.*
import java.io.File

class ChooseFolderActivity : AppCompatActivity() {

    public var filesList: ArrayList<FileName> = ArrayList();
    private var adapter = FilesAdapter(filesList);
    private var path = Environment.getExternalStorageDirectory().path;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_folder)
        recyclerView.layoutManager = LinearLayoutManager(applicationContext);
        recyclerView.adapter = adapter;
        refreshData();
    }

    fun refreshData() {
        val directory = File(path);
        val files: Array<out File> = directory.listFiles();
        for (i in 0 until files.size) {
            filesList.add(FileName(files[i].name, i));
        }
        adapter.setData(filesList);
        adapter.notifyDataSetChanged();
    }


}
