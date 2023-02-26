package com.github.mateo762.myapplication

import retrofit2.Response
import retrofit2.http.GET

interface BoredApi {
    @GET("activity")
    suspend fun getActivity(): Response<BoredActivityData>
}