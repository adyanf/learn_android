package com.raywenderlich.android.rwdc2018.service

import android.app.IntentService
import android.content.Context
import android.content.Intent
import android.support.v4.content.LocalBroadcastManager
import android.util.Log
import com.raywenderlich.android.rwdc2018.app.PhotosUtils

class FetchIntentService : IntentService("FetchIntentService") {

    override fun onHandleIntent(intent: Intent?) {
        when (intent?.action) {
            ACTION_FETCH -> handleActionDownload()
        }
    }

    private fun handleActionDownload() {
        try {
            Log.i(TAG, "Starting download json")
            PhotosUtils.fetchJsonString()
            Log.i(TAG, "Ending download json")

            Log.i(TAG, "Sending broadcast json")
            broadcastDownloadComplete()
        } catch (e: InterruptedException) {
            Log.e(TAG, "Error downloading json")
        }
    }

    private fun broadcastDownloadComplete() {
        val intent = Intent(FETCH_COMPLETE)
        LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)
    }

    companion object {
        private const val TAG  = "DownloadPhotosIntent"
        private const val ACTION_FETCH = "ACTION_FETCH"
        const val FETCH_COMPLETE = "FETCH_COMPLETE"

        fun startActionDownload(context: Context) {
            val intent = Intent(context, FetchIntentService::class.java).apply {
                action = ACTION_FETCH
            }
            context.startService(intent)
        }
    }
}
