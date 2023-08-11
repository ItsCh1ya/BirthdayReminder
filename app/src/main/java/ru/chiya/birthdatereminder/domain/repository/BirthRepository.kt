package ru.chiya.birthdatereminder.domain.repository

import ru.chiya.birthdatereminder.data.source.local.BirthdayEntity

interface BirthRepository {
    suspend fun getBirthdays(): List<BirthdayEntity>
    suspend fun insertBirthday(birthday: BirthdayEntity)
    suspend fun getCategories(): List<String>
    suspend fun editBirthday(birthday: BirthdayEntity)
    suspend fun deleteBirthday(birthday: BirthdayEntity)
}