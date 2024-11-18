package com.protecciontotal.rickandmorty.data.repository

import com.protecciontotal.rickandmorty.data.network.ApiService
import com.protecciontotal.rickandmorty.model.CharacterDetailsResponse
import com.protecciontotal.rickandmorty.model.EpisodeDetailsResponse
import com.protecciontotal.rickandmorty.model.LocationDetailsResponse

class DataRepository(private val apiService: ApiService) {
    suspend fun getCharacters() = apiService.getCharacters()
    suspend fun getEpisodes() = apiService.getEpisodes()
    suspend fun getLocations() = apiService.getLocations()
    suspend fun getCharacterById(id: Int): CharacterDetailsResponse {
        return apiService.getCharacterById(id)
    }
    suspend fun getEpisodeById(id: Int) : EpisodeDetailsResponse {
        return apiService.getEpisodeById(id)
    }
    suspend fun getLocationById(id: Int) : LocationDetailsResponse {
        return apiService.getLocationById(id)

    }
}