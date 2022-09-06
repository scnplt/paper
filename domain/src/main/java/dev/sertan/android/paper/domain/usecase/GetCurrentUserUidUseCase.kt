package dev.sertan.android.paper.domain.usecase

import dev.sertan.android.paper.common.util.Result
import dev.sertan.android.paper.domain.repository.UserRepository
import javax.inject.Inject

class GetCurrentUserUidUseCase @Inject constructor(private val userRepository: UserRepository) {

    suspend operator fun invoke(): Result<String> = userRepository.getCurrentUser().map { it?.uid }
}
