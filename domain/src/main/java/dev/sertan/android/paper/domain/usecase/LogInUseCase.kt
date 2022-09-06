package dev.sertan.android.paper.domain.usecase

import dev.sertan.android.paper.common.util.Result
import dev.sertan.android.paper.domain.model.Email
import dev.sertan.android.paper.domain.model.Password
import dev.sertan.android.paper.domain.repository.UserRepository
import javax.inject.Inject

class LogInUseCase @Inject constructor(private val userRepository: UserRepository) {

    suspend operator fun invoke(email: String, password: String): Result<Boolean> {
        return userRepository.logIn(
            email = Email.create(emailAddress = email),
            password = Password.create(pwd = password)
        )
    }
}
