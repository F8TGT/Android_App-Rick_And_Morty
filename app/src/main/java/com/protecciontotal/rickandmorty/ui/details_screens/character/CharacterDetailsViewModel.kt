package com.protecciontotal.rickandmorty.ui.details_screens.character

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.protecciontotal.rickandmorty.data.network.RetrofitInstance
import com.protecciontotal.rickandmorty.data.repository.DataRepository
import com.protecciontotal.rickandmorty.model.CharacterDetailsResponse
import com.protecciontotal.rickandmorty.model.EpisodeDetailsResponse
import kotlinx.coroutines.launch
import java.io.IOException
import java.net.UnknownHostException

class CharacterDetailsViewModel : ViewModel() {
    val isLoading: MutableState<Boolean> = mutableStateOf(false)
    val characterIsLoading: MutableState<Boolean> = mutableStateOf(false)
    private val repository = DataRepository(RetrofitInstance.api)
    private val _character = mutableStateOf<CharacterDetailsResponse?>(null)
    val character: State<CharacterDetailsResponse?> get() = _character
    val toastMessage = mutableStateOf<String?>(null)
    val episodes = mutableStateOf<List<EpisodeDetailsResponse>>(emptyList())

    fun fetchCharacterById(id: Int) {
        viewModelScope.launch {
            try {
                isLoading.value = true
                val response = repository.getCharacterById(id)
                _character.value = response
                Log.d("CharacterDetailsViewModel", "Fetched character: $response")
            } catch (e: UnknownHostException) {
                toastMessage.value = "No network connection. Please check your internet."
                Log.e("NetworkCall", "No network connection. Please check your internet. ${e.message}.")
            } catch (e: IOException) {
                toastMessage.value = "A network error occurred. Please try again later."
                Log.e("NetworkCall", "An error occurred: ${e.localizedMessage}")
            } catch (e: Exception) {
                toastMessage.value = e.message ?: "An unknown error occurred."
                Log.e("CharacterDetailsViewModel", "Error fetching character: ${e.message}")
            } finally {
                isLoading.value = false
            }
        }
    }

    fun fetchEpisodesByIds(ids: List<Int>) {
        viewModelScope.launch {
            try {
                characterIsLoading.value = true
                val episodeDetails = ids.mapNotNull { id ->
                    try {
                        repository.getEpisodeById(id)
                    } catch (e: Exception) {
                        Log.e(
                            "EpisodeDetailsViewModel",
                            "Error fetching episode with ID $id: ${e.message}"
                        )
                        null
                    }
                }
                episodes.value = episodeDetails
                Log.d("EpisodeDetailsViewModel", "Fetched episodes: $episodeDetails")
            } catch (e: UnknownHostException) {
                toastMessage.value = "No network connection. Please check your internet."
                Log.e("NetworkCall", "No network connection. Please check your internet. ${e.message}.")
            } catch (e: IOException) {
                toastMessage.value = "A network error occurred. Please try again later."
                Log.e("NetworkCall", "An error occurred: ${e.localizedMessage}")
            } catch (e: Exception) {
                toastMessage.value = e.message ?: "An error occurred while fetching episodes."
                Log.e("EpisodeDetailsViewModel", "Error fetching episodes: ${e.message}")
            } finally {
                characterIsLoading.value = false
            }
        }
    }
}