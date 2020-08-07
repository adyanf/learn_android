package com.raywenderlich.android.rwdc2018.service

import android.app.job.JobParameters
import android.app.job.JobService
import android.util.Log

class LogJobService : JobService() {

    override fun onCreate() {
        super.onCreate()
        Log.i(TAG, "onCreate")
    }

    override fun onDestroy() {
        Log.i(TAG, "onDestroy")
        super.onDestroy()
    }

    override fun onStartJob(params: JobParameters?): Boolean {
        val runnable = Runnable {
            Thread.sleep(10000)
            jobFinished(params, false)
            Log.i(TAG, "Job finished: ${params?.jobId}")
        }

        Log.i(TAG, "Starting job: ${params?.jobId}")
        Thread(runnable).start()
        return true
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        return false
    }

    companion object {
        private const val TAG = "LogJobService"
    }
}
