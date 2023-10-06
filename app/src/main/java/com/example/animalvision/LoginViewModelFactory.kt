package com.example.animalvision

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class LoginViewModelFactory(
    val database: InfoDatabaseDao,
   val application: Application
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(database,application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}