package com.example.golladreamclient.ui.main.write

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.golladreamclient.base.BaseSessionViewModel
import com.example.golladreamclient.data.model.*
import com.example.golladreamclient.data.repository.RecommendRepository
import com.example.golladreamclient.ui.main.image.getURLEncodedFileName
import com.example.golladreamclient.utils.SingleLiveEvent
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.time.LocalDateTime

class WriteViewModel(application: Application) : BaseSessionViewModel(application) {

    private val _selectedImageLiveData = MutableLiveData<File?>()
    val selectedImageLiveData: LiveData<File?> get() = _selectedImageLiveData
    private val _onSuccessSaveUserInfo = SingleLiveEvent<Any>()
    val onSuccessSaveUserInfo : LiveData<Any> get() = _onSuccessSaveUserInfo
    private val _onSuccessSaveResultInfo = SingleLiveEvent<Any>()
    val onSuccessSaveResultInfo : LiveData<Any> get() = _onSuccessSaveResultInfo

    private var userData : UserModel ?= null
    private var styleData : String ?= null
    private var imageData : File? = null
    private var resultImageData1 : String? = null
    private var resultImageData2 : String? = null

    fun saveUserInfo(userData : UserModel) {
        apiCall(userRepository.saveUserInfo(userData),{
            _onSuccessSaveUserInfo.value = true } )
    }
    fun saveFirstWriteInfo(user : UserModel){
        userData = user
    }
    fun saveSecondWriteInfo(color : String){
        styleData = color
    }
    fun saveThirdWriteInfo(image: File){
        imageData = image
    }
    fun saveResultInfo(image1: String?, image2 : String?){
        resultImageData1 = image1
        resultImageData2 = image2
        _onSuccessSaveResultInfo.call()
    }
    fun receiveThirdWriteInfo(imageFile: File?) = _selectedImageLiveData.postValue(imageFile)

    fun getWriteInfo() : InputData? {
        return if (userData != null && styleData !=null && imageData !=null){
            val personInfo = userData!!.getPersonalInfo()
            InputData(userData!!.id, LocalDateTime.now().toString(), personInfo, styleData!!, imageData!!)
        }else null
    }
    fun getResultInfo() : List<String>{
        return if (resultImageData1 == null && resultImageData2 == null) emptyList()
        else if (resultImageData1 != null && resultImageData2 == null) listOf(resultImageData1!!)
        else listOf(resultImageData1!!, resultImageData2!!)
    }

    //----------------------------------------------------------------------------------------------------

    private val reservationRepository: RecommendRepository = RecommendRepository.getInstance()

    private val _onSuccessSaveImage = SingleLiveEvent<ReceiverSaveInput>()
    val onSuccessSaveImage : LiveData<ReceiverSaveInput> get() = _onSuccessSaveImage
    private val _onSuccessGetRecommendResult = SingleLiveEvent<ReceiverRecommendOutput>()
    val onSuccessGetRecommendResult : LiveData<ReceiverRecommendOutput> get() = _onSuccessGetRecommendResult

    fun postRecommendInput(data : InputData) {
        apiCall(reservationRepository.postRecommendInput(convertInputDataToPartMap(data)), {
            _onSuccessSaveImage.postValue(it)
        })
    }

    fun getRecommendOutput(data : InputItem){
        apiCall(reservationRepository.getRecommendOutput(data.id, data.styleInfo, data.imageName), {
            _onSuccessGetRecommendResult.postValue(it) }, indicator = true)
    }

    private fun convertInputDataToPartMap(data : InputData): HashMap<String, RequestBody> {
        val map = HashMap<String, RequestBody>()
        map["id"] = data.id.toRequestBody("text/plain".toMediaTypeOrNull())
        map["style"] = data.styleInfo.toRequestBody("text/plain".toMediaTypeOrNull())
        val fileBody = data.image.asRequestBody("image/jpeg".toMediaTypeOrNull())
        map["image\"; filename=\"" + data.image.name.getURLEncodedFileName()] = fileBody
        return map
    }
}