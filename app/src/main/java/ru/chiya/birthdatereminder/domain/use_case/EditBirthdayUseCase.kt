package ru.chiya.birthdatereminder.domain.use_case

import ru.chiya.birthdatereminder.common.BirthdayNotificationManager
import ru.chiya.birthdatereminder.data.source.local.BirthdayEntity
import ru.chiya.birthdatereminder.domain.repository.BirthRepository
import javax.inject.Inject

class EditBirthdayUseCase @Inject constructor(
    private val repository: BirthRepository,
    private val notificationManager: BirthdayNotificationManager
) {
    suspend operator fun invoke(birthdayEntity: BirthdayEntity){
        repository.editBirthday(birthdayEntity)
        notificationManager.updateBirthdayNotification(birthdayEntity)
    }
}