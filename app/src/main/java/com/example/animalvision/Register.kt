package com.example.animalvision

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.example.animalvision.databinding.FragmentRegisterBinding

class Register : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = DataBindingUtil.inflate<FragmentRegisterBinding>(inflater, R.layout.fragment_register, container, false)

        val application = requireNotNull(this.activity).application
        val dataSource = RegisterDatabase.getInstance(application).registerDatabaseDao;

        val regviewModelFactory = RegisterViewModelFactory(dataSource, application)

        val sharedPreferences = requireActivity().getSharedPreferences(
            getString(R.string.shared_preferences),
            Context.MODE_PRIVATE
        )

        // Get a reference to the ViewModel associated with this fragment.
        val registerViewModel =
            ViewModelProviders.of(
                this, regviewModelFactory).get(RegisterViewModel::class.java)

        registerViewModel.errormessage.observe(viewLifecycleOwner, Observer {
            if(!registerViewModel.errormessage.value.isNullOrEmpty()&& !registerViewModel.errormessage.value.equals("checked") ) {
                Toast.makeText(application,registerViewModel.errormessage.value,Toast.LENGTH_LONG).show()
                registerViewModel.seterror()
            }

        })

        registerViewModel.success.observe(viewLifecycleOwner, Observer {
            if(it){
                Toast.makeText(application,"Successfully Inserted",Toast.LENGTH_LONG).show()
                registerViewModel.setsuccess()
                binding.signupLoginbtn.performClick();
            }
        })
        binding.setLifecycleOwner(this)
        binding.rregisterViewModel = registerViewModel

        binding.signupLoginbtn.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_register_to_login2)
        )

        return binding.root
    }
}

