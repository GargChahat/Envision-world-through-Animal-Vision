package com.example.animalvision

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
//import animalVision.R
import com.example.animalvision.databinding.FragmentProfileBinding
import com.example.animalvision.MainActivity

class ProfileFragment(val c: Context) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = DataBindingUtil.inflate<FragmentProfileBinding>(inflater, R.layout.fragment_profile, container, false)

        val sharedPreferences =
            c.getSharedPreferences(getString(R.string.shared_preferences), Context.MODE_PRIVATE)

       binding.textViewName.text = sharedPreferences.getString("name", "")
        binding.textViewEmail.text = sharedPreferences.getString("email", "")
        binding.textViewMobileNumber.text = "+91-" + sharedPreferences.getString("mobile_number", "")


        binding.proButtonLogout.setOnClickListener {
            val alterDialog = androidx.appcompat.app.AlertDialog.Builder(activity as Context)

            alterDialog.setTitle("Alert!")
            alterDialog.setMessage("Are you sure you want to log out?")
            alterDialog.setPositiveButton("Yes") { text, listener ->
                sharedPreferences.edit().putBoolean("user_logged_in", false).apply()

                val intent = Intent(context, MainActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            }

            alterDialog.setNegativeButton("No") { text, listener ->

            }
            alterDialog.create()
            alterDialog.show()
        }

        return binding.root
    }
}