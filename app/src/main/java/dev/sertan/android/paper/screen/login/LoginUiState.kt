package dev.sertan.android.paper.screen.login

import dev.sertan.android.paper.common.util.OneTimeState

internal data class LoginUiState(
    val isLoading: Boolean = false,
    val isLoggedIn: Boolean = false,
    val emailAddress: String = "",
    val password: String = "",
    val exceptionMessage: OneTimeState<String> = OneTimeState()
)
