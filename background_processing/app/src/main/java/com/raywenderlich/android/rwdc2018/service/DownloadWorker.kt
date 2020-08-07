package com.raywenderlich.android.rwdc2018.service

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.raywenderlich.android.rwdc2018.app.PhotosUtils

class DownloadWorker(context: Context, workerParams: WorkerParameters)
    : Worker(context, workerParams) {

    companion object {
        private const val TAG = "DownloadWorker"
    }

    override fun doWork(): Result {
        val needsRetry = try {
            val jsonString = PhotosUtils.fetchJsonString()
            (jsonString == null)
        } catch (e: InterruptedException) {
            Log.e(TAG, "Error downloading JSON: ${e.message}")
            true
        }

        if (needsRetry) {
            Log.i(TAG, "Result.RETRY")
            return Result.retry()
        }

        Log.i(TAG, "Result.SUCCESS")
        return Result.success()
    }
}