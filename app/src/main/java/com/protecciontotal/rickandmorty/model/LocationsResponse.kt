package com.protecciontotal.rickandmorty.model

data class LocationsResponse(
    val info: Info,
    val results: List<GeoLocation>
)

data class GeoLocation(
    val id: Int,
    val name: String,
    val type: String,
    val dimension: String,
    val residents: List<String>,
    val url: String,
    val created: String
)
