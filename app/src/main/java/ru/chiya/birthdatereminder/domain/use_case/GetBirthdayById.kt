package ru.chiya.birthdatereminder.domain.use_case

import ru.chiya.birthdatereminder.data.source.local.BirthdayEntity
import ru.chiya.birthdatereminder.domain.repository.BirthRepository
import javax.inject.Inject

class GetBirthdayById @Inject constructor(
    private val repository: BirthRepository
) {
    suspend operator fun invoke(id:Int): BirthdayEntity {
        return repository.getBirthdayById(id)
    }
}