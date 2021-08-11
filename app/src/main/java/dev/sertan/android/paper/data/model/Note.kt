package dev.sertan.android.paper.data.model

import android.os.Parcelable
import dev.sertan.android.paper.util.Utils
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.util.*

@Parcelize
data class Note(
    var userUid: String = "",
    var title: String = "",
    var content: String = "",
    var updateDate: String = Utils.getCurrentDate(),
    var createDate : String = Utils.getCurrentDate(),
    val timestamp: Long = Calendar.getInstance().timeInMillis,
    var uid: String = UUID.randomUUID().toString(),
) : Parcelable
