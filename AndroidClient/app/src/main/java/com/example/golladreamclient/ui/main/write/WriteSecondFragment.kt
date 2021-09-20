package com.example.golladreamclient.ui.main.write

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.example.golladreamclient.R
import com.example.golladreamclient.base.BaseSessionFragment
import com.example.golladreamclient.databinding.FragmentWriteSecondBinding

class WriteSecondFragment : BaseSessionFragment<FragmentWriteSecondBinding, WriteViewModel>(){
    override lateinit var viewbinding: FragmentWriteSecondBinding
    override val viewmodel: WriteViewModel by navGraphViewModels(R.id.writeGraph)
    private var nextAvailable : Boolean = false
    private var selectedColor : SelectedColor?= null

    override fun initViewbinding(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewbinding = FragmentWriteSecondBinding.inflate(inflater, container, false)
        return viewbinding.root
    }

    override fun initViewStart(savedInstanceState: Bundle?) {
        viewmodel.receiveThirdWriteInfo(null)
        viewbinding.backBtn.setOnClickListener { findNavController().navigate(R.id.action_writeSecondFragment_pop) }
    }

    override fun initDataBinding(savedInstanceState: Bundle?) {

    }

    override fun initViewFinal(savedInstanceState: Bundle?) {
        viewbinding.run {
            initButtonView()
            submitBtn.setOnClickListener {
                if (nextAvailable) {
                    viewmodel.saveSecondWriteInfo(selectedColor!!.name)
                    findNavController().navigate(R.id.action_writeSecondFragment_to_writeThirdFragment)
                }
            }
        }
    }

    private fun initButtonView(){
        viewbinding.run {
            firstStyleBtn.setOnClickListener {  getSelectedColor(it) }
            secondStyleBtn.setOnClickListener { getSelectedColor(it) }
            thirdStyleBtn.setOnClickListener { getSelectedColor(it) }
            fourthStyleBtn.setOnClickListener { getSelectedColor(it) }
            fifthStyleBtn.setOnClickListener { getSelectedColor(it) }
            sixStyleBtn.setOnClickListener { getSelectedColor(it) }
            sevenStyleBtn.setOnClickListener { getSelectedColor(it) }
            eightStyleBtn.setOnClickListener { getSelectedColor(it) }
            nineStyleBtn.setOnClickListener { getSelectedColor(it) }
            tenStyleBtn.setOnClickListener { getSelectedColor(it) }
            elevenStyleBtn.setOnClickListener { getSelectedColor(it) }
            twelveStyleBtn.setOnClickListener { getSelectedColor(it) }
            thirteenStyleBtn.setOnClickListener { getSelectedColor(it) }
            fourteenStyleBtn.setOnClickListener { getSelectedColor(it) }
            fifteenStyleBtn.setOnClickListener { getSelectedColor(it) }
            sixteenStyleBtn.setOnClickListener { getSelectedColor(it) }
            seventeenStyleBtn.setOnClickListener { getSelectedColor(it) }
            radioGroup1.setOnCheckedChangeListener { _, _ ->
                setButtonView()
            }
            radioGroup2.setOnCheckedChangeListener { _, _ ->
                setButtonView()
            }
            radioGroup3.setOnCheckedChangeListener { _, _ ->
                setButtonView()
            }
            radioGroup4.setOnCheckedChangeListener { _, _ ->
                setButtonView()
            }
            radioGroup5.setOnCheckedChangeListener { _, _ ->
                setButtonView()
            }
            radioGroup6.setOnCheckedChangeListener { _, _ ->
                setButtonView()
            }
        }
    }
    private fun setButtonView(){
        viewbinding.run {
            if (nextAvailable) submitBtn.setBackgroundResource(R.drawable.button_5dp_rectangle_purple)
            else submitBtn.setBackgroundResource(R.drawable.button_5dp_rectangle_black20)
        }
    }

    private fun getSelectedColor(view: View){
        nextAvailable = true
        viewbinding.run {
            when (view.id){
                firstStyleBtn.id -> {
                    clearRadioButtons(1)
                    selectedColor = SelectedColor(1, 0, "리조트")
                }
                secondStyleBtn.id -> {
                    clearRadioButtons(1)
                    selectedColor = SelectedColor(1, 1, "에스닉")
                }
                thirdStyleBtn.id -> {
                    clearRadioButtons(1)
                    selectedColor = SelectedColor(1, 2, "프레피")
                }
                fourthStyleBtn.id -> {
                    clearRadioButtons(2)
                    selectedColor = SelectedColor(2, 0, "페미닌")
                }
                fifthStyleBtn.id -> {
                    clearRadioButtons(2)
                    selectedColor = SelectedColor(2, 1, "매니시")
                }
                sixStyleBtn.id -> {
                    clearRadioButtons(2)
                    selectedColor = SelectedColor(2, 2, "젠더리스")
                }
                sevenStyleBtn.id -> {
                    clearRadioButtons(3)
                    selectedColor = SelectedColor(3, 0, "힙합")
                }
                eightStyleBtn.id -> {
                    clearRadioButtons(3)
                    selectedColor = SelectedColor(3, 1, "펑크")
                }
                nineStyleBtn.id -> {
                    clearRadioButtons(3)
                    selectedColor = SelectedColor(3, 2, "스트릿")
                }
                tenStyleBtn.id -> {
                    clearRadioButtons(4)
                    selectedColor = SelectedColor(4, 0, "밀리터리")
                }
                elevenStyleBtn.id -> {
                    clearRadioButtons(4)
                    selectedColor = SelectedColor(4, 1, "스포티")
                }
                twelveStyleBtn.id -> {
                    clearRadioButtons(4)
                    selectedColor = SelectedColor(4, 2, "아방가르드")
                }
                thirteenStyleBtn.id -> {
                    clearRadioButtons(5)
                    selectedColor = SelectedColor(5, 0, "레트로")
                }
                fourteenStyleBtn.id -> {
                    clearRadioButtons(5)
                    selectedColor = SelectedColor(5, 1, "컨트리")
                }
                fifteenStyleBtn.id -> {
                    clearRadioButtons(5)
                    selectedColor = SelectedColor(5, 2, "키덜트")
                }
                sixteenStyleBtn.id -> {
                    clearRadioButtons(6)
                    selectedColor = SelectedColor(6, 0, "모던")
                }
                seventeenStyleBtn.id -> {
                    clearRadioButtons(6)
                    selectedColor = SelectedColor(6, 1, "소피스트케이티드")
                }
                
            }
        }
    }
    private fun clearRadioButtons(type : Int){
        viewbinding.run {
            if (type!=1) radioGroup1.clearCheck()
            if (type!=2) radioGroup2.clearCheck()
            if (type!=3) radioGroup3.clearCheck()
            if (type!=4) radioGroup4.clearCheck()
            if (type!=5) radioGroup5.clearCheck()
            if (type!=6) radioGroup6.clearCheck()
        }
    }

}
data class SelectedColor(val groupIndex : Int, val position: Int, val name : String)