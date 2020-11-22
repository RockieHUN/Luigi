package com.example.luigi.repository

import androidx.lifecycle.LiveData
import com.example.luigi.model.CityRestaurants
import com.example.luigi.room.entities.EntityUser
import com.example.luigi.room.daos.MyDatabaseDao
import com.example.luigi.room.entities.EntityCity
import com.example.luigi.room.entities.EntityRestaurant
import java.sql.Timestamp
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

class MyDatabaseRepository(private val myDatabaseDao : MyDatabaseDao) {

    val readAllData : LiveData<List<EntityUser>> = myDatabaseDao.readAllData()

    suspend fun addUser(entityUser : EntityUser){
        myDatabaseDao.addUser(entityUser)
    }

    fun getUser(email : String, passwordHash: String) : EntityUser?{
        return myDatabaseDao.getUser(email,passwordHash)
    }

    /*
    Converting restaurants returned by api (CityRestaurants) to EntityRestaurant
    what will be added to the database with a TIMESTAMP using suspend function
     */
    suspend fun addRestaurants(restaurants : List<EntityRestaurant>){
        for (i in 0 until restaurants.size){

            //get timestamp
            val timestamp =  DateTimeFormatter
                    .ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS")
                    .withZone(ZoneOffset.UTC)
                    .format(Instant.now())

            restaurants[i].timestamp = timestamp

            myDatabaseDao.InsertRestaurant(restaurants[i])
        }
    }

    // Insert a list of cities to the database
    suspend fun addCities(cities : List<String>){
        for (i in 0 until cities.size){
            val city = EntityCity(0,cities[i])
            myDatabaseDao.InsertCity(city)
        }
    }


    /*
    Get the count of restaurants of a given city and page,
    available in the database
     */
    suspend fun getCount(city : String, page: Int) : Int{
        return myDatabaseDao.getCount(city, page)
    }

    /*
    Query the restaurants with the
    given city name and page
     */
    suspend fun getRestaurants(city : String, page: Int) : List<EntityRestaurant>{
        return myDatabaseDao.getRestaurants(city, page)
    }

    suspend fun deleteCache(){
        myDatabaseDao.deleteRestaurants()
        myDatabaseDao.deleteUsers()
    }

    //get the list of city names
    suspend fun getCityNames() : List<String>{
        return myDatabaseDao.getCityNames()
    }
}