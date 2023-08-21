package ru.chiya.birthdatereminder.presentation.screen.info

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.chiya.birthdatereminder.data.source.local.BirthdayEntity
import ru.chiya.birthdatereminder.domain.use_case.GetBirthdayById
import javax.inject.Inject

@HiltViewModel
class InfoViewModel @Inject constructor(
    val getBirthdayById: GetBirthdayById
) : ViewModel() {
    private val _birthdayEntity = MutableLiveData<BirthdayEntity>()
    val birthdayEntity: LiveData<BirthdayEntity?> = _birthdayEntity

    fun getBirthday(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = getBirthdayById(id)
            _birthdayEntity.postValue(result)
        }
    }
}