package com.example.luigi.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.luigi.model.Restaurant
import com.example.luigi.repository.Repository
import kotlinx.coroutines.launch

class ApiViewModel(private val repository: Repository): ViewModel() {

    val response : MutableLiveData<Restaurant> = MutableLiveData()

    fun getRestaurant(){
        viewModelScope.launch {
            val restaurant = repository.getRestaurant()
            response.value = restaurant
        }
    }
}