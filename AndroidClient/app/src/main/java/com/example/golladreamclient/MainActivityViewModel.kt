package com.example.golladreamclient

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.golladreamclient.base.BaseActivityViewModel
import com.example.golladreamclient.data.AppDatabase
import com.example.golladreamclient.data.model.UserModel
import com.example.golladreamclient.data.repository.UserRepository
import com.example.golladreamclient.utils.SingleLiveEvent


open class MainActivityViewModel(application: Application) : BaseActivityViewModel(application) {

    private val userRepository : UserRepository = UserRepository.getInstance(AppDatabase.getDatabase(application, viewModelScope))

    private val _onSuccessGettingUserInfo = SingleLiveEvent<UserModel>()
    val onSuccessGettingUserInfo : LiveData<UserModel> get() = _onSuccessGettingUserInfo
    private val _onSuccessGettingNullUserInfo = SingleLiveEvent<UserModel>()
    val onSuccessGettingNullUserInfo : LiveData<UserModel> get() = _onSuccessGettingNullUserInfo

    fun getUserInfo()  {
        apiCall(userRepository.getUserInfo(), {
            _onSuccessGettingUserInfo.postValue(it) },
            { _onSuccessGettingNullUserInfo.call() })
    }

}