package com.example.golladreamclient.ui.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.example.golladreamclient.R
import com.example.golladreamclient.base.BaseSessionFragment
import com.example.golladreamclient.databinding.FragmentSignupFirstBinding
import com.example.golladreamclient.utils.WrapedDialogBasicOneButton
import com.example.golladreamclient.utils.hideKeyboard


class SignUpFirstFragment : BaseSessionFragment<FragmentSignupFirstBinding, SignUpViewModel>() {
    override lateinit var viewbinding: FragmentSignupFirstBinding
    override val viewmodel: SignUpViewModel by navGraphViewModels(R.id.signUpGraph)

    override fun initViewbinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewbinding = FragmentSignupFirstBinding.inflate(inflater, container, false)
        return viewbinding.root
    }

    override fun initViewStart(savedInstanceState: Bundle?) {
        viewbinding.fragmentRootLayout.setOnClickListener { hideKeyboard(it) }
        setSpinnerView()
    }

    override fun initDataBinding(savedInstanceState: Bundle?) {
        viewmodel.run {
            invalidUserNameEventLiveData.observe(viewLifecycleOwner) { setNameErrorMessage(it) }
            validUserNameEventLiveData.observe(viewLifecycleOwner){ setNameEmptyMessage() }
            invalidUserBirthEventLiveData.observe(viewLifecycleOwner) { setBirthInfoErrorMessage(it) }
            validUserBirthEventLiveData.observe(viewLifecycleOwner) { setBirthInfoEmptyMessage() }
            invalidUserSexEventLiveData.observe(viewLifecycleOwner) { setSexInfoErrorMessage(it) }
            validUserSexEventLiveData.observe(viewLifecycleOwner) { setSexInfoEmptyMessage() }
            invalidUserHeightEventLiveData.observe(viewLifecycleOwner) { setHeightInfoErrorMessage(it) }
            validUserHeightEventLiveData.observe(viewLifecycleOwner) { setHeightInfoEmptyMessage() }
            invalidUserWeightEventLiveData.observe(viewLifecycleOwner) { setWeightInfoErrorMessage(it) }
            validUserWeightEventLiveData.observe(viewLifecycleOwner) { setWeightInfoEmptyMessage() }
            saveSignUpInfoEventLiveData.observe(viewLifecycleOwner){
                if (!checkUserBirthdayAble(getUserBirthday()))
                    makeDialog("앗 20대가 아니시네요!\n저희 골라드림 서비스는 현재\n20대 고객층 대상의 서비스만을 제공하고 있습니다.\n\n"
                    + "빠른 시일내로 고객님을 위한 서비스도 마련할게요.")
                else {
                    viewmodel.saveSignUpInfo(getUserName(), getUserBirthday(), getUserSex()!!, getUserHeight()!!, getUserWeight()!!)
                    findNavController().navigate(R.id.action_signUpFirstFragment_to_signUpSecondFragment)
                }
            }
        }
    }

    override fun initViewFinal(savedInstanceState: Bundle?) {
        viewbinding.run {
            editTextName.setOnFocusChangeListener { v, hasFocus ->
                if (hasFocus && (getUserName().isEmpty()|| getUserName().isBlank())) {
                    setNameErrorMessage("이름을 입력해주세요.")
                    setBirthInfoEmptyMessage()
                    setSexInfoEmptyMessage()
                }
            }
            editTextBirthInfo.setOnFocusChangeListener { v, hasFocus ->
                if (hasFocus && (getUserBirthday().isEmpty()|| getUserBirthday().isBlank())) {
                    setBirthInfoErrorMessage("8자리 숫자를 입력해주세요.(ex.20000101)")
                    setNameEmptyMessage()
                    setSexInfoEmptyMessage()
                }
            }
            signupNextbtn.setOnClickListener { viewmodel.checkForSaveSignUpInfo(getUserName(), getUserBirthday(), getUserSex(), getUserHeight(), getUserWeight()) }
        }
    }

    private fun makeDialog(msg : String){
        val dialog = WrapedDialogBasicOneButton(requireContext(), msg).apply {
            setCanceledOnTouchOutside(false)
            clickListener = object : WrapedDialogBasicOneButton.DialogButtonClickListener{
                override fun dialogClickListener() {
                    findNavController().navigate(R.id.action_signUpFirstFragment_pop_including_signUpGraph)
                    dismiss() }
            }
        }
        showDialog(dialog, viewLifecycleOwner)
    }

    private fun checkUserBirthdayAble(userBirth : String) : Boolean {
        return if (userBirth.subSequence(0 until 3) == "199") true
        else userBirth.subSequence(0 until 3) == "200" && userBirth[3].toString().toInt() < 3
    }

    private fun getUserName() = viewbinding.editTextName.text.toString().trim()
    private fun getUserBirthday() = viewbinding.editTextBirthInfo.text.toString().trim()
    private fun getUserSex() : String? {
        return if (viewbinding.spinnerSex.selectedItemPosition == 0) null
        else viewbinding.spinnerSex.selectedItem.toString()
    }
    private fun getUserHeight() : String? {
        return when (viewbinding.spinnerHeight.selectedItemPosition){
            0 -> null
            1 -> "150미만대"
            2 -> "150대"
            3 -> "160대"
            4 -> "170대"
            5 -> "180대"
            6 -> "190이상대"
            else -> null
        }
    }
    private fun getUserWeight() : String? {
        return when (viewbinding.spinnerWeight.selectedItemPosition){
            0 -> null
            1 -> "40미만대"
            2 -> "40대"
            3 -> "50대"
            4 -> "60대"
            5 -> "7대"
            6 -> "80대"
            7 -> "90대"
            8 -> "100이상대"
            else -> null
        }
    }

    private fun setSpinnerView(){
        viewbinding.spinnerSex.apply {
            ArrayAdapter.createFromResource(requireContext(), R.array.signUp_sex, R.layout.spinner_selected)
                .also { adapter ->
                adapter.setDropDownViewResource(R.layout.spinner_list_item)
                this.adapter = adapter }
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    setNameEmptyMessage()
                    setBirthInfoEmptyMessage()
                    setSexInfoEmptyMessage()
                    setHeightInfoEmptyMessage()
                    setWeightInfoEmptyMessage()
                }
                override fun onNothingSelected(parent: AdapterView<*>?) { }
            }
        }
        viewbinding.spinnerHeight.apply {
            ArrayAdapter.createFromResource(requireContext(), R.array.signUp_height, R.layout.spinner_selected)
                .also { adapter ->
                    adapter.setDropDownViewResource(R.layout.spinner_list_item)
                    this.adapter = adapter }
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    setNameEmptyMessage()
                    setBirthInfoEmptyMessage()
                    setSexInfoEmptyMessage()
                    setHeightInfoEmptyMessage()
                    setWeightInfoEmptyMessage()
                }
                override fun onNothingSelected(parent: AdapterView<*>?) { }
            }
        }
        viewbinding.spinnerWeight.apply {
            ArrayAdapter.createFromResource(requireContext(), R.array.signUp_weight, R.layout.spinner_selected)
                .also { adapter ->
                    adapter.setDropDownViewResource(R.layout.spinner_list_item)
                    this.adapter = adapter }
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    setNameEmptyMessage()
                    setBirthInfoEmptyMessage()
                    setSexInfoEmptyMessage()
                    setHeightInfoEmptyMessage()
                    setWeightInfoEmptyMessage()
                }
                override fun onNothingSelected(parent: AdapterView<*>?) { }
            }
        }
    }

    private fun setNameErrorMessage(message: String) {
        viewbinding.editTextName.setBackgroundResource(R.drawable.edittext_underline_pinkishorange)
        viewbinding.textNameWarning.apply {
            text = message
            visibility = View.VISIBLE
        }
    }
    private fun setBirthInfoErrorMessage(message: String) {
        viewbinding.editTextBirthInfo.setBackgroundResource(R.drawable.edittext_underline_pinkishorange)
        viewbinding.textBirthInfoWarning.apply {
            text = message
            visibility = View.VISIBLE
        }
    }
    private fun setSexInfoErrorMessage(message: String) {
        viewbinding.spinnerSex.setBackgroundResource(R.drawable.edittext_underline_pinkishorange)
        viewbinding.textSexWarning.apply {
            text = message
            visibility = View.VISIBLE
        }
    }
    private fun setHeightInfoErrorMessage(message: String) {
        viewbinding.spinnerHeight.setBackgroundResource(R.drawable.edittext_underline_pinkishorange)
        viewbinding.textHeightWarning.apply {
            text = message
            visibility = View.VISIBLE
        }
    }
    private fun setWeightInfoErrorMessage(message: String) {
        viewbinding.spinnerWeight.setBackgroundResource(R.drawable.edittext_underline_pinkishorange)
        viewbinding.textWeightWarning.apply {
            text = message
            visibility = View.VISIBLE
        }
    }
    private fun setNameEmptyMessage() {
        viewbinding.editTextName.setBackgroundResource(R.drawable.edittext_underline_black20)
        viewbinding.textNameWarning.text = "" }
    private fun setBirthInfoEmptyMessage() {
        viewbinding.editTextBirthInfo.setBackgroundResource(R.drawable.edittext_underline_black20)
        viewbinding.textBirthInfoWarning.text = "" }

    private fun setSexInfoEmptyMessage() {
        viewbinding.spinnerSex.setBackgroundResource(R.drawable.edittext_underline_black20)
        viewbinding.textSexWarning.text = "" }

    private fun setHeightInfoEmptyMessage() {
        viewbinding.spinnerHeight.setBackgroundResource(R.drawable.edittext_underline_black20)
        viewbinding.textHeightWarning.text = "" }

    private fun setWeightInfoEmptyMessage() {
        viewbinding.spinnerWeight.setBackgroundResource(R.drawable.edittext_underline_black20)
        viewbinding.textWeightWarning.text = "" }



}