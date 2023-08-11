package ru.chiya.birthdatereminder.domain.use_case

import ru.chiya.birthdatereminder.domain.repository.BirthRepository
import javax.inject.Inject

class GetAllCategoriesUseCase @Inject constructor(
    private val repository: BirthRepository
) {
    suspend operator fun invoke(): List<String> {
        return repository.getCategories()
    }
}