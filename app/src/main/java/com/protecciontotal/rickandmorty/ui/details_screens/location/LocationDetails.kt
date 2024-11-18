package com.protecciontotal.rickandmorty.ui.details_screens.location

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
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.protecciontotal.rickandmorty.R
import com.protecciontotal.rickandmorty.ui.screens.components.TopNavBarComponent
import com.protecciontotal.rickandmorty.utils.extractIdFromUrl

@Composable
fun LocationDetails(
    navController: NavHostController,
    viewModel: LocationDetailsViewModel,
    locationId: Int,
    modifier: Modifier = Modifier
) {
    val locationDetails by viewModel.location
    val isLoading by viewModel.isLoading
    val characterIsLoading by viewModel.characterIsLoading
    val context = LocalContext.current
    val toastMessage by viewModel.toastMessage
    val characters by viewModel.characters

    LaunchedEffect(locationId) {
        Log.d("LocationDetails", "Navigated to location with ID: $locationId")
        viewModel.fetchLocationById(locationId)
    }

    toastMessage?.let { message ->
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        viewModel.toastMessage.value = null
    }

    Scaffold(
        topBar = {
            TopNavBarComponent(
                navController,
                stringResource(id = R.string.locations_details_title)
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
                    locationDetails?.let { location ->
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
                                    text = location.name,
                                    fontWeight = Medium,
                                    fontSize = 24.sp
                                )
                                Text(
                                    text = "Type: ${location.type.ifEmpty { "unknown" }}",
                                )
                                Text(
                                    text = "Episode: ${location.dimension.ifEmpty { "unknown" }}",
                                )
                                LaunchedEffect(locationId) {
                                    val characterIds = location.residents
                                        .mapNotNull { extractIdFromUrl(it) }
                                    viewModel.fetchCharactersByIds(characterIds)
                                }
                                Text(
                                    text = "Residents:",
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
                                                horizontalAlignment = Alignment.CenterHorizontally,
                                                modifier = Modifier
                                                    .padding(end = 8.dp),
                                            ) {
                                                OutlinedCard(
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
                                                                error = painterResource(R.drawable.round_no_accounts_24),
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
    name = "Location Details",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun LocationDetailsPreview() {
    LocationDetails(
        navController = rememberNavController(),
        LocationDetailsViewModel(),
        1
    )
}