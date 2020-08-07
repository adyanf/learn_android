package com.raywenderlich.android.datadrop.model

import android.arch.persistence.room.TypeConverter
import com.google.android.gms.maps.model.LatLng
import java.util.*

class LatLngConverter {
    @TypeConverter
    fun fromLatLng(latLng: LatLng?): String? {
        if (latLng != null) {
            return String.format(Locale.US, "%f,%f", latLng.latitude, latLng.longitude)
        }
        return null
    }

    @TypeConverter
    fun toLatLng(value: String?): LatLng? {
        if (value != null) {
            val pieces = value.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            return LatLng(pieces[0].toDouble(), pieces[1].toDouble())
        }
        return null
    }
}