package com.example.animalvision

import android.app.Application
import android.util.Patterns
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class LoginViewModel(
    val database: InfoDatabaseDao,
     application: Application
) : AndroidViewModel(application) {

    private var _success = MutableLiveData<Boolean>()
    val lsuccess: LiveData<Boolean>
        get() = _success;

    private var _errormessage = MutableLiveData<String?>()
    val lerrormessage: LiveData<String?>
        get() = _errormessage;

    init {
        _success.value = false
        _errormessage.value = null
    }

    override fun onCleared() {
        super.onCleared()
        LoginviewModelJob.cancel()
    }

    private var LoginviewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + LoginviewModelJob)

    var linputusername = MutableLiveData<String>()
    var userpassword = MutableLiveData<String>()
     var  data:PersonalInfo? = null

    fun onSubmit() {
        if (linputusername.value == null)
            _errormessage.value = "Username empty";
        else if (userpassword.value == null)
            _errormessage.value = "Password empty";
        else {
            usernameExist()
        }
    }

    private suspend fun insertfun(data: PersonalInfo) {
        withContext(Dispatchers.IO) {
            database.insert(data)
        }
    }

    private suspend fun matchPassword(username:String){
        data=withContext(Dispatchers.IO){
            database.getEntry(username)
        }

        if(data?.password!=userpassword.value)
            _errormessage.value="Password not matched"
        else
            _success.value=true
    }

    private suspend fun check(data: String) {
        var count = withContext(Dispatchers.IO) {
            (database.checkusernameexist(data))

        }

        if (count == 0)
            _errormessage.value = "Username does not exists";
        else {
            runBlocking {
                uiScope.launch {
                    matchPassword(linputusername.value!!)
                }
            }
        }
    }

    fun usernameExist() {
        runBlocking {
            uiScope.launch {
                check(linputusername.value!!)
            }
        }
    }


    fun seterror(){
        _errormessage.value="checked"
    }
    fun setsuccess(){
        _success.value=false
         data=null
    }
}
