package ru.chiya.birthdatereminder.data.source.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "birthday")
data class BirthdayEntity (
    val name: String,
    val description: String,
    val birthday: Date,
    val category: String,
    @PrimaryKey(autoGenerate = true) var id: Int = 0
)