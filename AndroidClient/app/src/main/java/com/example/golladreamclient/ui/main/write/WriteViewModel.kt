package com.example.golladreamclient.ui.main.write

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.golladreamclient.base.BaseSessionViewModel
import com.example.golladreamclient.data.model.UserModel
import com.example.golladreamclient.utils.SingleLiveEvent
import java.io.File

class WriteViewModel(application: Application) : BaseSessionViewModel(application) {

    private val _selectedImageLiveData = MutableLiveData<File?>()
    val selectedImageLiveData: LiveData<File?> get() = _selectedImageLiveData
    private val _onSuccessSaveUserInfo = SingleLiveEvent<Any>()
    val onSuccessSaveUserInfo : LiveData<Any> get() = _onSuccessSaveUserInfo

    private var userData : UserModel ?= null
    private var colorData : String ?= null

    fun saveUserInfo(userData : UserModel) {
        apiCall(userRepository.saveUserInfo(userData),{
            _onSuccessSaveUserInfo.value = true } )
    }
    fun saveFirstWriteInfo(user : UserModel){
        userData = user
    }
    fun saveSecondWriteInfo(color : String){
        colorData = color
    }
    fun saveThirdWriteInfo(imageFile: File?) = _selectedImageLiveData.postValue(imageFile)
}