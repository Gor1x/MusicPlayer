package com.example.gorix134.musicplayer.activities.filesystem

import APP_PREFERENCES
import PATH_KEY
import android.content.Context
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.widget.HorizontalScrollView
import com.example.gorix134.musicplayer.R
import kotlinx.android.synthetic.main.activity_choose_folder.*
import java.io.File
import java.util.*


class ChooseFolderActivity : AppCompatActivity(), FilesAdapter.OnListClick {

    private var filesList: ArrayList<FileName> = ArrayList()
    private var adapter = FilesAdapter(filesList, this)
    private var path = "/"
    private var startPath = path
    private lateinit var snackbar: Snackbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_folder)


        snackbar = Snackbar.make(layoutChooseFolder, "Вы не можете выбрать файл", Snackbar.LENGTH_SHORT)
            .setDuration(500)

        recyclerView.layoutManager = LinearLayoutManager(applicationContext)
        recyclerView.adapter = adapter
        refreshData()

        floatingActionButton.setOnClickListener {
            if (isCorrect(path)) {
                val preferences = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
                val editor = preferences.edit()
                editor.putString(PATH_KEY, path).apply()
                finish()
            } else {
                Snackbar.make(
                    layoutChooseFolder,
                    "Вы не можете выбрать директорию без mp3",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun isCorrect(path: String): Boolean {
        val directory = File(path)
        val files = directory.listFiles { file, s ->
            s.toLowerCase().endsWith("mp3")
        }
        return !(files == null || files.isEmpty())
    }

    private fun refreshData() {

        pathTextView.text = path
        scrollView.postDelayed({ scrollView.fullScroll(HorizontalScrollView.FOCUS_RIGHT) }, 100L)
        filesList.clear()

        val files: Array<out File> = File(path).listFiles()

        for (i in 0 until files.size) {
            val fls = files[i].listFiles()

            if (!(fls == null || fls.isEmpty()) || !files[i].isDirectory)
                filesList.add(
                    FileName(
                        files[i].name,
                        files[i].isDirectory,
                        i
                    )
                )
        }

        filesList.sortWith(kotlin.Comparator { a: FileName, b: FileName ->
            when {
                a.isDirectory == b.isDirectory -> a.number - b.number
                a.isDirectory -> -1
                else -> 1
            }
        })

        adapter.setData(filesList)
        adapter.notifyDataSetChanged()
        snackbar.dismiss()

    }


    override fun onListClickListener(index: Int) {

        val newFile = File(path, filesList[index].name)

        if (newFile.isDirectory) {
            val fls: Array<out File?>? = newFile.listFiles()
            if (fls == null || fls.isEmpty()) {
                Snackbar.make(
                    layoutChooseFolder,
                    "Эта папка пуста либо недоступна",
                    Snackbar.LENGTH_SHORT
                ).setDuration(600).show()
            } else {
                path = newFile.path
                refreshData()
            }
        } else {
            snackbar.show()
        }
    }

    override fun onBackPressed() {
        if (path == startPath) {
            super.onBackPressed()
        } else {
            path = File(path).parent
            refreshData()
        }
    }

}
