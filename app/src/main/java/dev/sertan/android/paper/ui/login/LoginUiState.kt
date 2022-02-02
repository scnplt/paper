package dev.sertan.android.paper.ui.login

import dev.sertan.android.paper.util.Single

internal data class LoginUiState(
    var email: String = "",
    var password: String = "",
    val isLoading: Boolean = false,
    val message: Single<String> = Single(null),
    val messageRes: Single<Int> = Single(null),
)
