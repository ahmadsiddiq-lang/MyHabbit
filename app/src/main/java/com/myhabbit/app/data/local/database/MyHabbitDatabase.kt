package com.myhabbit.app.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.myhabbit.app.data.local.dao.HabitDao
import com.myhabbit.app.data.local.dao.HabitRecordDao
import com.myhabbit.app.data.local.dao.SettingsDao
import com.myhabbit.app.data.local.dao.UserDao
import com.myhabbit.app.data.local.entity.HabitEntity
import com.myhabbit.app.data.local.entity.HabitRecordEntity
import com.myhabbit.app.data.local.entity.SettingsEntity
import com.myhabbit.app.data.local.entity.UserEntity

@Database(
    entities = [
        HabitEntity::class,
        HabitRecordEntity::class,
        UserEntity::class,
        SettingsEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class MyHabbitDatabase : RoomDatabase() {

    abstract fun habitDao(): HabitDao

    abstract fun habitRecordDao(): HabitRecordDao

    abstract fun userDao(): UserDao

    abstract fun settingsDao(): SettingsDao

    companion object {
        const val DATABASE_NAME = "myhabbit.db"
    }
}
