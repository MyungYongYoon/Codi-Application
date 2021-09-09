package com.example.golladreamclient.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.golladreamclient.MainActivity
import com.example.golladreamclient.R
import com.example.golladreamclient.base.BaseSessionFragment
import com.example.golladreamclient.data.model.UserModel
import com.example.golladreamclient.databinding.FragmentMainBinding
import com.example.golladreamclient.restartActivity
import com.example.golladreamclient.ui.makeBirthText
import com.example.golladreamclient.ui.makeHeightText
import com.example.golladreamclient.ui.makeWeightText
import com.example.golladreamclient.utils.ChangeInfoBottomSheetDialog
import com.example.golladreamclient.utils.WithdrawalDialog
import com.example.golladreamclient.utils.WrapedDialogBasicOneButton
import com.example.golladreamclient.utils.WrapedDialogBasicTwoButton
import java.io.IOException

class MainFragment  : BaseSessionFragment<FragmentMainBinding, MainViewModel>(){
    override lateinit var viewbinding: FragmentMainBinding
    override val viewmodel: MainViewModel by viewModels()
    private var userInfo : UserModel? = null

    override fun initViewbinding(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewbinding = FragmentMainBinding.inflate(inflater, container, false)
        return viewbinding.root
    }

    override fun initViewStart(savedInstanceState: Bundle?) {
        requireActivity().onBackPressedDispatcher.addCallback(this) { viewmodel.onBackPressed() }
    }

    override fun initDataBinding(savedInstanceState: Bundle?) {
        viewmodel.onBackPressedEventLiveData.observe(viewLifecycleOwner){ requireActivity().finish() }

        viewmodel.run {
            onSuccessGettingUserInfo.observe(viewLifecycleOwner){
                userInfo = it
                showProfile(it)
            }
            onSuccessSaveUserInfo.observe(viewLifecycleOwner) {
                showToast("회원정보가 변경되었습니다.")
                viewmodel.getUserInfo()
            }
            onFailureGettingNullAdminInfo.observe(viewLifecycleOwner){ restartActivity() }
            onSuccessDeleteUserInfoFromServer.observe(viewLifecycleOwner){
                if (it)  makeWithdrawalDialog()
                else showSnackbar("회원탈퇴에 실패했습니다. 골라드림 고객센터에 문의해주세요.")
            }
            onSuccessDeleteUserInfo.observe(viewLifecycleOwner){ logout() }
        }
    }

    override fun initViewFinal(savedInstanceState: Bundle?) {
        viewmodel.getUserInfo()
        viewbinding.run {
            logoutBtn.setOnClickListener { makeDialog(0,"로그아웃하시겠습니까?", "취소", "로그아웃") }
            changeInfoBtn.setOnClickListener {
                userInfo?.let { changeInfo(it) }
            }
            withdrawalBtn.setOnClickListener { makeDialog(1, "모든 정보가 삭제되며, 복구할 수 없습니다.\n" +
                    "정말 회원탈퇴 하시겠습니까?", "취소", "탈퇴") }
            writeBtn.setOnClickListener {
                findNavController().navigate(MainFragmentDirections.actionMainFragmentToWriteGraph(userInfo))
            }
        }
    }
    private fun showProfile(userInfo : UserModel){
        viewbinding.run {
            userName.text = userInfo.name
            userBirthday.text = makeBirthText(userInfo.birth)
            userSex.text = userInfo.sex
            userHeight.text = makeHeightText(userInfo.height)
            userWeight.text = makeWeightText(userInfo.weight)
        }
    }
    private fun makeDialog(type : Int, content : String, closeBtn : String, customBtn : String){
        val dialog = WrapedDialogBasicTwoButton(requireContext(), content, closeBtn, customBtn).apply {
            clickListener = object : WrapedDialogBasicTwoButton.DialogButtonClickListener{
                override fun dialogCloseClickListener() { dismiss() }
                override fun dialogCustomClickListener() {
                    when (type){
                        0 -> viewmodel.deleteUserInfoFromAppDatabase()
                        1 -> viewmodel.deleteAdminInfoFromServerDatabase()
                    }
                    dismiss()
                }
            }
        }
        showDialog(dialog, viewLifecycleOwner)
    }
    private fun makeWithdrawalDialog(){
        val dialog = WithdrawalDialog(requireContext()).apply {
            clickListener = object : WithdrawalDialog.DialogButtonClickListener{
                override fun dialogConfirmClickListener() {
                    viewmodel.deleteUserInfoFromAppDatabase()
                    dismiss()
                }
            }
        }
        showDialog(dialog, viewLifecycleOwner)
    }
    private fun changeInfo(userInfo: UserModel){
        val bottomSheetFragment = ChangeInfoBottomSheetDialog(userInfo){ viewmodel.saveUserInfo(it) }
        bottomSheetFragment.isCancelable = false
        bottomSheetFragment.show(childFragmentManager, bottomSheetFragment.tag)
    }
    private fun logout() {
        try {
            Intent(context, MainActivity::class.java).apply {
                requireActivity().finish()
                startActivity(this)
            }
            requireActivity().finish()
        } catch (e: IOException) {
            findNavController().navigate(R.id.action_global_introFragment)
        }
    }

}