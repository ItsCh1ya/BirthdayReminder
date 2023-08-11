package ru.chiya.birthdatereminder.domain.use_case

import ru.chiya.birthdatereminder.data.source.local.BirthdayEntity
import ru.chiya.birthdatereminder.domain.repository.BirthRepository
import javax.inject.Inject

class GetAllBirthdaysUseCase @Inject constructor(
    private val repository: BirthRepository
) {
    suspend operator fun invoke(): List<BirthdayEntity> {
        return repository.getBirthdays()
    }
}