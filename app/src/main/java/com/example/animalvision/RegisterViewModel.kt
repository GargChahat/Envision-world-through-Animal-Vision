package com.example.animalvision

import android.app.Application
import android.util.Log
import android.util.Patterns

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class RegisterViewModel(
    val database: InfoDatabaseDao,
    application: Application
) : AndroidViewModel(application) {
    private var _success= MutableLiveData<Boolean>()
    val success: LiveData<Boolean>
        get()= _success;

    private var _errormessage= MutableLiveData<String?>()
    val errormessage: LiveData<String?>
        get()= _errormessage;

    var inputusername=MutableLiveData<String?>()
    var inputemail=MutableLiveData<String?>()
    var inputphoneno=MutableLiveData<String?>()
    var inputpassword=MutableLiveData<String?>()
    var cnfminputpassword=MutableLiveData<String?>()

    init{
        _success.value=false
        _errormessage.value=null
    }

    override fun onCleared() {
        super.onCleared()
        RegisterviewModelJob.cancel()
    }

    private var RegisterviewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main +  RegisterviewModelJob)

    fun onSubmit(){

        if(inputusername.value==null)
            _errormessage.value="Username empty";

        else if(inputemail.value==null)
            _errormessage.value="Email empty";
        else if(!Patterns.EMAIL_ADDRESS.matcher(inputemail.value).matches())
            _errormessage.value="Email address not valid";
        else if(inputphoneno.value==null)
            _errormessage.value="Contact number empty";
        else if(inputphoneno.value!!.length!=10)
            _errormessage.value="Not valid contact number"
        else if(inputpassword.value==null)
            _errormessage.value="Password empty";
        else if(inputpassword.value!!.length<6)
            _errormessage.value="Weak password";
        else if(inputpassword.value!=cnfminputpassword.value)
            _errormessage.value="Password match failed";
        else{
            usernameExist()
        }

    }

fun insertToDB(){
    uiScope.launch {
        val name= inputusername.value!!
        val mail=inputemail.value!!
        val contact=inputphoneno.value!!
        val pass=inputpassword.value!!
        val newdata = PersonalInfo(0,name,mail,contact,pass);

        insertfun(newdata);
        _success.value=true;
    }
}

    private suspend fun insertfun(data: PersonalInfo){
        withContext(Dispatchers.IO) {

            database.insert(data)

        }
    }

    private suspend fun check(data:String){
        var count= withContext(Dispatchers.IO) {
           ( database.checkusernameexist(data))

        }

        if(count != 0)
            _errormessage.value="Username exists";
        else
            insertToDB();

    }
    fun usernameExist(){
     runBlocking{
       uiScope.launch {
           check(inputusername.value!!)
       }
        }
    }

    fun seterror(){
        _errormessage.value="checked"
    }
    fun setsuccess(){
        _success.value=false
        inputusername.value=null
        inputemail.value=null
         inputphoneno.value=null
         inputpassword.value=null
         cnfminputpassword.value=null
    }
}