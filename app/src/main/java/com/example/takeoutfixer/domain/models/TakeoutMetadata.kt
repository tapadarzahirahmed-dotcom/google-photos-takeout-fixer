package com.example.takeoutfixer.domain.models

data class TakeoutMetadata(
    val title: String,
    val description: String?,
    val creationTime: Long?,
    val photoTakenTime: Long?,
    val geoData: GeoData?,
    val geoDataExif: GeoData?
)

data class GeoData(
    val latitude: Double,
    val longitude: Double,
    val altitude: Double,
    val latitudeSpan: Double,
    val longitudeSpan: Double
)
