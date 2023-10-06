package com.example.animalvision

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.example.animalvision.databinding.FragmentLoginBinding


//import androidx.lifecycle.ViewModelProviders

class Login : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val sharedPreferences = requireActivity().getSharedPreferences(
            getString(R.string.shared_preferences),
            Context.MODE_PRIVATE
        )

        val binding = DataBindingUtil.inflate<FragmentLoginBinding>(inflater, R.layout.fragment_login, container, false)

        val application = requireNotNull(this.activity).application
        val dataSource = RegisterDatabase.getInstance(application).registerDatabaseDao;

        val viewModelFactory = LoginViewModelFactory(dataSource, application)

        // Get a reference to the ViewModel associated with this fragment.
        val loginViewModel =
            ViewModelProviders.of(
                this, viewModelFactory).get(LoginViewModel::class.java)

        loginViewModel.lerrormessage.observe(viewLifecycleOwner, Observer {
            if(!loginViewModel.lerrormessage.value.isNullOrEmpty()&& !loginViewModel.lerrormessage.value.equals("checked") ) {
                Toast.makeText(application,loginViewModel.lerrormessage.value, Toast.LENGTH_LONG).show()
                loginViewModel.seterror()
            }

        })

        loginViewModel.lsuccess.observe(viewLifecycleOwner, Observer {
            if(it){
                sharedPreferences.edit().putBoolean("user_logged_in", true).apply()

                sharedPreferences.edit().putString("name", loginViewModel.data?.username)
                    .apply()
                sharedPreferences.edit().putString("email", loginViewModel.data?.email)
                    .apply()
                sharedPreferences.edit()
                    .putString("mobile_number", loginViewModel.data?.phno)?.apply()

//                Toast.makeText(application,"Successfully Inserted", Toast.LENGTH_LONG).show()
                loginViewModel.setsuccess()
                val intent = Intent(activity, DashboardActivity::class.java)
                startActivity(intent)

            }
        })

        binding.setLifecycleOwner(this)
        binding.lloginViewModel = loginViewModel

        binding.loginRegbtn.setOnClickListener {
            it.findNavController().navigate(R.id.action_login_to_register)
        }

        return binding.root
    }




}