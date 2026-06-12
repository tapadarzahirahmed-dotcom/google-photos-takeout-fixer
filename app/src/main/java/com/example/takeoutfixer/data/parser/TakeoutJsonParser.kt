package com.example.takeoutfixer.data.parser

import android.content.Context
import android.net.Uri
import com.example.takeoutfixer.domain.models.GeoData
import com.example.takeoutfixer.domain.models.TakeoutMetadata
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.InputStreamReader
import javax.inject.Inject

class TakeoutJsonParser @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun parse(uri: Uri): TakeoutMetadata? {
        return try {
            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                val jsonObject = JsonParser.parseReader(InputStreamReader(inputStream)).asJsonObject
                
                val title = jsonObject.get("title")?.asString ?: ""
                val description = jsonObject.get("description")?.asString
                
                val creationTimeObj = jsonObject.getAsJsonObject("creationTime")
                val creationTime = creationTimeObj?.get("timestamp")?.asLong
                
                val photoTakenTimeObj = jsonObject.getAsJsonObject("photoTakenTime")
                val photoTakenTime = photoTakenTimeObj?.get("timestamp")?.asLong

                val geoData = parseGeoData(jsonObject.getAsJsonObject("geoData"))
                val geoDataExif = parseGeoData(jsonObject.getAsJsonObject("geoDataExif"))

                TakeoutMetadata(
                    title = title,
                    description = description,
                    creationTime = creationTime,
                    photoTakenTime = photoTakenTime,
                    geoData = geoData,
                    geoDataExif = geoDataExif
                )
            }
        } catch (e: Exception) {
            null
        }
    }

    private fun parseGeoData(obj: JsonObject?): GeoData? {
        if (obj == null) return null
        return try {
            GeoData(
                latitude = obj.get("latitude").asDouble,
                longitude = obj.get("longitude").asDouble,
                altitude = obj.get("altitude").asDouble,
                latitudeSpan = obj.get("latitudeSpan").asDouble,
                longitudeSpan = obj.get("longitudeSpan").asDouble
            )
        } catch (e: Exception) {
            null
        }
    }
}
