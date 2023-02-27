package com.orels.jeruchess.android.presentation.main.components

import androidx.annotation.DrawableRes
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.orels.jeruchess.android.R
import com.orels.jeruchess.android.core.presentation.Screens

@get:DrawableRes
private val Screens.icon: Int?
    get() = when (this) {
        Screens.Main -> R.drawable.round_home_24
        else -> null
    }

private const val navigation = "navigation"

@get:DrawableRes
private val String.icon: Int?
    get() = when (this) {
        navigation -> R.drawable.round_location_24
        else -> null
    }

@Composable
fun BottomBar(
    navHostController: NavHostController,
    onNavigationClick: () -> Unit,
) {
    val bottomBarDestinations = listOf(Screens.Main)
    val extraDestinations = listOf(navigation)
    val navBackStackEntry by navHostController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    BottomAppBar {
        bottomBarDestinations.forEach { screen ->
            val selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true
            BottomNavigationItem(
                selected = selected,
                onClick = {
                    navHostController.navigate(screen.route) {
                        popUpTo(navHostController.graph.findStartDestination().id) {
                            inclusive = false
                        }
                        launchSingleTop = true
                    }
                },
                label = { Text(text = stringResource(id = screen.label)) },
                icon = {
                    screen.icon?.let {
                        Icon(
                            painter = painterResource(id = it),
                            tint = MaterialTheme.colors.onBackground,
                            contentDescription = null
                        )
                    }
                }
            )
        }
        extraDestinations.forEach {
            BottomNavigationItem(
                selected = false,
                onClick = onNavigationClick,
                label = { Text(text = stringResource(id = R.string.label_navigation)) },
                icon = {
                    it.icon?.let {
                        Icon(
                            painter = painterResource(id = it),
                            tint = MaterialTheme.colors.onBackground,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    }
}