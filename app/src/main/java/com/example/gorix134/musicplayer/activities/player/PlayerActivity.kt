package com.example.gorix134.musicplayer.activities.player

import APP_PREFERENCES
import PATH_KEY
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.support.constraint.ConstraintLayout
import android.support.design.widget.BottomSheetBehavior
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.example.gorix134.musicplayer.PlayerService
import com.example.gorix134.musicplayer.R
import com.example.gorix134.musicplayer.activities.ButtonActivity
import kotlinx.android.synthetic.main.activity_player.*
import kotlinx.android.synthetic.main.bottom_sheet.*
import java.io.File


class PlayerActivity : AppCompatActivity(), TrackListViewAdapter.TrackListViewAdapterInterface {


    private var playerServiceBinder: PlayerService.PlayerServiceBinder? = null
    private var currentPath = "" //Empty if it is a first run
    private lateinit var bottomBehavior: BottomSheetBehavior<ConstraintLayout>
    private lateinit var callback: MediaControllerCompat.Callback
    private lateinit var serviceConnection: ServiceConnection
    private var mediaController: MediaControllerCompat? = null
    private lateinit var playerService: PlayerService
    private var trackArray = ArrayList<Track>()
    private val adapter = TrackListViewAdapter(trackArray, this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        if (!checkPreferencesForFolder()) {
            return
        }

        trackListView.layoutManager = LinearLayoutManager(applicationContext)
        trackListView.adapter = adapter
        refreshListData()
        initBottomSheet()

        callback = object : MediaControllerCompat.Callback() {
            override fun onPlaybackStateChanged(state: PlaybackStateCompat?) {
                if (state == null)
                    return
                val playing = (state.state == PlaybackStateCompat.STATE_PLAYING)
                statePlayChanged(playing)
            }

            override fun onMetadataChanged(metadata: MediaMetadataCompat?) {
                songPanelName.text = metadata!!.getString(MediaMetadataCompat.METADATA_KEY_TITLE) ?: "Not exist"

            }
        }

        serviceConnection = object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {

                playerServiceBinder = service as PlayerService.PlayerServiceBinder
                try {
                    mediaController =
                        MediaControllerCompat(applicationContext, playerServiceBinder!!.mediaSessionToken)
                    mediaController!!.registerCallback(callback)
                    callback.onPlaybackStateChanged(mediaController!!.playbackState)
                } catch (e: Exception) {
                    mediaController = null
                }

                playerService = playerServiceBinder!!.getService()


            }

            override fun onServiceDisconnected(p0: ComponentName?) {
                playerServiceBinder = null
                if (mediaController != null) {
                    mediaController!!.unregisterCallback(callback)
                    mediaController = null
                }
            }

        }

        bindService(Intent(this, PlayerService::class.java), serviceConnection, Context.BIND_AUTO_CREATE)

        songPanelPlay.setOnClickListener {
            mediaController?.transportControls?.play()
        }

        songPanelNext.setOnClickListener {
            mediaController?.transportControls?.skipToNext()
        }

        songPanelPause.setOnClickListener {
            mediaController?.transportControls?.pause()
        }

    }

    override fun onResume() {
        super.onResume()
        refreshListData()
    }

    private fun refreshListData() {
        val directory = File(currentPath)
        val fileList = directory.listFiles { dir, name ->
            name.toLowerCase().endsWith("mp3")
        }

        trackArray = ArrayList<Track>()
        fileList.forEach {
            trackArray.add(Track(it))
        }

        adapter.setData(trackArray)
        adapter.notifyDataSetChanged()
    }

    override fun onListItemClickListener(position: Int) {
        playerService.playTrackChanged(position)
        showBottomPlayer()
    }

    private fun showBottomPlayer() {
        bottomBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    fun timeToString(currentTime: Int): String {
        return (currentTime / 60).toString() + ":" + normalizing(currentTime % 60)
    }

    private fun normalizing(seconds: Int): String {
        return if (seconds < 10)
            "0$seconds"
        else
            seconds.toString()
    }

    private fun initBottomSheet() {
        bottomBehavior = BottomSheetBehavior.from(bottomSheet as ConstraintLayout)
        bottomBehavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_EXPANDED)
                    playerPanel.alpha = 0F
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                playerPanel.alpha = 1 - slideOffset
            }
        })

        playerPanel.addOnLayoutChangeListener { _, _, top, _, bottom, _, oldTop, oldRight, oldBottom ->
            bottomBehavior.peekHeight = bottom - top
        }
        songPanelNext.addOnLayoutChangeListener { view, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
            val lp = songPanelPlay.layoutParams as ConstraintLayout.LayoutParams
            lp.width = view.width
            lp.height = view.height
            songPanelPlay.layoutParams = lp
        }
        playerPanel.setOnClickListener {
            bottomBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
    }

    private fun checkPreferencesForFolder(): Boolean {
        @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
        currentPath = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
            .getString(PATH_KEY, "")

        if (currentPath == "") {
            val intent = Intent(applicationContext, ButtonActivity::class.java)
            startActivity(intent)
            return false
        } else
            return true
    }

    fun statePlayChanged(playing: Boolean) {
        if (playing) {
            songPanelPlay.visibility = View.INVISIBLE
            songPanelPlay.isEnabled = false

            songPanelPause.visibility = View.VISIBLE
            songPanelPause.isEnabled = true
        } else {
            songPanelPlay.visibility = View.VISIBLE
            songPanelPlay.isEnabled = true

            songPanelPause.visibility = View.INVISIBLE
            songPanelPause.isEnabled = false
        }
    }

}
