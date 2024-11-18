package com.protecciontotal.rickandmorty.ui.screens.locations

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.protecciontotal.rickandmorty.R
import com.protecciontotal.rickandmorty.ui.screens.components.TopNavBarComponent

@Composable
fun Locations(
    navController: NavHostController,
    viewModel: LocationsViewModel,
    modifier: Modifier = Modifier
) {
    val locations by viewModel.geoLocation
    val isLoading by viewModel.isLoading
    val context = LocalContext.current
    val toastMessage by viewModel.toastMessage

    LaunchedEffect(key1 = true) {
        if (locations.isEmpty()) {
            viewModel.fetchLocations()
        }
    }

    toastMessage?.let { message ->
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        viewModel.toastMessage.value = null
    }

    Scaffold(
        topBar = {
            TopNavBarComponent(navController, stringResource(id = R.string.locations_title))
        },
        content = { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .semantics { contentDescription = "Loading, please wait" }
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .padding(16.dp)
                    ) {
                        items(locations) { location ->
                            OutlinedCard(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { navController.navigate("location/${location.id}") }
                                    .semantics { role = Role.Button },
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = modifier
                                        .fillMaxWidth()
                                        .padding(16.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Start,
                                        modifier = Modifier
                                            .weight(1f)
                                            .fillMaxWidth()
                                    ) {
                                        Column {
                                            Text(
                                                text = location.name,
                                                fontWeight = Medium,
                                                fontSize = 20.sp
                                            )
                                            Text(
                                                text = "Type: ${location.type}",
                                            )
                                            Text(
                                                text = "Dimension: ${location.dimension}",
                                            )
                                        }
                                    }
                                    Icon(
                                        painter = painterResource(R.drawable.round_keyboard_arrow_right_24),
                                        contentDescription = null
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }
                }
            }
        },
        modifier = modifier
            .fillMaxSize()
    )
}

@Preview(
    name = "Locations",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun LocationsPreview() {
    Locations(navController = rememberNavController(), LocationsViewModel())
}