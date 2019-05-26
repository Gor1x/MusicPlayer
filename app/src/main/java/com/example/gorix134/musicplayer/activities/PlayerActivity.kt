package com.example.gorix134.musicplayer.activities

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
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.example.gorix134.musicplayer.PlayerService
import com.example.gorix134.musicplayer.R
import kotlinx.android.synthetic.main.bottom_sheet.*


class PlayerActivity : AppCompatActivity() {


    private var playerServiceBinder: PlayerService.PlayerServiceBinder? = null
    private var currentPath = "" //Empty if it is a first run
    private lateinit var bottomBehavior: BottomSheetBehavior<ConstraintLayout>
    private lateinit var callback: MediaControllerCompat.Callback
    private lateinit var serviceConnection: ServiceConnection
    private var mediaController: MediaControllerCompat? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
        initBottomSheet()


        callback = object : MediaControllerCompat.Callback() {
            override fun onPlaybackStateChanged(state: PlaybackStateCompat?) {
                if (state == null)
                    return
                val playing = (state.state == PlaybackStateCompat.STATE_PLAYING)
                statePlayChanged(playing)
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

    private fun initBottomSheet() {
        bottomBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomBehavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_EXPANDED)
                    playerPanel.alpha = 0F
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                playerPanel.alpha = 1 - slideOffset
            }
        })

        playerPanel.addOnLayoutChangeListener { view, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
            bottomBehavior.peekHeight = bottom
        }
        songPanelNext.addOnLayoutChangeListener { view, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
            val lp = songPanelPlay.layoutParams as ConstraintLayout.LayoutParams
            lp.width = view.width
            lp.height = view.height
            songPanelPlay.layoutParams = lp
        }
    }

    override fun onResume() {
        super.onResume()
        checkPreferencesForFolder()
    }

    private fun checkPreferencesForFolder() {
        @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
        currentPath = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
            .getString(PATH_KEY, "")

        if (currentPath == "") {
            val intent = Intent(applicationContext, ButtonActivity::class.java)
            startActivity(intent)
        }
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
