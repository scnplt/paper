package dev.sertan.android.paper.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class Note(
    var title: String = "",
    var content: String = "",
    var userUid: String = "",
    var updateDate: Long = System.currentTimeMillis(),
    var createDate: Long = System.currentTimeMillis(),
    var uid: String = UUID.randomUUID().toString(),
) : Parcelable
