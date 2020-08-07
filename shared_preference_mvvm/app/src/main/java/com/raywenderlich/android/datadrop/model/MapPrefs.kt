package com.raywenderlich.android.datadrop.model

import android.preference.PreferenceManager
import com.raywenderlich.android.datadrop.app.DataDropApplication
import com.raywenderlich.android.datadrop.model.MarkerColor.Companion.RED_COLOR

object MapPrefs {

    private const val MARKER_COLOR_KEY = "MARKER_COLOR_KEY"
    private const val MAP_TYPE_KEY = "MAP_TYPE_KEY"

    fun sharedPrefs() = PreferenceManager.getDefaultSharedPreferences(DataDropApplication.getAppContext())

    fun saveMarkerColor(markerColor: String) {
        val editor = sharedPrefs().edit()
        editor.putString(MARKER_COLOR_KEY, markerColor).apply()
    }

    fun getMarkerColor(): String = sharedPrefs().getString(MARKER_COLOR_KEY, RED_COLOR)

    fun saveMapType(mapType: String) {
        val editor = sharedPrefs().edit()
        editor.putString(MAP_TYPE_KEY, mapType).apply()
    }

    fun getMapType(): String = sharedPrefs().getString(MAP_TYPE_KEY, "Normal")
}