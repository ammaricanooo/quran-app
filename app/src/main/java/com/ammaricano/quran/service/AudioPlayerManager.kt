package com.ammaricano.quran.service

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class AudioPlaybackState(
    val isPlaying: Boolean = false,
    val title: String = "",
    val subtitle: String = "",
    val currentUrl: String? = null
)

class AudioPlayerManager(context: Context) {
    private val exoPlayer = ExoPlayer.Builder(context).build()

    private val _playbackState = MutableStateFlow(AudioPlaybackState())
    val playbackState: StateFlow<AudioPlaybackState> = _playbackState.asStateFlow()

    init {
        exoPlayer.addListener(object : Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                _playbackState.value = _playbackState.value.copy(isPlaying = isPlaying)
            }

            override fun onPlaybackStateChanged(playbackState: Int) {
                if (playbackState == Player.STATE_ENDED) {
                    _playbackState.value = _playbackState.value.copy(isPlaying = false)
                }
            }
        })
    }

    fun playAudio(url: String, title: String, subtitle: String) {
        if (_playbackState.value.currentUrl == url && exoPlayer.playbackState != Player.STATE_IDLE) {
            if (exoPlayer.isPlaying) {
                exoPlayer.pause()
            } else {
                exoPlayer.play()
            }
            return
        }

        exoPlayer.stop()
        val mediaItem = MediaItem.fromUri(url)
        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()
        exoPlayer.play()

        _playbackState.value = AudioPlaybackState(
            isPlaying = true,
            title = title,
            subtitle = subtitle,
            currentUrl = url
        )
    }

    fun togglePlayPause() {
        if (exoPlayer.isPlaying) {
            exoPlayer.pause()
        } else {
            exoPlayer.play()
        }
    }

    fun stop() {
        exoPlayer.stop()
        _playbackState.value = AudioPlaybackState()
    }

    fun release() {
        exoPlayer.release()
    }
}
