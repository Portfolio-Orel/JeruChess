package com.orels.jeruchess.android.presentation.main.components

import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.orels.jeruchess.android.R
import com.orels.jeruchess.main.domain.model.Currency

@Composable
fun Currency(currency: Currency, modifier: Modifier = Modifier) {
    when (currency) {
        Currency.ILS -> {
            Icon(
                modifier = modifier,
                painter = painterResource(id = R.drawable.ils_50),
                contentDescription = "ILS",
            )
        }
        Currency.USD -> {
            Icon(
                modifier = modifier,
                painter = painterResource(id = R.drawable.usd_50),
                contentDescription = "USD",
            )
        }
    }
}