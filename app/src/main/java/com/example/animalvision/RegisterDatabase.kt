package com.example.animalvision

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [PersonalInfo::class], version = 2,  exportSchema = false)
abstract class RegisterDatabase:RoomDatabase() {
    abstract val registerDatabaseDao: InfoDatabaseDao
    companion object {

        @Volatile
        private var INSTANCE: RegisterDatabase? = null

        fun getInstance(context: Context): RegisterDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        RegisterDatabase::class.java,
                        "info_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}