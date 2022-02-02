package dev.sertan.android.paper.ui.main

import dev.sertan.android.paper.util.Single

internal data class MainUiState(
    val noteDeleted: Single<Boolean> = Single(false),
    val message: Single<String> = Single(null)
)
