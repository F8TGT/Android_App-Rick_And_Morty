package com.protecciontotal.rickandmorty.ui.details_screens.episode

import android.content.res.Configuration
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight.Companion.Medium
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.protecciontotal.rickandmorty.R
import com.protecciontotal.rickandmorty.ui.screens.components.TopNavBarComponent
import com.protecciontotal.rickandmorty.utils.extractIdFromUrl

@Composable
fun EpisodeDetails(
    navController: NavHostController,
    viewModel: EpisodeDetailsViewModel,
    episodeId: Int,
    modifier: Modifier = Modifier
) {
    val episodeDetails by viewModel.episode
    val isLoading by viewModel.isLoading
    val characterIsLoading by viewModel.characterIsLoading
    val context = LocalContext.current
    val toastMessage by viewModel.toastMessage
    val characters by viewModel.characters

    LaunchedEffect(episodeId) {
        Log.d("EpisodeDetails", "Navigated to episode with ID: $episodeId")
        viewModel.fetchEpisodeById(episodeId)
    }

    toastMessage?.let { message ->
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        viewModel.toastMessage.value = null
    }

    Scaffold(
        topBar = {
            TopNavBarComponent(
                navController,
                stringResource(id = R.string.episodes_details_title)
            )
        },
        content = { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .semantics { contentDescription = "Loading, please wait" }
                    )
                } else {
                    episodeDetails?.let { episode ->
                        OutlinedCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .verticalScroll(rememberScrollState())
                        ) {
                            Column(
                                horizontalAlignment = Alignment.Start,
                                verticalArrangement = Arrangement.SpaceBetween,
                                modifier = modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            ) {
                                Text(
                                    text = episode.name,
                                    fontWeight = Medium,
                                    style = MaterialTheme.typography.headlineSmall,
                                )
                                Text(
                                    text = "Air Date: ${episode.air_date.ifEmpty { "unknown" }}",
                                )
                                Text(
                                    text = "Episode: ${episode.episode.ifEmpty { "unknown" }}",
                                )
                                LaunchedEffect(episodeId) {
                                    val characterIds = episode.characters
                                        .mapNotNull { extractIdFromUrl(it) }
                                    viewModel.fetchCharactersByIds(characterIds)
                                }
                                Text(
                                    text = "Characters:",
                                )
                                if (characterIsLoading) {
                                    CircularProgressIndicator(
                                        modifier = Modifier
                                            .align(Alignment.CenterHorizontally)
                                            .semantics { contentDescription = "Loading, please wait" }
                                    )
                                } else {
                                    LazyRow(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 16.dp),
                                    ) {
                                        items(characters) { character ->
                                            Column(
                                                verticalArrangement = Arrangement.Center,
                                                horizontalAlignment = Alignment.CenterHorizontally,
                                                modifier = Modifier
                                                    .padding(end = 8.dp),
                                            ) {
                                                OutlinedCard (
                                                    modifier = Modifier
                                                        .clickable { navController.navigate("character/${character.id}") }
                                                        .semantics { role = Role.Button },
                                                ) {
                                                    Column(
                                                        verticalArrangement = Arrangement.Center,
                                                        horizontalAlignment = Alignment.CenterHorizontally,
                                                        modifier = Modifier.padding(8.dp)
                                                    ) {
                                                        Image(
                                                            painter = rememberAsyncImagePainter(
                                                                placeholder = painterResource(R.drawable.round_no_accounts_24),
                                                                model = character.image,
                                                                error = painterResource(R.drawable.round_no_accounts_24)
                                                            ),
                                                            contentDescription = null,
                                                            contentScale = ContentScale.Crop,
                                                            modifier = Modifier
                                                                .fillMaxHeight()
                                                                .defaultMinSize(100.dp)
                                                                .clip(CircleShape)
                                                        )
                                                        Text(text = character.name.ifEmpty { "unknown" })
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        },
        modifier = modifier
            .fillMaxSize()
    )
}

@Preview(
    name = "Episode Details",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun EpisodeDetailsPreview() {
    EpisodeDetails(
        navController = rememberNavController(),
        EpisodeDetailsViewModel(),
        1
    )
}