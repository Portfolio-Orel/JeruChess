package com.orels.jeruchess.android.presentation.login.components.forgot_password

import androidx.lifecycle.ViewModel
import com.orels.jeruchess.core.util.toCommonStateFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

/**
 * @author Orel Zilberman
 * 28/09/2022
 */

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
) : ViewModel() {

    private val _state = MutableStateFlow(ForgotPasswordState())
    val state = _state.toCommonStateFlow()

    fun forgotPassword() {

    }

    fun insertUsername(username: String) {
        _state.value = _state.value.copy(username = username)
    }

    fun insertCodeAndPasswords(code: String, password: String, confirmPassword: String) {
    }
}