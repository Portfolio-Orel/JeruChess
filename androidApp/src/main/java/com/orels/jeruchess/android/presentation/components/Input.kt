package com.orels.jeruchess.android.presentation.components


import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
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
    maxCharacters: Int? = null,
    isError: Boolean = false,
    isPassword: Boolean = false,
    shouldFocus: Boolean = false,
    keyboardType: CustomKeyboardType = CustomKeyboardType.Text,
    keyboardActions: KeyboardActions = KeyboardActions(),
    leadingIcon: (@Composable (() -> Unit))? = null,
    trailingIcon: (@Composable (() -> Unit))? = { },
    value: String = "",
    onTextChange: (String) -> Unit = {},
    isDisabled: Boolean = false,
    formatter: (String) -> String = { it }
) {
    val textFieldValueState = remember { mutableStateOf(TextFieldValue(value)) }
    val previousText = remember { mutableStateOf(initialText) }

    val passwordVisible = rememberSaveable { mutableStateOf(false) }
    val lineHeight = 40
    val focusRequester = FocusRequester()

    var inputModifier = if (shouldFocus) Modifier.focusRequester(focusRequester) else Modifier
    val maxWidth = if (maxCharacters != null) (maxCharacters * 75).dp else null

    inputModifier =
        if (maxLines == 1) inputModifier else inputModifier.requiredHeight((lineHeight * minLines).dp)
    inputModifier = if (maxWidth != null) inputModifier.requiredWidth(maxWidth) else inputModifier

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
                value = textFieldValueState.value,
                onValueChange = {
                    if (previousText.value == it.text) return@OutlinedTextField
                    var newValue = it.text
                    if (maxCharacters != null) {
                        newValue = it.text.takeLast(maxCharacters)
                    }
                    textFieldValueState.value =
                        keyboardType.buildTextFieldValue(
                            it.composition,
                            newValue,
                            previousText.value,
                            formatter
                        )
                    newValue = formatter(newValue)
                    previousText.value = newValue
                    onTextChange(newValue)
                },
                placeholder = {
                    Text(
                        text = formatter(placeholder),
                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.4f)
                    )
                },
                singleLine = maxLines == 1,
                visualTransformation = if (isPassword && !passwordVisible.value) PasswordVisualTransformation() else VisualTransformation.None,
                keyboardOptions = if (isPassword) KeyboardOptions(keyboardType = KeyboardType.Password) else KeyboardOptions(
                    keyboardType = keyboardType.type
                ),
                keyboardActions = keyboardActions,
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

@Suppress("unused")
enum class CustomKeyboardType(
    val type: KeyboardType,
    val buildTextFieldValue: (
        currentComposition: TextRange?,
        newValue: String,
        previousValue: String,
        formatter: (String) -> String
    ) -> TextFieldValue = { currentComposition, newValue, _, _ ->
        TextFieldValue(
            newValue,
            TextRange(newValue.length),
            currentComposition
        )
    }
) {
    Ascii(type = KeyboardType.Ascii),
    Number(type = KeyboardType.Number),
    NumberPassword(type = KeyboardType.NumberPassword),
    Uri(type = KeyboardType.Uri),
    Text(type = KeyboardType.Text),
    Password(type = KeyboardType.Password),
    Email(type = KeyboardType.Email, buildTextFieldValue = { currentComposition, newValue, _, _ ->
        TextFieldValue(
            newValue.replace(" ", ""),
            TextRange(newValue.length),
            currentComposition
        )
    }),
    Phone(type = KeyboardType.Phone),
    Decimal(type = KeyboardType.Decimal),
    Date(
        type = KeyboardType.Phone,
        buildTextFieldValue = { _, newValue, previousValue, formatter ->
            val cursorPosition =
                if (newValue.length < previousValue.length) { // It's a delete
                    if (newValue.takeLast(1) == "/") newValue.length - 1 else newValue.length
                } else {
                    formatter(newValue).length
                }
            TextFieldValue(formatter(newValue), TextRange(cursorPosition))
        });
}