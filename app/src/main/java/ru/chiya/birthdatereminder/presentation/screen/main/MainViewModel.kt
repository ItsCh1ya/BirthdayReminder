package ru.chiya.birthdatereminder.presentation.screen.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.chiya.birthdatereminder.data.source.local.BirthdayEntity
import ru.chiya.birthdatereminder.domain.model.DatePeriod
import ru.chiya.birthdatereminder.domain.model.Filter
import ru.chiya.birthdatereminder.domain.use_case.DeleteBirthdayUseCase
import ru.chiya.birthdatereminder.domain.use_case.EditBirthdayUseCase
import ru.chiya.birthdatereminder.domain.use_case.GetAllBirthdaysUseCase
import ru.chiya.birthdatereminder.domain.use_case.GetAllCategoriesUseCase
import java.time.ZoneId
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getAllBirthdaysUseCase: GetAllBirthdaysUseCase,
    private val getAllCategoriesUseCase: GetAllCategoriesUseCase,
    private val deleteBirthdayUseCase: DeleteBirthdayUseCase,
    private val editBirthdayUseCase: EditBirthdayUseCase
) : ViewModel() {

    private val _birthdaysList: MutableLiveData<List<BirthdayEntity>> = MutableLiveData()
    val birthdaysList: LiveData<List<BirthdayEntity>> get() = _birthdaysList

    private val _categoriesList: MutableLiveData<List<String>> = MutableLiveData()
    val categoriesList: LiveData<List<String>> get() = _categoriesList

    var filter: Filter = Filter(null, null, mutableListOf(), null)

    init {
        fetchBirthdays()
        fetchCategories()
    }

    fun fetchBirthdays() {
        viewModelScope.launch(Dispatchers.IO) {
            val birthdays = getAllBirthdaysUseCase()
            _birthdaysList.postValue(birthdays)
        }
    }

    fun fetchCategories() {
        viewModelScope.launch(Dispatchers.IO) {
            val categories = getAllCategoriesUseCase()
            _categoriesList.postValue(categories)
        }
    }

    fun filterBirthdays() {
        viewModelScope.launch(Dispatchers.IO) {
            val birthdays = getAllBirthdaysUseCase()
            val filteredList = birthdays.filter { birthdayEntity ->
                (filter.name == null || birthdayEntity.name.contains(filter.name.toString())) &&
                (filter.description == null || filter.description == birthdayEntity.description ) &&
                (filter.category.isEmpty() || filter.category.contains(birthdayEntity.category)) &&
                (filter.datePeriod == null || isDateWithinPeriod(birthdayEntity.birthday, filter.datePeriod!!))
            }
            _birthdaysList.postValue(filteredList)
            Log.d("List", filteredList.toString())
        }
    }
    private fun isDateWithinPeriod(date: Date, datePeriod: DatePeriod): Boolean {
        val convertedDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
        return !convertedDate.isAfter(datePeriod.to) && !convertedDate.isBefore(datePeriod.from)
    }

    fun deleteBirthday(birthdayEntity: BirthdayEntity){
        viewModelScope.launch (Dispatchers.IO){
            deleteBirthdayUseCase(birthdayEntity)
            fetchBirthdays()
            fetchCategories()
        }
    }

    fun editBirthday(birthdayEntity: BirthdayEntity){
        viewModelScope.launch (Dispatchers.IO) {
            editBirthdayUseCase(birthdayEntity)
            fetchBirthdays()
            fetchCategories()
        }
    }
}