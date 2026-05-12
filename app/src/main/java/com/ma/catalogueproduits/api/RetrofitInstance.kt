package com.ma.catalogueproduits.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// Singleton de connexion au serveur Laravel
// 10.0.2.2 = localhost depuis l'émulateur Android
// Vrai téléphone : remplacer par l'IP de la machine ex: http://192.168.1.X:8000/api/

object RetrofitInstance {

    private const val BASE_URL = "http://127.0.0.1:8000/api/"

    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
