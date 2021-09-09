package com.example.golladreamclient.ui.splash

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.golladreamclient.base.BaseSessionViewModel
import com.example.golladreamclient.data.ServerAPI
import com.example.golladreamclient.utils.SingleLiveEvent

class SplashViewModel(application: Application) : BaseSessionViewModel(application) {

    private val _onSuccessGettingToken = SingleLiveEvent<Boolean>()
    val onSuccessGettingToken: LiveData<Boolean> get() = _onSuccessGettingToken

    fun getUserStatus() {
        apiCall(userRepository.checkIdFromServer(authToken), {
            if (!it) _onSuccessGettingToken.postValue(true)
            else _onSuccessGettingToken.postValue(false)
        })
    }

}