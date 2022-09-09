package dev.sertan.android.paper.data.mapper

import dev.sertan.android.paper.data.model.NetworkUser
import java.util.UUID
import org.junit.Test

internal class UserMapperTest {

    @Test
    fun `networkUser to userDto`() {
        val networkUser = NetworkUser(
            uid = UUID.randomUUID().toString(),
            email = "test@test.com",
            password = "12345678"
        )

        val userDto = networkUser.toUserDto()
        assert(userDto?.uid == networkUser.uid)
        assert(userDto?.email.toString() == networkUser.email)
        assert(userDto?.password.toString() == networkUser.password)
    }
}
