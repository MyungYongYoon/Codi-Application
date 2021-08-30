package com.example.golladreamclient.ui.signup

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.golladreamclient.base.BaseSessionViewModel
import com.example.golladreamclient.data.model.SignUpInfo
import com.example.golladreamclient.utils.RegularExpressionUtils
import com.example.golladreamclient.utils.SingleLiveEvent
import io.reactivex.Single

class SignUpViewModel(application: Application) : BaseSessionViewModel(application) {

    private val _onClickedAllBtn = SingleLiveEvent<Boolean>()
    val onClickedAllBtn : LiveData<Boolean> get() = _onClickedAllBtn
    private val _onClickedFirstBtn = SingleLiveEvent<Boolean>()
    val onClickedFirstBtn : LiveData<Boolean> get() = _onClickedFirstBtn
    private val _onClickedSecondBtn = SingleLiveEvent<Boolean>()
    val onClickedSecondBtn : LiveData<Boolean> get() = _onClickedSecondBtn
    private val _checkPermissionClickedState = SingleLiveEvent<Any>()
    val checkPermissionClickedState : LiveData<Any> get() = _checkPermissionClickedState

    var clickedAllBtn : Boolean = false
    var clickedFirstBtn : Boolean = false
    var clickedSecondBtn : Boolean = false

    fun changeAllBtnValue() {
        clickedAllBtn = !clickedAllBtn
        clickedFirstBtn = clickedAllBtn
        clickedSecondBtn = clickedAllBtn
        _onClickedAllBtn.value = clickedAllBtn
        _onClickedFirstBtn.value = clickedAllBtn
        _onClickedSecondBtn.value = clickedAllBtn
        observePermissionBtnState()
    }

    fun changeFirstBtnValue() {
        clickedFirstBtn = !clickedFirstBtn
        _onClickedFirstBtn.value = clickedFirstBtn
        observePermissionBtnState()
    }
    fun changeFirstBtnValueTrue() {
        clickedFirstBtn = true
        _onClickedFirstBtn.postValue(clickedFirstBtn)
        observePermissionBtnState()
    }
    fun changeSecondBtnValue() {
        clickedSecondBtn = !clickedSecondBtn
        _onClickedSecondBtn.value = clickedSecondBtn
        observePermissionBtnState()
    }
    fun changeSecondBtnValueTrue() {
        clickedSecondBtn = true
        _onClickedSecondBtn.postValue(clickedSecondBtn)
        observePermissionBtnState()
    }
    fun observePermissionBtnState() {
        _checkPermissionClickedState.call()
    }
    fun checkBtnState() : Boolean {
        return if (clickedFirstBtn && clickedSecondBtn) {
            clickedAllBtn = true
            _onClickedAllBtn.value = clickedAllBtn
            true
        } else {
            clickedAllBtn = false
            _onClickedAllBtn.value = clickedAllBtn
            false
        }
    }

    //-----------------------------------------------------------------------------------------------------------------

    private val _invalidUserNameEventLiveData = SingleLiveEvent<String>()
    val invalidUserNameEventLiveData: LiveData<String> get() = _invalidUserNameEventLiveData
    private val _validUserNameEventLiveData = SingleLiveEvent<Any>()
    val validUserNameEventLiveData: LiveData<Any> get() = _validUserNameEventLiveData
    private val _invalidBirthInfoEventLiveData = SingleLiveEvent<String>()
    val invalidUserBirthEventLiveData: LiveData<String> get() = _invalidBirthInfoEventLiveData
    private val _validUserBirthEventLiveData = SingleLiveEvent<Any>()
    val validUserBirthEventLiveData: LiveData<Any> get() = _validUserBirthEventLiveData
    private val _invalidUserSexEventLiveData = SingleLiveEvent<String>()
    val invalidUserSexEventLiveData: LiveData<String> get() = _invalidUserSexEventLiveData
    private val _validUserSexEventLiveData = SingleLiveEvent<Any>()
    val validUserSexEventLiveData: LiveData<Any> get() = _validUserSexEventLiveData
    private val _saveSignUpInfoEventLiveData = SingleLiveEvent<Any>()
    val saveSignUpInfoEventLiveData: LiveData<Any> get() = _saveSignUpInfoEventLiveData

    private var personalSignUpInfo : PersonalSignUpInfo ?= null
    data class PersonalSignUpInfo(val userName : String, val userBirthday: String, val userSex: String)

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

    fun checkForSaveSignUpInfo(userName : String, userBirthday : String, usersSmsInfo : String) {
        val checkName = checkForUserName(userName)
        val checkBirthInfo = checkForUserBirthday(userBirthday)
        if (checkName && checkBirthInfo ) _saveSignUpInfoEventLiveData.call()
    }


    fun saveSignUpInfo(userName : String, userBirthday : String, userSex : String){
        personalSignUpInfo = PersonalSignUpInfo(userName, userBirthday, userSex)
    }


