package com.demon000.odomu.services

import androidx.lifecycle.MutableLiveData
import com.demon000.odomu.dependencies.DependencyLocator
import com.demon000.odomu.models.User
import com.demon000.odomu.models.UserLoginData

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserService {
    val userObservable = MutableLiveData<User?>()

    var accessToken: String?
        get() {
            return DependencyLocator.userRepository.accessToken
        }
        set(value) {
            DependencyLocator.userRepository.accessToken = value
        }

    var refreshToken: String?
        get() {
            return DependencyLocator.userRepository.refreshToken
        }
        set(value) {
            DependencyLocator.userRepository.refreshToken = value
        }

    fun loginUser(data: UserLoginData): MutableLiveData<User> {
        val userObservable = MutableLiveData<User>()

        DependencyLocator.userAPI.postUserLoginData(data).enqueue(object: Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                userObservable.postValue(response.body())
                this@UserService.userObservable.postValue(response.body())
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                userObservable.postValue(null)
                this@UserService.userObservable.postValue(null)
            }
        })

        return userObservable
    }

    fun logoutUser() {
        accessToken = null
        refreshToken = null
        this@UserService.userObservable.postValue(null)
    }

    fun getLoggedInUser(): MutableLiveData<User> {
        val userObservable = MutableLiveData<User>()

        DependencyLocator.userAPI.getUser().enqueue(object: Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                userObservable.postValue(response.body())
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                userObservable.postValue(null)
            }
        })

        return userObservable
    }
}
