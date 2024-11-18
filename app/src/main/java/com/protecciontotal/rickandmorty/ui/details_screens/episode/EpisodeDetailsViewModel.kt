package com.protecciontotal.rickandmorty.ui.details_screens.episode

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

class EpisodeDetailsViewModel : ViewModel() {
    val isLoading: MutableState<Boolean> = mutableStateOf(false)
    val characterIsLoading: MutableState<Boolean> = mutableStateOf(false)
    private val repository = DataRepository(RetrofitInstance.api)
    private val _episode = mutableStateOf<EpisodeDetailsResponse?>(null)
    val episode: State<EpisodeDetailsResponse?> get() = _episode
    val characters = mutableStateOf<List<CharacterDetailsResponse>>(emptyList())
    val toastMessage = mutableStateOf<String?>(null)

    fun fetchEpisodeById(id: Int) {
        viewModelScope.launch {
            try {
                isLoading.value = true
                val response = repository.getEpisodeById(id)
                _episode.value = response
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

    fun fetchCharactersByIds(ids: List<Int>) {
        viewModelScope.launch {
            try {
                characterIsLoading.value = true
                val characterDetails = ids.mapNotNull { id ->
                    try {
                        repository.getCharacterById(id)
                    } catch (e: Exception) {
                        Log.e(
                            "EpisodeDetailsViewModel",
                            "Error fetching character with ID $id: ${e.message}"
                        )
                        null
                    }
                }
                characters.value = characterDetails
                Log.d("EpisodeDetailsViewModel", "Fetched characters: $characterDetails")
            } catch (e: UnknownHostException) {
                toastMessage.value = "No network connection. Please check your internet."
                Log.e("NetworkCall", "No network connection. Please check your internet. ${e.message}.")
            } catch (e: IOException) {
                toastMessage.value = "A network error occurred. Please try again later."
                Log.e("NetworkCall", "An error occurred: ${e.localizedMessage}")
            } catch (e: Exception) {
                toastMessage.value = e.message ?: "An error occurred while fetching characters."
                Log.e("EpisodeDetailsViewModel", "Error fetching characters: ${e.message}")
            } finally {
                characterIsLoading.value = false
            }
        }
    }
}