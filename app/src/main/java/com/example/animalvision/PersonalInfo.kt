package com.example.animalvision

import android.provider.ContactsContract.CommonDataKinds.Email
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity("Personal_info_table")
data class PersonalInfo(
    @PrimaryKey(autoGenerate = true)
     var id: Long,
    @ColumnInfo("username")
    var username:String,
    @ColumnInfo("email")
    var email:String,
    @ColumnInfo("phno")
    var phno:String,
    @ColumnInfo("password")
    var password:String
)

