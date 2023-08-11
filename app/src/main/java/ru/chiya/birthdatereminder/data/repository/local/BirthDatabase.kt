package ru.chiya.birthdatereminder.data.repository.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.chiya.birthdatereminder.data.source.local.BirthdayEntity

@Database(
    entities = [BirthdayEntity::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class BirthDatabase: RoomDatabase() {

    abstract val birthDao: BirthDao

    companion object {
        const val DATABASE_NAME = "birth_db"
    }
}