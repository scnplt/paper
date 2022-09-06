package dev.sertan.android.paper.data.mapper

import com.google.firebase.auth.FirebaseUser
import dev.sertan.android.paper.data.model.NetworkUser
import dev.sertan.android.paper.domain.model.Email
import dev.sertan.android.paper.domain.model.Password
import dev.sertan.android.paper.domain.model.UserDto

internal fun FirebaseUser?.toNetworkUser(): NetworkUser? {
    return this?.let {
        NetworkUser(
            uid = uid,
            email = email.orEmpty(),
            password = null
        )
    }
}

internal fun NetworkUser?.toUserDto(): UserDto? {
    return this?.run {
        UserDto(
            uid = uid,
            email = Email.create(email),
            password = password?.let { Password.create(pwd = it) }
        )
    }
}
