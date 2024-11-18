package com.protecciontotal.rickandmorty.model

data class CharactersResponse(
    val info: Info,
    val results: List<RickAndMortyCharacter>
)

data class Info(
    val count: Int,
    val pages: Int,
    val next: String,
    val prev: String
)

data class RickAndMortyCharacter(
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val type: String,
    val gender: String,
    val origin: Origin,
    val location: Location,
    val image: String,
    val episode: List<String>,
    val url: String,
    val created: String
)

data class Origin(
    val name: String,
    val url: String
)

data class Location(
    val name: String,
    val url: String
)
