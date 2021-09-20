package com.example.golladreamclient.data.repository

import com.example.golladreamclient.data.ServerAPI
import com.example.golladreamclient.data.model.ReceiverRecommendOutput
import com.example.golladreamclient.data.model.ReceiverSaveInput
import io.reactivex.Single
import okhttp3.RequestBody

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

    fun postRecommendInput(mapData: HashMap<String, RequestBody>) : Single<ReceiverSaveInput> {
        return ServerAPI.create().postRecommendInput(mapData)
    }

    fun getRecommendOutput(id : String, style : String, image : String) : Single<ReceiverRecommendOutput> {
        return ServerAPI.create().getRecommendOutput(id, style, image)
    }

}