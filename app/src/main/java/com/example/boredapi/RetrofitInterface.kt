package com.example.boredapi

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface RetrofitInterface {

    companion object {
        val BASE_URL = "https://www.boredapi.com/api/"

        val api by lazy {
            Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create())
                .build().create(RetrofitInterface::class.java)
        }
    }

    @GET("activity")
    suspend fun getActivity(): GetActivity
}