package com.example.golladreamclient.ui.main.write

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.golladreamclient.base.BaseSessionViewModel
import com.example.golladreamclient.data.model.InputData
import com.example.golladreamclient.data.model.OutputData
import com.example.golladreamclient.data.model.PersonalInfo
import com.example.golladreamclient.data.model.UserModel
import com.example.golladreamclient.data.repository.RecommendRepository
import com.example.golladreamclient.utils.SingleLiveEvent
import java.io.File
import java.time.LocalDateTime

class WriteViewModel(application: Application) : BaseSessionViewModel(application) {

    private val _selectedImageLiveData = MutableLiveData<File?>()
    val selectedImageLiveData: LiveData<File?> get() = _selectedImageLiveData
    private val _onSuccessSaveUserInfo = SingleLiveEvent<Any>()
    val onSuccessSaveUserInfo : LiveData<Any> get() = _onSuccessSaveUserInfo

    private var userData : UserModel ?= null
    private var colorData : String ?= null
    private var imageData : File? = null

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
    fun saveThirdWriteInfo(image: File){
        imageData = image
    }
    fun receiveThirdWriteInfo(imageFile: File?) = _selectedImageLiveData.postValue(imageFile)

    fun getWriteInfo() : InputData? {
        return if (userData != null && colorData !=null && imageData !=null){
            val personInfo = userData!!.getPersonalInfo()
            InputData(userData!!.id, LocalDateTime.now().toString(), personInfo, colorData!!, imageData.toString()) //TODO : 중요
        }else null
    }

    //----------------------------------------------------------------------------------------------------

    private val reservationRepository: RecommendRepository = RecommendRepository.getInstance()

    private val _onSuccessGetRecommend = SingleLiveEvent<OutputData>()
    val onSuccessGetRecommend : LiveData<OutputData> get() = _onSuccessGetRecommend

    fun postRecommendInput(data : InputData) {
        apiCall(reservationRepository.postRecommendInput(data), {
            _onSuccessGetRecommend.postValue(it)
        })
    }
}