package com.myhabbit.app.di

import android.content.Context
import androidx.room.Room
import com.myhabbit.app.data.local.dao.HabitDao
import com.myhabbit.app.data.local.dao.HabitRecordDao
import com.myhabbit.app.data.local.dao.SettingsDao
import com.myhabbit.app.data.local.dao.UserDao
import com.myhabbit.app.data.local.database.MyHabbitDatabase
import com.myhabbit.app.data.repository.HabitRecordRepositoryImpl
import com.myhabbit.app.data.repository.HabitRepositoryImpl
import com.myhabbit.app.domain.repository.HabitRecordRepository
import com.myhabbit.app.domain.repository.HabitRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): MyHabbitDatabase =
        Room.databaseBuilder(
            context,
            MyHabbitDatabase::class.java,
            MyHabbitDatabase.DATABASE_NAME
        ).build()

    @Provides
    fun provideHabitDao(database: MyHabbitDatabase): HabitDao = database.habitDao()

    @Provides
    fun provideHabitRecordDao(database: MyHabbitDatabase): HabitRecordDao =
        database.habitRecordDao()

    @Provides
    fun provideUserDao(database: MyHabbitDatabase): UserDao = database.userDao()

    @Provides
    fun provideSettingsDao(database: MyHabbitDatabase): SettingsDao = database.settingsDao()
}

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindHabitRepository(impl: HabitRepositoryImpl): HabitRepository

    @Binds
    @Singleton
    abstract fun bindHabitRecordRepository(impl: HabitRecordRepositoryImpl): HabitRecordRepository
}
