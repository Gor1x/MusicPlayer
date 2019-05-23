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

class ChooseFolderActivity : AppCompatActivity(), FilesAdapter.OnListClick {


    var filesList: ArrayList<FileName> = ArrayList();
    private var adapter = FilesAdapter(filesList, this);
    private var startPath = Environment.getExternalStorageDirectory().path;
    private var path = Environment.getExternalStorageDirectory().path;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_folder)
        recyclerView.layoutManager = LinearLayoutManager(applicationContext);
        recyclerView.adapter = adapter;
        refreshData();
    }

    private fun refreshData() {
        val directory = File(path);
        var files: Array<out File>;
        pathTextView.text = path;

        files = try {
            directory.listFiles();
        } catch (e: Exception) {
            Array(0) { File("") };
        }

        filesList.clear();
        for (i in 0 until files.size) {
            filesList.add(FileName(files[i].name, files[i].isDirectory));
        }
        adapter.setData(filesList);
        adapter.notifyDataSetChanged();
    }

    override fun onListClickListener(index: Int) {
        path = path + '/' + filesList[index].name;
        refreshData();
    }

    override fun onBackPressed() {
        if (path == startPath) {
            super.onBackPressed()
        } else {
            path = File(path).parent;
            refreshData()
        }
    }

}
