package com.example.golladreamclient.ui.main.write

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.NavArgs
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import com.example.golladreamclient.R
import com.example.golladreamclient.base.BaseFragment
import com.example.golladreamclient.base.BaseSessionFragment
import com.example.golladreamclient.data.model.UserModel
import com.example.golladreamclient.databinding.FragmentWriteFirstBinding
import com.example.golladreamclient.ui.makeBirthText
import com.example.golladreamclient.ui.makeHeightText
import com.example.golladreamclient.ui.makeWeightText
import com.example.golladreamclient.utils.ChangeInfoBottomSheetDialog

class WriteFirstFragment : BaseSessionFragment<FragmentWriteFirstBinding, WriteViewModel>(){
    override lateinit var viewbinding: FragmentWriteFirstBinding
    override val viewmodel: WriteViewModel by navGraphViewModels(R.id.writeGraph)
    private val args : WriteFirstFragmentArgs by navArgs()
    private var userInfo : UserModel? = null

    override fun initViewbinding(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewbinding = FragmentWriteFirstBinding.inflate(inflater, container, false)
        return viewbinding.root
    }

    override fun initViewStart(savedInstanceState: Bundle?) {
        userInfo = args.userInfo
        viewbinding.backBtn.setOnClickListener { findNavController().navigate(R.id.action_writeFirstFragment_pop) }
    }

    override fun initDataBinding(savedInstanceState: Bundle?) {
        viewmodel.onSuccessSaveUserInfo.observe(viewLifecycleOwner) {
            showToast("프로필이 수정되었습니다.")
            userInfo?.let { showProfile(it) }
        }
    }
    override fun initViewFinal(savedInstanceState: Bundle?) {
        userInfo?.let { showProfile(it) }
        viewbinding.changeInfoText.setOnClickListener { userInfo?.let { changeInfo(it) } }
        viewbinding.submitBtn.setOnClickListener {
            viewmodel.saveFirstWriteInfo(userInfo!!)
            findNavController().navigate(R.id.action_writeFirstFragment_to_writeSecondFragment)
        }
    }
    private fun showProfile(userInfo : UserModel){
        viewbinding.run {
            editTextNameInfo.setText(userInfo.name)
            editTextBirthInfo.setText(makeBirthText(userInfo.birth))
            editTextSexInfo.setText(userInfo.sex)
            editTextHeightInfo.setText(makeHeightText(userInfo.height))
            editTextWeightInfo.setText(makeWeightText(userInfo.weight))
        }
    }
    private fun changeInfo(userdata: UserModel){
        val bottomSheetFragment = ChangeInfoBottomSheetDialog(userdata){
            userInfo = it
            viewmodel.saveUserInfo(it)
        }
        bottomSheetFragment.isCancelable = false
        bottomSheetFragment.show(childFragmentManager, bottomSheetFragment.tag)
    }

}