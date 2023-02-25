package com.orels.jeruchess.android.presentation.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.orels.jeruchess.android.R
import com.orels.jeruchess.android.presentation.main.components.EventCard
import com.orels.jeruchess.main.presentation.MainState

@Composable
fun MainScreen(
    navController: NavController,
    state: MainState,
    viewModel: AndroidMainViewModel
) {

    val context = LocalContext.current

    Scaffold(
        topBar = {

        },
        content = {
            Box(modifier = Modifier.padding(it)) {
                MainContent(viewModel = viewModel)
            }
        },
        bottomBar = {
            BottomBar(
                clubAddress = viewModel.state.value.clubData?.address ?: "",
                onClick = { viewModel.openGoogleMaps(context = context) }
            )
        }
    )
}

@Composable
fun BottomBar(
    clubAddress: String,
    onClick: (String) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable(onClick = { onClick(clubAddress) }),
        horizontalArrangement = Arrangement.End
    ) {
        Text(
            text = clubAddress,
            style = MaterialTheme.typography.body2,
            textAlign = TextAlign.Start,
            modifier = Modifier.padding(start = 8.dp)
        )
        Icon(
            modifier = Modifier.padding(start = 8.dp),
            painter = painterResource(id = R.drawable.ic_location),
            tint = MaterialTheme.colors.onBackground,
            contentDescription = stringResource(
                R.string.location_icon
            ),
        )
    }
}

@Composable
fun MainContent(
    viewModel: AndroidMainViewModel,
) {
    val state = viewModel.state.value
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {
        items(state.events.size) { index ->
            EventCard(
                event = state.events[index],
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}