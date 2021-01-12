package com.demon000.odomu.api

import com.demon000.odomu.models.User
import com.demon000.odomu.models.UserLoginData
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface UserAPIInterface {
    @POST("user/login")
    fun postUserLoginData(@Body loginData: UserLoginData): Call<User>

    @GET("user")
    fun getUser(): Call<User>
}
