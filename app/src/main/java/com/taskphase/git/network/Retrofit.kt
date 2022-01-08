package com.taskphase.git.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val base_url = "https://api.github.com/repos/jquery/"

class Retrofit {

    companion object {

        private val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(base_url)
            .build()

        fun api(): EndPoints{
            return retrofit.create(EndPoints::class.java)
        }
    }
}