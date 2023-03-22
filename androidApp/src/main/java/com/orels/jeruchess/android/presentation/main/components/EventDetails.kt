package com.orels.jeruchess.android.presentation.main.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.orels.jeruchess.android.R
import com.orels.jeruchess.android.presentation.components.ActionButton
import com.orels.jeruchess.main.domain.model.Event

typealias OnPayByCardClick = (Event) -> Unit
typealias OnLaterClick = (Event) -> Unit
typealias OnUnregister = (Event) -> Unit
typealias OnDismiss = () -> Unit

@Composable
fun EventDetails(
    event: Event,
    onDismiss: OnDismiss,
    isRegistered: Boolean,
    onUnregister: OnUnregister,
    onPayByCardClick: OnPayByCardClick,
    onLaterClick: OnLaterClick
) {

    Dialog(
        onDismissRequest = onDismiss,
        DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
        )
    ) {
        Box(
            modifier = Modifier
                .background(
                    MaterialTheme.colors.surface,
                    shape = MaterialTheme.shapes.medium
                )
                .clip(MaterialTheme.shapes.medium)
                .padding(24.dp),
        ) {
            if (isRegistered) {
                RegistrationPopUp(
                    description = {
                        Text(
                            text = "${stringResource(R.string.what_a_shame_to_see_you_go)}\n ${
                                stringResource(
                                    id = R.string.hope_to_see_you_again_soon
                                )
                            }",
                            style = MaterialTheme.typography.body1.copy(fontSize = 18.sp),
                            color = MaterialTheme.colors.onSurface
                        )
                    },
                    primaryActionText = stringResource(id = R.string.thanks),
                    secondaryActionText = stringResource(R.string.nevermind_i_will_stay_exclamation),
                    primaryAction = { onDismiss() },
                    secondaryAction = { onUnregister(event) }
                )
            } else {
                RegistrationPopUp(
                    description = {
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = event.name,
                                style = MaterialTheme.typography.h5,
                                color = MaterialTheme.colors.onSurface
                            )
                            Text(
                                text = event.description,
                                style = MaterialTheme.typography.body1,
                                color = MaterialTheme.colors.onSurface
                            )
                        }
                    },
                    primaryActionText = stringResource(id = R.string.i_will_pay_now),
                    secondaryActionText = stringResource(R.string.later),
                    primaryAction = { onPayByCardClick(event) },
                    secondaryAction = { onLaterClick(event) }
                )
            }
        }
    }
}


@Composable
fun RegistrationPopUp(
    description: @Composable () -> Unit,
    primaryActionText: String,
    secondaryActionText: String,
    primaryAction: () -> Unit,
    secondaryAction: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(32.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        description()

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ActionButton(
                onClick = { primaryAction() }, colors = ButtonDefaults.buttonColors(
                    backgroundColor = MaterialTheme.colors.primary,
                    contentColor = MaterialTheme.colors.onPrimary
                ), text = primaryActionText
            )
            Text(
                modifier = Modifier.clickable { secondaryAction() },
                text = secondaryActionText,
                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colors.primary
            )
        }
    }
}