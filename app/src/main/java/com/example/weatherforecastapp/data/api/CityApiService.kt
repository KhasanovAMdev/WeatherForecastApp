package com.example.weatherforecastapp.data.api

import com.example.weatherforecastapp.data.model.CityResponse
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface CityApiService {
    @Headers("X-Api-Key: X2ocPpyyTRS9JfWzg84uMA==5k0OdywDYapnhOOw")
    @GET("city")
    suspend fun getCityCoordinates(
        @Query("name") cityName: String
    ): List<CityResponse>
}