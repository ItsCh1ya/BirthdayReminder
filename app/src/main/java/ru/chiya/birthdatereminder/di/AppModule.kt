package ru.chiya.birthdatereminder.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.chiya.birthdatereminder.common.BirthdayNotificationManager
import ru.chiya.birthdatereminder.data.repository.local.BirthDatabase
import ru.chiya.birthdatereminder.data.repository.local.BirthRepositoryImpl
import ru.chiya.birthdatereminder.domain.repository.BirthRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideBirthDatabase(app: Application): BirthDatabase {
        return Room.databaseBuilder(
            app,
            BirthDatabase::class.java,
            BirthDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideBirthRepository(db: BirthDatabase): BirthRepository {
        return BirthRepositoryImpl(db.birthDao)
    }

    @Provides
    @Singleton
    fun provideContext(application: Application): Context {
        return application.applicationContext
    }

    @Provides
    fun provideNotificationManager(context: Context): BirthdayNotificationManager {
        return BirthdayNotificationManager(context)
    }
}