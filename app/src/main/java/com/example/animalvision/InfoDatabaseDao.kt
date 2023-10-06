package com.example.animalvision

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface InfoDatabaseDao {

    @Insert
    fun insert(info:PersonalInfo)

    @Query("Select Count(*) from Personal_info_table where username=:key")
    fun checkusernameexist(key:String):Int

    @Query("Select *from Personal_info_table where username=:key")
    fun getEntry(key:String):PersonalInfo


    @Query("Delete from Personal_info_table")
    fun del()
}
