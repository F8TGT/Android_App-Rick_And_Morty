package com.protecciontotal.rickandmorty.ui.screens.episodes

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.protecciontotal.rickandmorty.data.network.RetrofitInstance
import com.protecciontotal.rickandmorty.data.repository.DataRepository
import com.protecciontotal.rickandmorty.model.Episode
import kotlinx.coroutines.launch
import java.io.IOException
import java.net.UnknownHostException

class EpisodesViewModel : ViewModel() {
    val isLoading: MutableState<Boolean> = mutableStateOf(false)
    private val repository = DataRepository(RetrofitInstance.api)
    private val _episodes = mutableStateOf<List<Episode>>(emptyList())
    val episodes: State<List<Episode>> get() = _episodes
    val toastMessage = mutableStateOf<String?>(null)

    fun fetchEpisodes() {
        viewModelScope.launch {
            try {
                isLoading.value = true
                val response = repository.getEpisodes()
                _episodes.value = response.results
                Log.d("EpisodesViewModel", "Fetched episodes: ${response.results}")
            } catch (e: UnknownHostException) {
                toastMessage.value = "No network connection. Please check your internet."
                Log.e("NetworkCall", "No network connection. Please check your internet. ${e.message}.")
            } catch (e: IOException) {
                toastMessage.value = "A network error occurred. Please try again later."
                Log.e("NetworkCall", "An error occurred: ${e.localizedMessage}")
            } catch (e: Exception) {
                toastMessage.value = e.message ?: "An unknown error occurred."
                Log.e("EpisodesViewModel", "Error fetching espisode: ${e.message}")
            } finally {
                isLoading.value = false
            }
        }
    }
}