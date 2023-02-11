package com.orels.jeruchess.android.presentation.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.orels.jeruchess.android.R
import com.orels.jeruchess.main.presentation.MainEvent
import com.orels.jeruchess.main.presentation.MainState

@Composable
fun MainScreen(
    navController: NavController,
    state: MainState,
    viewModel: AndroidMainViewModel
) {
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
                onClick = { viewModel.onEvent(MainEvent.NavigateToClubAddress) }
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
        modifier = Modifier.clickable(onClick = { onClick(clubAddress) })
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_location),
            contentDescription = stringResource(
                R.string.location_icon
            )
        )
        Text(
            text = clubAddress,
            style = MaterialTheme.typography.body2,
            textAlign = TextAlign.Start,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

@Composable
fun MainContent(
    viewModel: AndroidMainViewModel,
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text("Main Content")
    }
}