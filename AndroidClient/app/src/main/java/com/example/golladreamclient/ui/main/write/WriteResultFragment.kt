package com.example.golladreamclient.ui.main.write

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import coil.load
import com.example.golladreamclient.R
import com.example.golladreamclient.base.BaseSessionFragment
import com.example.golladreamclient.data.model.InputData
import com.example.golladreamclient.databinding.FragmentWriteResultBinding
import com.example.golladreamclient.databinding.FragmentWriteThirdBinding
import com.example.golladreamclient.ui.makeBirthText

class WriteResultFragment : BaseSessionFragment<FragmentWriteResultBinding, WriteViewModel>(){
    override lateinit var viewbinding: FragmentWriteResultBinding
    override val viewmodel: WriteViewModel by navGraphViewModels(R.id.writeGraph)

    override fun initViewbinding(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewbinding = FragmentWriteResultBinding.inflate(inflater, container, false)
        return viewbinding.root
    }

    override fun initViewStart(savedInstanceState: Bundle?) {
        viewbinding.backBtn.setOnClickListener { findNavController().navigate(R.id.action_writeResultFragment_pop) }
    }

    override fun initDataBinding(savedInstanceState: Bundle?) {
        val inputInfo = viewmodel.getWriteInfo()
        val resultList = viewmodel.getResultInfo()
        when (resultList.size){
            0 -> setView(0, inputInfo!!)
            1 -> {
                setView(1, inputInfo!!)
                viewbinding.imageLayout1.visibility = View.VISIBLE
                viewbinding.photoImage1.load(resultList[0])
                viewbinding.textImage1.text = "추천 코디 사진"
            }
            2 -> {
                setView(2, inputInfo!!)
                viewbinding.imageLayout1.visibility = View.VISIBLE
                viewbinding.photoImage1.load(resultList[0])
                viewbinding.imageLayout2.visibility = View.VISIBLE
                viewbinding.photoImage2.load(resultList[1])
            }
            else -> showSnackbar("에러가 발생했습니다. 골라드림에 문의해주세요.")
        }
    }

    override fun initViewFinal(savedInstanceState: Bundle?) { }

    private fun setView(resultType : Int, inputInfo : InputData){
        viewbinding.run {
            photoImage0.load(inputInfo.image)
            infoContentVar1.text = inputInfo.personalInfo.name
            infoContentVar2.text = inputInfo.personalInfo.sex
            infoContentVar3.text = makeBirthText(inputInfo.personalInfo.birth)
            infoContentVar4.text = inputInfo.styleInfo
        }
        val userName = inputInfo.personalInfo.name.subSequence(1 until inputInfo.personalInfo.name.length)
        when (resultType){
            0 -> {
                val titleText = "와! "+"$userName"+"님은 패셔니스타 :)"
                viewbinding.titleText.text = titleText
            }else -> {
                val titleText = "$userName" + "님, 이런 옷은 어때요..?"
                viewbinding.titleText.text = titleText
            }
        }
    }

}
