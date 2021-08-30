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
        viewbinding.fragmentContent.setOnClickListener { hideKeyboard(it) }
    }

    override fun initDataBinding(savedInstanceState: Bundle?) {
        viewmodel.run {
            invalidUserNameEventLiveData.observe(viewLifecycleOwner) { setNameErrorMessage(it) }
            validUserNameEventLiveData.observe(viewLifecycleOwner){ setNameEmptyMessage() }
            invalidUserBirthEventLiveData.observe(viewLifecycleOwner) { setBirthInfoErrorMessage(it) }
            validUserBirthEventLiveData.observe(viewLifecycleOwner) { setBirthInfoEmptyMessage() }
            invalidUserSexEventLiveData.observe(viewLifecycleOwner) { setSmsInfoErrorMessage(it) }
            validUserSexEventLiveData.observe(viewLifecycleOwner) { setSmsInfoEmptyMessage() }
            saveSignUpInfoEventLiveData.observe(viewLifecycleOwner){
                viewmodel.saveSignUpInfo(getUserName(), getUserBirthday(), getUserSex())
                findNavController().navigate(R.id.action_signUpFirstFragment_to_signUpSecondFragment)
            }
        }
    }

    override fun initViewFinal(savedInstanceState: Bundle?) {
        viewbinding.run {
            editTextName.setOnFocusChangeListener { v, hasFocus ->
                if (hasFocus && (getUserName().isEmpty()|| getUserName().isBlank())) {
                    setNameErrorMessage("이름을 입력해주세요.")
                    setBirthInfoEmptyMessage()
                    setSmsInfoEmptyMessage()
                }
            }
            editTextBirthInfo.setOnFocusChangeListener { v, hasFocus ->
                if (hasFocus && (getUserBirthday().isEmpty()|| getUserBirthday().isBlank())) {
                    setBirthInfoErrorMessage("8자리 숫자를 입력해주세요.(ex.20000101)")
                    setNameEmptyMessage()
                    setSmsInfoEmptyMessage()
                }
            }
            //TODO
            /*editTextSexInfo.setOnFocusChangeListener { v, hasFocus ->
                if (hasFocus && (getUserSmsInfo().isEmpty()|| getUserSmsInfo().isBlank())) {
                    setSmsInfoErrorMessage("'-'없이 휴대폰번호 11자리를 입력해주세요.")
                    setNameEmptyMessage()
                    setBirthInfoEmptyMessage()
                }
            }*/
            signupNextbtn.setOnClickListener { viewmodel.checkForSaveSignUpInfo(getUserName(), getUserBirthday(), getUserSex()) }
        }
    }

    private fun getUserName() = viewbinding.editTextName.text.toString().trim()
    private fun getUserBirthday() = viewbinding.editTextBirthInfo.text.toString().trim()
    private fun getUserSex() = "" //viewbinding.editTextSex.text.toString().trim()

/*    private fun setSpinnerUsingTimeView(){
        viewbinding.spinnerReserveTime.apply {
            ArrayAdapter.createFromResource(requireContext(), R.array.reserve_time, R.layout.spinner_reserve_selected
            ).also { adapter ->
                adapter.setDropDownViewResource(R.layout.spinner_reserve_list_item)
                this.adapter = adapter
            }
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    setSpinnerMaxTimeView(position)
                    viewmodel.checkSelectedUsingTimeSetting(getSpinnerUsingTime())
                }
                override fun onNothingSelected(parent: AdapterView<*>?) { }
            }
        }
    }*/

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
    private fun setSmsInfoErrorMessage(message: String) {
        //TODO
        //viewbinding.editTextSex.setBackgroundResource(R.drawable.edittext_underline_pinkishorange)
        viewbinding.textSmsInfoWarning.apply {
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

    private fun setSmsInfoEmptyMessage() {
        //TODO
        //viewbinding.editTextSex.setBackgroundResource(R.drawable.edittext_underline_black20)
        viewbinding.textSmsInfoWarning.text = "" }



}