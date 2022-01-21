package dev.sertan.android.paper.ui.register

import dev.sertan.android.paper.util.Single

internal data class RegisterUiState(
    var email: String = "",
    var password: String = "",
    var passwordConfirm: String = "",
    val isLoading: Boolean = false,
    val isRegistered: Boolean = false,
    val message: Single<String> = Single(null),
    val messageRes: Single<Int> = Single(null),
)
