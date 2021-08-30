package com.example.golladreamclient.ui.signup

import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.example.golladreamclient.R
import com.example.golladreamclient.base.BaseSessionFragment
import com.example.golladreamclient.databinding.FragmentSignupSecondBinding
import com.example.golladreamclient.restartActivity
import com.example.golladreamclient.utils.WrapedDialogBasicOneButton
import com.example.golladreamclient.utils.hideKeyboard


class SignUpSecondFragment : BaseSessionFragment<FragmentSignupSecondBinding, SignUpViewModel>() {

    override lateinit var viewbinding: FragmentSignupSecondBinding
    override val viewmodel: SignUpViewModel by navGraphViewModels(R.id.signUpGraph)
    private lateinit var connectionManager : ConnectivityManager

    override fun initViewbinding(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewbinding = FragmentSignupSecondBinding.inflate(inflater, container, false)
        return viewbinding.root
    }

    override fun initViewStart(savedInstanceState: Bundle?) {
        when (context){
            null -> {
                showToast("에러가 발생했습니다.\n앱을 재부팅합니다.")
                restartActivity()
            }else ->{ connectionManager = requireContext().getSystemService()!! }
        }
        viewbinding.fragmentContent.setOnClickListener { hideKeyboard(it) }
        requireActivity().onBackPressedDispatcher.addCallback(this){
            viewmodel.clearSecondFragmentVar()
            findNavController().navigate(R.id.action_signUpSecondFragment_pop)
        }
    }

    override fun initDataBinding(savedInstanceState: Bundle?) {
        viewmodel.run {
            invalidUserIdEventLiveData.observe(viewLifecycleOwner) { setIdErrorMessage(it) }
            validUserIdEventLiveData.observe(viewLifecycleOwner){ setIdEmptyMessage() }
            invalidUserPwdEventLiveData.observe(viewLifecycleOwner) { setPwdErrorMessage(it) }
            validUserPwdEventLiveData.observe(viewLifecycleOwner){ setPwdEmptyMessage() }
            invalidUserPwd2EventLiveData.observe(viewLifecycleOwner) { setPwd2ErrorMessage(it) }
            validUserPwd2EventLiveData.observe(viewLifecycleOwner){ setPwd2EmptyMessage() }
            checkIdEventLiveData.observe(viewLifecycleOwner){ result ->
                when(result){
                    true -> setIdChecked()
                    false -> setIdNotChecked() }
            }
            sendSignUpInfoEventLiveData.observe(viewLifecycleOwner){
                viewmodel.sendSignUpInfoToServer(getUserId(), getUserPwd())
            }
            onSuccessSignUpEvent.observe(viewLifecycleOwner){ showSignUpSucceedDialog() }
        }
    }

    override fun initViewFinal(savedInstanceState: Bundle?) {

        viewbinding.run {
            editTextId.setOnFocusChangeListener { v, hasFocus ->
                if (hasFocus && (getUserId().isEmpty()|| getUserId().isBlank())) {
                    setIdErrorMessage("4~13자리로 입력해주세요.")
                    setPwdEmptyMessage()
                    setPwd2EmptyMessage()
                }
            }
            editTextPwd.setOnFocusChangeListener { v, hasFocus ->
                if (hasFocus && (getUserPwd().isEmpty()||getUserPwd().isBlank())) {
                    setPwdErrorMessage("8~22자리의 영문/숫자로 입력해주세요.")
                    setIdEmptyMessage()
                    setPwd2EmptyMessage()
                }
            }
            editTextPwd2.setOnFocusChangeListener { v, hasFocus ->
                if (hasFocus && (getUserPwd().isEmpty()|| getUserPwd().isBlank())) {
                    setPwd2ErrorMessage("비밀번호를 먼저 입력해주세요.")
                    setIdEmptyMessage()
                    setPwdEmptyMessage()
                }else if (hasFocus && getUserPwd().isNotEmpty() && getUserPwd().isNotBlank()) setPwdEmptyMessage()
            }
            editTextId.addTextChangedListener {
                viewmodel.clearCheckedId()
                checkIdBtn.isSelected = false
                if (it.toString().isEmpty() || it.toString().isBlank()) setIdErrorMessage("4~13자리로 입력해주세요.")
                else setIdEmptyMessage()
            }
            showingPwdBtn.setOnClickListener {
                showingPwdBtn.isSelected = !showingPwdBtn.isSelected
                if (showingPwdBtn.isSelected) {
                    editTextPwd.inputType = 0x00000001
                    editTextPwd.typeface = resources.getFont(R.font.notosan_font_family) }
                else editTextPwd.inputType = 0x00000081
            }
            showingPwd2Btn.setOnClickListener {
                showingPwd2Btn.isSelected = !showingPwd2Btn.isSelected
                if (showingPwd2Btn.isSelected) {
                    editTextPwd2.inputType = 0x00000001
                    editTextPwd2.typeface = resources.getFont(R.font.notosan_font_family) }
                else editTextPwd2.inputType = 0x00000081
            }
            checkIdBtn.setOnClickListener {
                if (checkServiceState()){ if(viewmodel.checkForUserId(getUserId())) viewmodel.checkIdFromServer(getUserId())  }
                else{ showToast("인터넷 연결이 불안정합니다.\nWifi 상태를 체킹해주세요.") }
            }
            signupBtn.setOnClickListener {
                if (checkServiceState()){ viewmodel.checkForSendSignUpInfo(getUserId(), getUserPwd(), getUserPwd2()) }
                else{ showToast("인터넷 연결이 불안정합니다.\nWifi 상태를 체킹해주세요.") }
            }
        }
    }

