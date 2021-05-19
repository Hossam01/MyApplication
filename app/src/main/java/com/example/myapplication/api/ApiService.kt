package com.example.myapplication.api

import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("/")
    suspend fun getData():Response<String>
}