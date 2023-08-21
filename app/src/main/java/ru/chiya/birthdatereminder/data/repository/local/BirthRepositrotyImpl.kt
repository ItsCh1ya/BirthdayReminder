package ru.chiya.birthdatereminder.data.repository.local

import ru.chiya.birthdatereminder.data.source.local.BirthdayEntity
import ru.chiya.birthdatereminder.domain.repository.BirthRepository

class BirthRepositoryImpl(
    private val dao: BirthDao
) : BirthRepository {
    override suspend fun getBirthdays(): List<BirthdayEntity> {
        return dao.getBirthdays()
    }

    override suspend fun getBirthdayById(id: Int): BirthdayEntity {
        return dao.getBirthdayById(id)
    }

    override suspend fun insertBirthday(birthday: BirthdayEntity) {
        dao.insertBirthday(
            birthday
        )
    }

    override suspend fun getCategories(): List<String> {
        return dao.getCategories()
    }

    override suspend fun editBirthday(birthday: BirthdayEntity) {
        dao.updateBirthday(birthday)
    }

    override suspend fun deleteBirthday(birthday: BirthdayEntity) {
        dao.deleteBirthday(birthday)
    }
}