    private fun showSignUpSucceedDialog(){
        val dialog = WrapedDialogBasicOneButton(requireContext(), "회원가입이 성공적으로 되었습니다!").apply {
            setCanceledOnTouchOutside(false)
            clickListener = object : WrapedDialogBasicOneButton.DialogButtonClickListener {
                override fun dialogClickListener() {
                    findNavController().navigate(R.id.action_signUpSecondFragment_pop)
                    dismiss() }
            }
        }
        showDialog(dialog, viewLifecycleOwner)
    }

    private fun checkServiceState() : Boolean { return connectionManager.activeNetwork != null }
    private fun getUserId() = viewbinding.editTextId.text.toString().trim()
    private fun getUserPwd() = viewbinding.editTextPwd.text.toString().trim()
    private fun getUserPwd2() = viewbinding.editTextPwd2.text.toString().trim()


    private fun setIdErrorMessage(message: String) {
        viewbinding.editTextId.setBackgroundResource(R.drawable.edittext_underline_pinkishorange)
        viewbinding.textIdWarning.apply {
            text = message
            visibility = View.VISIBLE
            setTextColor(ContextCompat.getColor(requireContext(), R.color.pinkish_orange))
        }
    }
    private fun setIdEmptyMessage() {
        viewbinding.editTextId.setBackgroundResource(R.drawable.edittext_underline_black20)
        viewbinding.textIdWarning.text = "" }

    private fun setPwdErrorMessage(message: String) {
        viewbinding.editTextPwd.setBackgroundResource(R.drawable.edittext_underline_pinkishorange)
        viewbinding.textPwdWarning.apply {
            text = message
            visibility = View.VISIBLE
        }
    }
    private fun setPwdEmptyMessage() {
        viewbinding.editTextPwd.setBackgroundResource(R.drawable.edittext_underline_black20)
        viewbinding.textPwdWarning.text = "" }

    private fun setPwd2ErrorMessage(message: String) {
        viewbinding.editTextPwd2.setBackgroundResource(R.drawable.edittext_underline_pinkishorange)
        viewbinding.textPwd2Warning.apply {
            text = message
            visibility = View.VISIBLE
            setTextColor(ContextCompat.getColor(requireContext(), R.color.pinkish_orange))
        }
    }
    private fun setPwd2EmptyMessage() {
        viewbinding.editTextPwd2.setBackgroundResource(R.drawable.edittext_underline_black20)
        viewbinding.textPwd2Warning.text = "" }

    private fun setIdChecked() {
        viewbinding.apply {
            textIdWarning.apply {
                text = "사용할 수 있는 아이디입니다."
                setTextColor(ContextCompat.getColor(requireContext(), R.color.applemint))
                visibility = View.VISIBLE
            }
            checkIdBtn.isSelected = true
        }
    }
    private fun setIdNotChecked() {
        viewbinding.apply {
            textIdWarning.apply {
                text = "이미 존재하는 아이디입니다."
                setTextColor(ContextCompat.getColor(requireContext(), R.color.pinkish_orange))
            }
        }
    }

}