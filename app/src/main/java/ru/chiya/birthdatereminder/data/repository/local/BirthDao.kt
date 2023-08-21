package ru.chiya.birthdatereminder.data.repository.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import ru.chiya.birthdatereminder.data.source.local.BirthdayEntity

@Dao
interface BirthDao{
    @Query("SELECT * FROM birthday")
    fun getBirthdays(): List<BirthdayEntity>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertBirthday(birthday: BirthdayEntity)

    @Query("SELECT DISTINCT category FROM birthday")
    fun getCategories(): List<String>

    @Query("SELECT * FROM birthday WHERE id = :argId")
    fun getBirthdayById(argId: Int): BirthdayEntity

    @Update
    fun updateBirthday(birthday: BirthdayEntity)

    @Transaction
    fun updateBirthdayTransaction(birthday: BirthdayEntity) {
        updateBirthday(birthday)
    }

    @Delete
    fun deleteBirthday(birthday: BirthdayEntity)
}