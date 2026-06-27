package com.example.pawmesh.network

import retrofit2.http.GET

interface ApiService {

    @GET("dogs/nearby")
    suspend fun getNearbyDogs(): Any

    @GET("friends")
    suspend fun getFriends(): Any
}