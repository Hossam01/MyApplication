package com.example.myapplication.repositry

import com.example.myapplication.api.ApiService
import com.example.myapplication.others.DataManager
import javax.inject.Inject


class MainRepository @Inject constructor(
    private val apiService: ApiService,
    private val dataManager: DataManager

){
    suspend fun getArticales() = apiService.getData()

    suspend fun add(name:String){
        dataManager.storeData(name)
    }

    fun getData()=dataManager.userNameFlow

}