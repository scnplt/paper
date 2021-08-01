package dev.sertan.android.paper.data.model

import java.util.*

internal data class Paper(
    var userUid: String = "",
    var title: String = "",
    var content: String = "",
    var updateDate: String = Calendar.getInstance().time.toString(),
    var createData: String = Calendar.getInstance().time.toString(),
    var uid: String = UUID.randomUUID().toString(),
)
