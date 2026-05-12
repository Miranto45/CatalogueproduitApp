package com.ma.catalogueproduits.api

import com.ma.catalogueproduits.model.Produit
import retrofit2.http.*

// Interface Retrofit — appels HTTP vers le backend Laravel
// ANDRIAMAHERISON Tanteliniaina Miranto — Groupe M&A

interface ApiService {

    @GET("produits")
    suspend fun getProduits(): List<Produit>

    @GET("produits/{id}")
    suspend fun getProduit(@Path("id") id: Int): Produit

    @POST("produits")
    suspend fun createProduit(@Body produit: Produit): Produit

    @PUT("produits/{id}")
    suspend fun updateProduit(@Path("id") id: Int, @Body produit: Produit): Produit

    @DELETE("produits/{id}")
    suspend fun deleteProduit(@Path("id") id: Int)
}
