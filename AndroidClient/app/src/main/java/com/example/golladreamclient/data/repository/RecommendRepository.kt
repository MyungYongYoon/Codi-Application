package com.example.golladreamclient.data.repository

import com.example.golladreamclient.data.AppDatabase
import com.example.golladreamclient.data.ServerAPI
import com.example.golladreamclient.data.model.InputData
import com.example.golladreamclient.data.model.OutputData
import io.reactivex.Single

class RecommendRepository() {
    companion object {
        private var sInstance: RecommendRepository? = null
        fun getInstance(): RecommendRepository {
            return sInstance
                ?: synchronized(this) {
                    val instance = RecommendRepository()
                    sInstance = instance
                    instance
                }
        }
    }

    fun postRecommendInput(data: InputData) : Single<OutputData> {
        return ServerAPI.create().postRecommendInput(data)
    }

}