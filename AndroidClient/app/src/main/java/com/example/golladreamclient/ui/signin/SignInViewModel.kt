package com.example.golladreamclient.ui.signin

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.golladreamclient.base.BaseSessionViewModel
import com.example.golladreamclient.data.ServerAPI
import com.example.golladreamclient.data.model.UserModel
import com.example.golladreamclient.data.model.UserStatus
import com.example.golladreamclient.utils.RegularExpressionUtils
import com.example.golladreamclient.utils.SingleLiveEvent


class SignInViewModel(application: Application) : BaseSessionViewModel(application) {

    private val _invalidUserNameEventLiveData = SingleLiveEvent<String>()
    val invalidUserNameEventLiveData: LiveData<String> get() = _invalidUserNameEventLiveData
    private val _validUserNameEventLiveData = SingleLiveEvent<Any>()
    val validUserNameEventLiveData: LiveData<Any> get() = _validUserNameEventLiveData
    private val _invalidBirthInfoEventLiveData = SingleLiveEvent<String>()
    val invalidUserBirthEventLiveData: LiveData<String> get() = _invalidBirthInfoEventLiveData
    private val _validUserBirthEventLiveData = SingleLiveEvent<Any>()
    val validUserBirthEventLiveData: LiveData<Any> get() = _validUserBirthEventLiveData
    private val _invalidUserIdEventLiveData = SingleLiveEvent<String>()
    val invalidUserIdEventLiveData: LiveData<String> get() = _invalidUserIdEventLiveData
    private val _validUserIdEventLiveData = SingleLiveEvent<Any>()
    val validUserIdEventLiveData: LiveData<Any> get() = _validUserIdEventLiveData
    private val _findInfoEventLiveData = SingleLiveEvent<Any>()
    val findInfoEventLiveData: LiveData<Any> get() = _findInfoEventLiveData
    private val _onSuccessFindInfo = SingleLiveEvent<String>()
    val onSuccessFindInfo : LiveData<String> get() = _onSuccessFindInfo
    private val _onSuccessSaveUserInfo = SingleLiveEvent<Any>()
    val onSuccessSaveUserInfo : LiveData<Any> get() = _onSuccessSaveUserInfo
    private val _userStatusEvent = SingleLiveEvent<UserStatus>()
    val userStatusEvent : LiveData<UserStatus> get() = _userStatusEvent

    var findInfoType : FindInfoType = FindInfoType.ID
    var findInfoUserName : String = ""
    var findInfoResultData : String = ""
    private var userData : UserModel?= null

    fun clearTypeData() {
        findInfoType = FindInfoType.ID
        findInfoUserName = ""
        findInfoResultData = ""
    }

    private fun checkForUserName(userName: String) : Boolean{
        return if (userName.isBlank() || userName.isEmpty()) {
            _invalidUserNameEventLiveData.postValue("이름을 입력해주세요.")
            false
        } else if (userName.length < 2) {
            _invalidUserNameEventLiveData.postValue("이름은 2글자 이상이어야 합니다.")
            false
        } else if (!RegularExpressionUtils.validCheck(RegularExpressionUtils.Regex.NAME, userName)) {
            _invalidUserNameEventLiveData.postValue("제대로 된 한글형식으로 입력해주세요.")
            false
        } else {
            _validUserNameEventLiveData.call()
            true}
    }

    private fun checkForUserBirthday(userBirthday: String) : Boolean{
        return if (userBirthday.isBlank() || userBirthday.isEmpty()) {
            _invalidBirthInfoEventLiveData.postValue("생년월일을 입력해주세요.")
            false
        } else if (!RegularExpressionUtils.validCheck(RegularExpressionUtils.Regex.BIRTH, userBirthday)){
            _invalidBirthInfoEventLiveData.postValue("생년월일은 8자리 숫자이어야 합니다.")
            false
        } else {
            _validUserBirthEventLiveData.call()
            true
        }
    }

    fun checkForUserId(userId: String) : Boolean {
        return if (userId.isBlank() || userId.isEmpty()) {
            _invalidUserIdEventLiveData.postValue("아이디를 입력해주세요.")
            false
        } else if (!RegularExpressionUtils.validCheck(RegularExpressionUtils.Regex.ID, userId)) {
            _invalidUserIdEventLiveData.postValue("아이디는 4~12자의 영문, 숫자만 가능합니다.")
            false
        } else {
            _validUserIdEventLiveData.call()
            true
        }
    }

    fun checkForFindInfo(type : FindInfoType, userName : String, userBirthday : String,
                         userName2 : String, userId : String) {
        when(type) {
            FindInfoType.ID -> {
                val checkName = checkForUserName(userName)
                val checkBirthInfo = checkForUserBirthday(userBirthday)
                if (checkName && checkBirthInfo ) _findInfoEventLiveData.call() }
            FindInfoType.PWD -> {
                val checkName = checkForUserName(userName2)
                val checkId = checkForUserId(userId)
                if (checkName && checkId) _findInfoEventLiveData.call() }
        }
    }

    fun sendFindInfo(type : FindInfoType, userName : String, userBirthday : String, userName2 : String, userId : String) {
        when(type) {
            FindInfoType.ID -> {
                apiCall(userRepository.findUserId(userName, userBirthday),{
                    if (it.exist) _onSuccessFindInfo.postValue(it.userId!!)
                    else _onSuccessFindInfo.postValue("") }) }
            FindInfoType.PWD ->  {
                apiCall(userRepository.findUserPwd(userName2, userId),{
                    if (it.exist) _onSuccessFindInfo.postValue(it.userPwd!!)
                    else _onSuccessFindInfo.postValue("")
                })
            }
        }
    }

    fun sendSignInInfo(userId : String, userPwd : String) {
        apiCall(userRepository.checkUserInfoFromServer(userId, userPwd), {
            if (it.exist){ userData = it.user!!.getUserModel()
                _userStatusEvent.postValue(UserStatus.USER)
            }else _userStatusEvent.postValue(UserStatus.NOT_USER)
        })
    }

    fun saveUserInfo() {
        val context = getApplication<Application>().applicationContext
        userData?.let {
            apiCall(userRepository.saveUserInfo(it),{
                _onSuccessSaveUserInfo.value = true
                userRepository.saveUserToken(it.id, context) } )
        }
    }




}