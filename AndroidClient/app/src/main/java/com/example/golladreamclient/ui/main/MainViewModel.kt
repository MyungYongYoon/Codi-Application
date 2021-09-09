package com.example.golladreamclient.ui.main

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.golladreamclient.base.BaseSessionViewModel
import com.example.golladreamclient.data.model.UserModel
import com.example.golladreamclient.utils.SingleLiveEvent
import java.util.concurrent.TimeUnit

class MainViewModel(application: Application) : BaseSessionViewModel(application) {

    private val _onBackPressedEventLiveData = SingleLiveEvent<Any>()
    val onBackPressedEventLiveData: LiveData<Any> get() = _onBackPressedEventLiveData
    private var mBackPressedAt = 0L

    fun onBackPressed() {
        if (mBackPressedAt + TimeUnit.SECONDS.toMillis(2) > System.currentTimeMillis()) {
            _onBackPressedEventLiveData.call()
        } else {
            showSnackbar("\'뒤로\' 버튼을 한번 더 누르시면 종료됩니다.")
            mBackPressedAt = System.currentTimeMillis()
        }
    }

    private val _onSuccessGettingUserInfo = SingleLiveEvent<UserModel>()
    val onSuccessGettingUserInfo : LiveData<UserModel> get() = _onSuccessGettingUserInfo
    private val _onSuccessSaveUserInfo = SingleLiveEvent<Any>()
    val onSuccessSaveUserInfo : LiveData<Any> get() = _onSuccessSaveUserInfo
    private val _onFailureGettingNullAdminInfo  = SingleLiveEvent<UserModel>()
    val onFailureGettingNullAdminInfo : LiveData<UserModel> get() = _onFailureGettingNullAdminInfo
    private val _onSuccessDeleteUserInfo = SingleLiveEvent<Any>()
    val onSuccessDeleteUserInfo : LiveData<Any> get() = _onSuccessDeleteUserInfo
    private val _onSuccessDeleteUserInfoFromServer = SingleLiveEvent<Boolean>()
    val onSuccessDeleteUserInfoFromServer : LiveData<Boolean> get() = _onSuccessDeleteUserInfoFromServer


    fun getUserInfo()  {
        apiCall(userRepository.getUserInfo(), {
            _onSuccessGettingUserInfo.postValue(it) },
            { _onFailureGettingNullAdminInfo.call() })
    }

    fun saveUserInfo(userData : UserModel) {
        apiCall(userRepository.saveUserInfo(userData),{
            _onSuccessSaveUserInfo.value = true } )
    }

    fun deleteUserInfoFromAppDatabase() {
        val context = getApplication<Application>().applicationContext
        apiCall(userRepository.deleteUserInfo(authToken),
            { userRepository.removeUserToken(context)
                _onSuccessDeleteUserInfo.value = true },
            { showSnackbar("로그아웃에 실패했습니다. 잠시후에 시도해주세요.") })
    }

    fun deleteAdminInfoFromServerDatabase() {
        apiCall(userRepository.deleteUserInfoFromServer(authToken),
            { if (it) _onSuccessDeleteUserInfoFromServer.postValue(true)
                else _onSuccessDeleteUserInfoFromServer.postValue(false) },
            { showSnackbar("회원탈퇴에 실패했습니다. 네트워크 상태를 체크해주세요.") })
    }

}