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
            whiteColorBtn.setOnClickListener {  getSelectedColor(it) }
            grayColorBtn.setOnClickListener { getSelectedColor(it) }
            blackColorBtn.setOnClickListener { getSelectedColor(it) }
            redColorBtn.setOnClickListener { getSelectedColor(it) }
            orangeColorBtn.setOnClickListener { getSelectedColor(it) }
            yellowColorBtn.setOnClickListener { getSelectedColor(it) }
            lightGreenColorBtn.setOnClickListener { getSelectedColor(it) }
            greenColorBtn.setOnClickListener { getSelectedColor(it) }
            kakiColorBtn.setOnClickListener { getSelectedColor(it) }
            lightBlueColorBtn.setOnClickListener { getSelectedColor(it) }
            blueColorBtn.setOnClickListener { getSelectedColor(it) }
            darkBlueColorBtn.setOnClickListener { getSelectedColor(it) }
            purpleColorBtn.setOnClickListener { getSelectedColor(it) }
            lightPinkColorBtn.setOnClickListener { getSelectedColor(it) }
            pinkColorBtn.setOnClickListener { getSelectedColor(it) }
            purpleColorBtn.setOnClickListener { getSelectedColor(it) }
            brownColorBtn.setOnClickListener { getSelectedColor(it) }
            otherColorBtn.setOnClickListener { getSelectedColor(it) }
            noColorBtn.setOnClickListener { getSelectedColor(it) }
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
                whiteColorBtn.id -> {
                    clearRadioButtons(1)
                    selectedColor = SelectedColor(1, 0, "흰색")
                }
                grayColorBtn.id -> {
                    clearRadioButtons(1)
                    selectedColor = SelectedColor(1, 1, "회색")
                }
                blackColorBtn.id -> {
                    clearRadioButtons(1)
                    selectedColor = SelectedColor(1, 2, "검은색")
                }
                redColorBtn.id -> {
                    clearRadioButtons(2)
                    selectedColor = SelectedColor(2, 0, "빨간색")
                }
                orangeColorBtn.id -> {
                    clearRadioButtons(2)
                    selectedColor = SelectedColor(2, 1, "주황색")
                }
                yellowColorBtn.id -> {
                    clearRadioButtons(2)
                    selectedColor = SelectedColor(2, 2, "노란색")
                }
                lightGreenColorBtn.id -> {
                    clearRadioButtons(3)
                    selectedColor = SelectedColor(3, 0, "연두색")
                }
                greenColorBtn.id -> {
                    clearRadioButtons(3)
                    selectedColor = SelectedColor(3, 1, "초록색")
                }
                kakiColorBtn.id -> {
                    clearRadioButtons(3)
                    selectedColor = SelectedColor(3, 2, "카키색")
                }
                lightBlueColorBtn.id -> {
                    clearRadioButtons(4)
                    selectedColor = SelectedColor(4, 0, "하늘색")
                }
                blueColorBtn.id -> {
                    clearRadioButtons(4)
                    selectedColor = SelectedColor(4, 1, "파란색")
                }
                darkBlueColorBtn.id -> {
                    clearRadioButtons(4)
                    selectedColor = SelectedColor(4, 2, "남청색")
                }
                lightPinkColorBtn.id -> {
                    clearRadioButtons(5)
                    selectedColor = SelectedColor(5, 0, "연분홍색")
                }
                pinkColorBtn.id -> {
                    clearRadioButtons(5)
                    selectedColor = SelectedColor(5, 1, "진분홍색")
                }
                purpleColorBtn.id -> {
                    clearRadioButtons(5)
                    selectedColor = SelectedColor(5, 2, "보라색")
                }
                brownColorBtn.id -> {
                    clearRadioButtons(6)
                    selectedColor = SelectedColor(6, 0, "갈색")
                }
                otherColorBtn.id -> {
                    clearRadioButtons(6)
                    selectedColor = SelectedColor(6, 1, "기타")
                }
                noColorBtn.id -> {
                    clearRadioButtons(6)
                    selectedColor = SelectedColor(6, 2, "상관없음")
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