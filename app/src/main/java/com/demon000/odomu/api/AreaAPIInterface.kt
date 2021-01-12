package com.demon000.odomu.api

import com.demon000.odomu.models.Area
import com.demon000.odomu.models.AreaAddData
import com.demon000.odomu.models.AreaUpdateData
import retrofit2.Call
import retrofit2.http.*

interface AreaAPIInterface {
    @GET("areas/categories")
    fun areasGetCategories(): Call<Map<Number, String>>

    @GET("areas/all")
    fun areasGet(): Call<List<Area>>

    @POST("areas")
    fun areasPost(
        @Body
        data: AreaAddData,


    ): Call<Area>

    @GET("areas/{id}")
    fun areasGetArea(
        @Path("id")
        id: String,
    ): Call<Area>

    @PATCH("areas/{id}")
    fun areasPatchArea(
        @Path("id")
        id: String,

        @Body
        data: AreaUpdateData,
    ): Call<Area>

    @DELETE("areas/{id}")
    fun areasDeleteArea(
        @Path("id")
        id: String,
    ): Call<Unit>
}
