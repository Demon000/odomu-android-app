package com.demon000.odomu.services

import androidx.lifecycle.MutableLiveData
import com.demon000.odomu.dependencies.DependencyLocator
import com.demon000.odomu.models.Area
import com.demon000.odomu.models.AreaAddData
import com.demon000.odomu.models.AreaUpdateData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AreaService {
    fun getAreaCategories(): MutableLiveData<Map<Number, String>?> {
        val areaCategoriesObservable = MutableLiveData<Map<Number, String>?>()

        DependencyLocator.areaAPI.areasGetCategories().enqueue(object: Callback<Map<Number, String>> {
            override fun onResponse(
                call: Call<Map<Number, String>>,
                response: Response<Map<Number, String>>
            ) {
                areaCategoriesObservable.postValue(response.body())
            }

            override fun onFailure(call: Call<Map<Number, String>>, t: Throwable) {
                areaCategoriesObservable.postValue(null)
            }

        })

        return areaCategoriesObservable
    }

    fun getAreas(): MutableLiveData<List<Area>?> {
        val areasObservable = MutableLiveData<List<Area>?>()

        DependencyLocator.areaAPI.areasGet().enqueue(object: Callback<List<Area>> {
            override fun onResponse(call: Call<List<Area>>, response: Response<List<Area>>) {
                areasObservable.postValue(response.body())
            }

            override fun onFailure(call: Call<List<Area>>, t: Throwable) {
                areasObservable.postValue(null)
            }
        })

        return areasObservable
    }

    fun addArea(data: AreaAddData): MutableLiveData<Area?> {
        val areaObservable = MutableLiveData<Area?>()

        DependencyLocator.areaAPI.areasPost(data).enqueue(object: Callback<Area> {
            override fun onResponse(call: Call<Area>, response: Response<Area>) {
                areaObservable.postValue(response.body())
            }

            override fun onFailure(call: Call<Area>, t: Throwable) {
                areaObservable.postValue(null)
            }
        })

        return areaObservable
    }

    fun getArea(id: String): MutableLiveData<Area?> {
        val areaObservable = MutableLiveData<Area?>()

        DependencyLocator.areaAPI.areasGetArea(id).enqueue(object: Callback<Area> {
            override fun onResponse(call: Call<Area>, response: Response<Area>) {
                areaObservable.postValue(response.body())
            }

            override fun onFailure(call: Call<Area>, t: Throwable) {
                areaObservable.postValue(null)
            }
        })

        return areaObservable
    }

    fun updateArea(id: String, data: AreaUpdateData): MutableLiveData<Area?> {
        val areaObservable = MutableLiveData<Area?>()

        DependencyLocator.areaAPI.areasPatchArea(id, data).enqueue(object: Callback<Area> {
            override fun onResponse(call: Call<Area>, response: Response<Area>) {
                areaObservable.postValue(response.body())
            }

            override fun onFailure(call: Call<Area>, t: Throwable) {
                areaObservable.postValue(null)
            }
        })

        return areaObservable
    }

    fun deleteArea(id: String): MutableLiveData<Unit?> {
        val areaObservable = MutableLiveData<Unit?>()

        DependencyLocator.areaAPI.areasDeleteArea(id).enqueue(object: Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                areaObservable.postValue(response.body())
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                areaObservable.postValue(null)
            }
        })

        return areaObservable
    }
}
