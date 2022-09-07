package dev.sertan.android.paper.domain.usecase

import dev.sertan.android.paper.common.util.Result
import dev.sertan.android.paper.domain.model.Email
import dev.sertan.android.paper.domain.repository.UserRepository
import javax.inject.Inject

class SendPasswordResetEmailUseCase @Inject constructor(
    private val userRepository: UserRepository
) {

    suspend operator fun invoke(email: String): Result<Boolean> {
        return userRepository.sendPasswordResetEmail(email = Email.create(emailAddress = email))
    }
}
