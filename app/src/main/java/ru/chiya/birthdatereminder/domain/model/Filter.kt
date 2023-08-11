package ru.chiya.birthdatereminder.domain.model

import java.time.LocalDate

data class Filter(
    var name: String?,
    var description: String?,
    var category: MutableList<String>,
    var datePeriod: DatePeriod?
)

data class DatePeriod(
    val from: LocalDate,
    val to: LocalDate
)