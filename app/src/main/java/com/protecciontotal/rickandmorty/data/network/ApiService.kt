package com.protecciontotal.rickandmorty.data.network

import com.protecciontotal.rickandmorty.model.CharacterDetailsResponse
import com.protecciontotal.rickandmorty.model.CharactersResponse
import com.protecciontotal.rickandmorty.model.EpisodeDetailsResponse
import com.protecciontotal.rickandmorty.model.EpisodesResponse
import com.protecciontotal.rickandmorty.model.LocationDetailsResponse
import com.protecciontotal.rickandmorty.model.LocationsResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("character")
    suspend fun getCharacters(): CharactersResponse

    @GET("episode")
    suspend fun getEpisodes(): EpisodesResponse

    @GET("location")
    suspend fun getLocations(): LocationsResponse

    @GET("character/{id}")
    suspend fun getCharacterById(@Path("id") id: Int): CharacterDetailsResponse

    @GET("episode/{id}")
    suspend fun getEpisodeById(@Path("id") id: Int): EpisodeDetailsResponse

    @GET("location/{id}")
    suspend fun getLocationById(@Path("id") id: Int): LocationDetailsResponse

}