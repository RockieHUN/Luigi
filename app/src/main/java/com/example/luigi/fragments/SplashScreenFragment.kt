package com.example.luigi.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.luigi.R
import com.example.luigi.databinding.FragmentSplashScreenBinding
import com.example.luigi.room.UserDatabase
import com.example.luigi.viewModels.UserViewModel
import java.math.BigInteger
import java.security.MessageDigest
import java.util.*

class SplashScreenFragment : Fragment() {
    private lateinit var binding : FragmentSplashScreenBinding
    private lateinit var sharedPref : SharedPreferences
    private lateinit var userViewModel : UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding= DataBindingUtil.inflate(inflater, R.layout.fragment_splash_screen,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //get userViewModel
        userViewModel = requireActivity().run {
            ViewModelProvider(requireActivity()).get(UserViewModel::class.java)
        }

        //try to login after 2 seconds
        Timer().schedule(object : TimerTask() {
            override fun run() {

                sharedPref = requireContext().getSharedPreferences("credentials", Context.MODE_PRIVATE)
                val credentials = sharedPref.all
                login(credentials)

            }
        }, 2000)
    }

    // ***************************** PRIVATE FUNCTIONS ***********************

    //attempting to login. if it fails then navigate to register
    private fun login(preferences:MutableMap<String,*>){

        //check if credentials already exists
        if (preferences.containsKey("email") && preferences.containsKey("password")) {
            var user = userViewModel.getUser(preferences["email"] as String , preferences["password"] as String)

            //check if user exists in the database
            if (user != null) findNavController().navigate(R.id.action_splashScreenFragment_to_mainMenuFragment)
            else{
                sharedPref.edit().clear().apply()
                findNavController().navigate(R.id.action_splashScreenFragment_to_register2)
            }
        }
        else{
            sharedPref.edit().clear().apply()
            findNavController().navigate(R.id.action_splashScreenFragment_to_register2)
        }
    }



    }

