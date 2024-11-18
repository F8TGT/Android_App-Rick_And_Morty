package com.protecciontotal.rickandmorty.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.protecciontotal.rickandmorty.ui.details_screens.character.CharacterDetails
import com.protecciontotal.rickandmorty.ui.details_screens.character.CharacterDetailsViewModel
import com.protecciontotal.rickandmorty.ui.details_screens.episode.EpisodeDetails
import com.protecciontotal.rickandmorty.ui.details_screens.episode.EpisodeDetailsViewModel
import com.protecciontotal.rickandmorty.ui.details_screens.location.LocationDetails
import com.protecciontotal.rickandmorty.ui.details_screens.location.LocationDetailsViewModel
import com.protecciontotal.rickandmorty.ui.screens.characters.Characters
import com.protecciontotal.rickandmorty.ui.screens.characters.CharactersViewModel
import com.protecciontotal.rickandmorty.ui.screens.episodes.Episodes
import com.protecciontotal.rickandmorty.ui.screens.episodes.EpisodesViewModel
import com.protecciontotal.rickandmorty.ui.screens.home.Home
import com.protecciontotal.rickandmorty.ui.screens.locations.Locations
import com.protecciontotal.rickandmorty.ui.screens.locations.LocationsViewModel

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Routes.Home) {
        composable(Routes.Home) {
            Home(navController)
        }
        composable(Routes.Locations) {
            val viewModel: LocationsViewModel = viewModel()
            Locations(navController, viewModel = viewModel)
        }
        composable(Routes.Characters) {
            val viewModel: CharactersViewModel = viewModel()
            Characters(navController, viewModel = viewModel)
        }
        composable(Routes.Episodes) {
            val viewModel: EpisodesViewModel = viewModel()
            Episodes(navController, viewModel = viewModel)
        }
        composable(
            Routes.CharactersDetails,
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
            val viewModel: CharacterDetailsViewModel = viewModel()
            val characterId = backStackEntry.arguments?.getInt("id") ?: 0
            CharacterDetails(navController, viewModel, characterId)
        }
        composable(
            Routes.EpisodesDetails,
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
            val viewModel: EpisodeDetailsViewModel = viewModel()
            val episodeId = backStackEntry.arguments?.getInt("id") ?: 0
            EpisodeDetails(navController, viewModel, episodeId)
        }
        composable(
            Routes.LocationsDetails,
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
            val viewModel: LocationDetailsViewModel = viewModel()
            val locationId = backStackEntry.arguments?.getInt("id") ?: 0
            LocationDetails(navController, viewModel, locationId)
        }
    }
}