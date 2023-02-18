package com.orels.jeruchess.android.presentation.components


import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.orels.jeruchess.android.R

@Composable
fun Input(
    modifier: Modifier = Modifier,
    title: String = "",
    placeholder: String = "",
    initialText: String = "",
    minLines: Int = 1,
    maxLines: Int = 1,
    isError: Boolean = false,
    isPassword: Boolean = false,
    shouldFocus: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text,
    leadingIcon: (@Composable (() -> Unit))? = null,
    trailingIcon: (@Composable (() -> Unit))? = { },
    onTextChange: (String) -> Unit = {},
    isDisabled: Boolean = false
) {
    val value = remember { mutableStateOf(initialText) }
    val passwordVisible = rememberSaveable { mutableStateOf(false) }
    val lineHeight = 40
    val focusRequester = FocusRequester()

    var inputModifier = if (shouldFocus) Modifier.focusRequester(focusRequester) else Modifier

    inputModifier =
        if (maxLines == 1) inputModifier else inputModifier.height((lineHeight * minLines).dp)

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = title,
            modifier = Modifier.padding(bottom = 4.dp),
            style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Normal),
            color = MaterialTheme.colors.onBackground.copy(alpha = 0.9f)
        )
        Box(modifier = Modifier.zIndex(1f)) {
            OutlinedTextField(
                modifier = inputModifier
                    .fillMaxWidth()
                    .focusable(enabled = shouldFocus),
                value = value.value,
                onValueChange = {
                    value.value = it
                    onTextChange(it)
                },
                placeholder = {
                    Text(
                        text = placeholder,
                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.4f)
                    )
                },
                singleLine = maxLines == 1,
                visualTransformation = if (isPassword && !passwordVisible.value) PasswordVisualTransformation() else VisualTransformation.None,
                keyboardOptions = if (isPassword) KeyboardOptions(keyboardType = KeyboardType.Password) else KeyboardOptions(
                    keyboardType = keyboardType
                ),
                trailingIcon = {
                    if (isPassword) {
                        PasswordIcon(
                            passwordVisible = passwordVisible.value,
                            onClick = { passwordVisible.value = !passwordVisible.value })
                    } else {
                        trailingIcon?.invoke()
                    }
                },
                leadingIcon = leadingIcon,
                isError = isError,
                enabled = isDisabled.not()
            )
        }
    }
}

@Composable
private fun PasswordIcon(
    passwordVisible: Boolean,
    onClick: () -> Unit
) {
    val image = if (passwordVisible)
        painterResource(id = R.drawable.ic_visibility_off_24)
    else painterResource(id = R.drawable.ic_visibility_24)
    val description =
        if (passwordVisible) stringResource(R.string.hide_password) else stringResource(R.string.show_password)

    IconButton(onClick = onClick) {
        Icon(painter = image, description)
    }
}