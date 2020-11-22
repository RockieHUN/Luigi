package com.example.luigi.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.luigi.adapters.MainDataAdapter
import com.example.luigi.R
import com.example.luigi.databinding.FragmentMainMenuBinding
import com.example.luigi.repository.ApiRepository
import com.example.luigi.viewModels.ApiViewModel
import com.example.luigi.viewModels.ApiViewModelFactory
import com.example.luigi.viewModels.MyDatabaseViewModel
import kotlinx.android.synthetic.main.fragment_main_menu.view.*
import java.util.*
import kotlin.concurrent.timerTask


class MainMenuFragment : Fragment(), MainDataAdapter.OnItemClickListener {

    private lateinit var binding: FragmentMainMenuBinding
    private lateinit var apiViewModel: ApiViewModel
    private lateinit var myDatabaseViewModel: MyDatabaseViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main_menu, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Add listener to the city selection button
        binding.appBarLayout.constraint_toolbarHolder.select_button.setOnClickListener{
            val dialog = MyDialogFragment()
            dialog.show(requireActivity().supportFragmentManager,"costumDialog")
        }

        //show bottom navigation menu
        requireActivity().findViewById<View>(R.id.bottom_navigaton).visibility = View.VISIBLE

        //API VIEWMODEL
        val repository = ApiRepository()
        val viewModelFactory = ApiViewModelFactory(repository)
        apiViewModel =
            ViewModelProvider(requireActivity(), viewModelFactory).get(ApiViewModel::class.java)

        //DATABASE VIEWMODEL
        myDatabaseViewModel = requireActivity().run {
            ViewModelProvider(requireActivity()).get(MyDatabaseViewModel::class.java)
        }


        /*
        When back button is pressed, start timer.
        If the user presses the button again under 2 seconds, exit from the application
        else reset the callbackCounter
         */
        var callbackCounter = 0

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            if (callbackCounter == 0) {
                Toast.makeText(requireContext(), "Press again to exit", Toast.LENGTH_SHORT).show()
                Timer().schedule(timerTask {
                    callbackCounter = 0
                }, 2000)

                callbackCounter++
            } else requireActivity().finish()
        }


        //OBSERVING THE DATA AND PASSING TO THE RECYCLE VIEW
        myDatabaseViewModel.restaurants.observe(requireActivity(), { restaurants ->
            binding.recycleView.adapter = MainDataAdapter(restaurants, this)
            binding.recycleView.layoutManager = LinearLayoutManager(context)
            binding.recycleView.setHasFixedSize(true)
        })
    }

    override fun onItemClick(position: Int) {
       myDatabaseViewModel.position = position
        findNavController().navigate(R.id.action_mainMenuFragment_to_detailFragment)
    }


}