    //-----------------------------------------------------------------------------------------------------------------

    private val _invalidUserIdEventLiveData = SingleLiveEvent<String>()
    val invalidUserIdEventLiveData: LiveData<String> get() = _invalidUserIdEventLiveData
    private val _validUserIdEventLiveData = SingleLiveEvent<Any>()
    val validUserIdEventLiveData: LiveData<Any> get() = _validUserIdEventLiveData
    private val _invalidUserPwdEventLiveData = SingleLiveEvent<String>()
    val invalidUserPwdEventLiveData: LiveData<String> get() = _invalidUserPwdEventLiveData
    private val _validUserPwdEventLiveData = SingleLiveEvent<Any>()
    val validUserPwdEventLiveData: LiveData<Any> get() = _validUserPwdEventLiveData
    private val _invalidUserPwd2EventLiveData = SingleLiveEvent<String>()
    val invalidUserPwd2EventLiveData: LiveData<String> get() = _invalidUserPwd2EventLiveData
    private val _validUserPwd2EventLiveData = SingleLiveEvent<Any>()
    val validUserPwd2EventLiveData: LiveData<Any> get() = _validUserPwd2EventLiveData
    private val _checkIdEventLiveData = SingleLiveEvent<Boolean>()
    val checkIdEventLiveData: LiveData<Boolean> get() = _checkIdEventLiveData
    private val _sendSignUpInfoEventLiveData = SingleLiveEvent<Any>()
    val sendSignUpInfoEventLiveData: LiveData<Any> get() = _sendSignUpInfoEventLiveData
    private val _onSuccessSignUpEvent = SingleLiveEvent<Any>()
    val onSuccessSignUpEvent : LiveData<Any> get() = _onSuccessSignUpEvent

    var checkedId : String ?= null
    private var signUpInfo : SignUpInfo? =null

    fun clearSecondFragmentVar(){
        signUpInfo = null
        clearCheckedId() }

    fun clearCheckedId() {checkedId = null}

    fun checkIdFromServer(userId: String) {
        //TODO
        /*apiCall(
            Single.zip(userRepository.checkIdFromWaitingUser(userId), userRepository.checkIdFromAllowedUser(userId),
            BiFunction<Boolean, Boolean, Boolean> { waitingIdExist, allowedIdExist ->
                if (waitingIdExist) return@BiFunction false
                else if (allowedIdExist) return@BiFunction false
                return@BiFunction true
            }),
            {   if (it) checkedId = userId
                _checkIdEventLiveData.postValue(it)
            })*/
    }

    private fun checkForUserIdDuplicated(userId: String): Boolean {
        return if (userId!=checkedId) {
            _invalidUserIdEventLiveData.postValue("아이디 중복확인을 해주세요.")
            false
        }else true
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

    private fun checkForUserPwd(userPwd: String, userPwd2: String) : Boolean {
        return if (userPwd.isBlank() || userPwd.isEmpty() ) {
            _invalidUserPwdEventLiveData.postValue("비밀번호를 입력해주세요.")
            _validUserPwd2EventLiveData.call()
            false
        }else if (userPwd.length < 8 || userPwd.length > 22) {
            _invalidUserPwdEventLiveData.postValue("비밀번호는 8~22자로 입력해야 합니다.")
            _validUserPwd2EventLiveData.call()
            false
        }else if (!RegularExpressionUtils.validCheck(RegularExpressionUtils.Regex.PWD, userPwd)) {
            _invalidUserPwdEventLiveData.postValue("비밀번호는 영문 또는 숫자만 가능합니다.")
            _validUserPwd2EventLiveData.call()
            false
        }else if (userPwd2.isBlank() || userPwd2.isEmpty()){
            _invalidUserPwd2EventLiveData.postValue("비밀번호 확인을 입력해주세요.")
            _validUserPwdEventLiveData.call()
            false
        }else if (userPwd != userPwd2){
            _invalidUserPwd2EventLiveData.postValue("비밀번호 확인이 일치하지 않습니다.")
            _validUserPwdEventLiveData.call()
            false
        } else {
            _validUserPwdEventLiveData.call()
            _validUserPwd2EventLiveData.call()
            true
        }
    }

    fun checkForSendSignUpInfo(userId : String, userPwd : String, userPwd2 : String) {
        val checkId = checkForUserIdDuplicated(userId)
        val checkPwd = checkForUserPwd(userPwd, userPwd2)
        if (checkId && checkPwd ) _sendSignUpInfoEventLiveData.call()
    }


    fun sendSignUpInfoToServer(userId : String, userPwd : String){
        signUpInfo = SignUpInfo(personalSignUpInfo!!.userName, personalSignUpInfo!!.userBirthday, personalSignUpInfo!!.userSex, userId, userPwd)
        //TODO
        //apiCall(userRepository.signUp(signUpInfo!!), { _onSuccessSignUpEvent.call() })
    }
}