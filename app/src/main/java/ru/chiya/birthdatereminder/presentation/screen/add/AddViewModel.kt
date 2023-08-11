package ru.chiya.birthdatereminder.presentation.screen.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.chiya.birthdatereminder.data.source.local.BirthdayEntity
import ru.chiya.birthdatereminder.domain.use_case.AddBirthdayUseCase
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date
import javax.inject.Inject


@HiltViewModel
class AddViewModel @Inject constructor(
    val addBirthdayUseCase: AddBirthdayUseCase
) : ViewModel() {
    fun addBirthday(name: String, description: String, category: String, date: LocalDate) {
        val newDate = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant())

        viewModelScope.launch(Dispatchers.IO) {
            addBirthdayUseCase(
                BirthdayEntity(
                    name, description, newDate, category
                )
            )
        }
    }
}