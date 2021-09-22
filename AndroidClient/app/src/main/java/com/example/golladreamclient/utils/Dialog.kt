package com.example.golladreamclient.utils

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.InsetDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import com.example.golladreamclient.R
import com.example.golladreamclient.data.model.UserModel
import com.example.golladreamclient.databinding.*
import com.example.golladreamclient.ui.getHeightPosition
import com.example.golladreamclient.ui.getSexPosition
import com.example.golladreamclient.ui.getWeightPosition
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlin.system.exitProcess

class LoadingIndicator(context: Context?) : Dialog(context!!) {
    init {
        setCancelable(false)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        if (window != null) window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setContentView(R.layout.dialog_loading_indicator)
    }
    override fun setOnShowListener(listener: DialogInterface.OnShowListener?) {
        super.setOnShowListener(listener)
    }
}

class WrapedDialogBasicTwoButton (context: Context, content: String, closeBtnText: String, customBtnText: String) : Dialog(context){

    var clickListener : DialogButtonClickListener ? = null
    interface DialogButtonClickListener {
        fun dialogCloseClickListener()
        fun dialogCustomClickListener()
    }

    init {
        val binding : DialogBasicTwobuttonBinding = DialogBasicTwobuttonBinding.inflate(layoutInflater)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(binding.root)
        window?.run {
            setBackgroundDrawable(InsetDrawable(ColorDrawable(Color.TRANSPARENT), 50))
            attributes.width = ViewGroup.LayoutParams.MATCH_PARENT
            attributes.height = ViewGroup.LayoutParams.WRAP_CONTENT
        } ?: exitProcess(0)

        binding.run {
            dialogContent.text = content
            dialogCloseBtn.text = closeBtnText
            dialogCustomBtn.text = customBtnText
            dialogCloseBtn.setOnClickListener { clickListener?.dialogCloseClickListener()}
            dialogCustomBtn.setOnClickListener { clickListener?.dialogCustomClickListener() }
        }
    }
}


class MatchedFullDialogBasicOneButton (context: Context, title: String, content: String) : Dialog(context){
    var clickListener : DialogButtonClickListener ? = null
    interface DialogButtonClickListener {
        fun dialogCloseClickListener()
        fun dialogClickListener()
    }
    init {
        val binding : DialogFullBasicOneButtonBinding = DialogFullBasicOneButtonBinding.inflate(layoutInflater)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(binding.root)
        window?.run {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            attributes.width = ViewGroup.LayoutParams.MATCH_PARENT
            attributes.height = ViewGroup.LayoutParams.MATCH_PARENT
        } ?: exitProcess(0)

        binding.dialogTitle.text = title
        binding.dialogContent.text = content
        binding.dialogCloseBtn.setOnClickListener { clickListener?.dialogCloseClickListener()}
        binding.dialogBtn.setOnClickListener { clickListener?.dialogClickListener() }
    }
}


class WrapedDialogBasicOneButton (context: Context, content: String) : Dialog(context){
    var clickListener : DialogButtonClickListener ? = null
    interface DialogButtonClickListener {
        fun dialogClickListener()
    }
    init {
        val binding : DialogBasicOneButtonBinding = DialogBasicOneButtonBinding.inflate(layoutInflater)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(binding.root)
        window?.run {
            setBackgroundDrawable(InsetDrawable(ColorDrawable(Color.TRANSPARENT), 50))
            attributes.width = ViewGroup.LayoutParams.MATCH_PARENT
            attributes.height = ViewGroup.LayoutParams.WRAP_CONTENT
        } ?: exitProcess(0)

        binding.dialogContent.text = content
        binding.dialogBtn.setOnClickListener { clickListener?.dialogClickListener() }
    }
}

class ChangeInfoBottomSheetDialog(userData : UserModel, private val itemClick :(UserModel) -> Unit) : BottomSheetDialogFragment(){

    lateinit var viewbinding : DialogChangeInfoBinding
    private var userInfo : UserModel = userData
    private val userId : String = userData.id

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogStyle)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        viewbinding = DialogChangeInfoBinding.inflate(inflater, container, false)
        return viewbinding.root
    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        dialog.setOnShowListener {
            val bottomSheet = dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet!!).apply {
                addBottomSheetCallback(object: BottomSheetBehavior.BottomSheetCallback() {
                    override fun onSlide(bottomSheet: View, slideOffset: Float) {
                    }
                    override fun onStateChanged(bottomSheet: View, newState: Int) {
                        if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                            this@apply.state = BottomSheetBehavior.STATE_EXPANDED }
                    }
                })
            }
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED }
        return dialog
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewbinding.run {
            fragmentRootLayout.setOnClickListener { hideKeyboard(it) }
            editTextName.setText(userInfo.name)
            editTextBirthInfo.setText(userInfo.birth)
            setSpinnerView(userInfo.sex, userInfo.height, userInfo.weight)
            dialogLeftBtn.setOnClickListener { dismiss() }
            dialogRightBtn.setOnClickListener {
                userInfo = UserModel(userId, getUserName(), getUserBirth(), getUserHeight(), getUserWeight(), getUserSex())
                if (checkChangeInfo(userInfo)) {
                    itemClick(userInfo)
                    dismiss()
                }
            }
        }
    }
    private fun checkChangeInfo(userData: UserModel): Boolean{
        if (userData.name.isBlank()|| userData.name.isEmpty()) {
            setWarningMessage("이름을 입력해주세요.")
            return false
        }else if (userData.name.length < 2) {
            setWarningMessage("이름은 2글자 이상이어야 합니다.")
            return false
        }else if (!RegularExpressionUtils.validCheck(RegularExpressionUtils.Regex.NAME, userData.name)){
            setWarningMessage("제대로 된 한글형식으로 입력해주세요.")
            return false
        }else if (userData.birth.isBlank() || userData.birth.isEmpty()){
            setWarningMessage("생년월일을 입력해주세요.")
            return false
        }else if (!RegularExpressionUtils.validCheck(RegularExpressionUtils.Regex.BIRTH, userData.birth)){
            setWarningMessage("생년월일은 8자리 숫자이어야 합니다.")
            return false
        }else if (userData.sex.isBlank() || userData.sex.isEmpty()){
            setWarningMessage("성별을 선택해주세요.")
            return false
        }else if (userData.height.isBlank() || userData.height.isEmpty()){
            setWarningMessage("키를 선택해주세요.")
            return false
        }else if (userData.sex.isBlank() || userData.sex.isEmpty()){
            setWarningMessage("몸무게를 선택해주세요.")
            return false
        }else return true
    }
    private fun setSpinnerView(sex : String, height : String, weight : String){
        viewbinding.spinnerSex.apply {
            ArrayAdapter.createFromResource(context, R.array.signUp_sex, R.layout.spinner_selected)
                .also { adapter ->
                    adapter.setDropDownViewResource(R.layout.spinner_list_item)
                    this.adapter = adapter }
            setSelection(getSexPosition(sex), false)
        }
        viewbinding.spinnerHeight.apply {
            ArrayAdapter.createFromResource(context, R.array.signUp_height, R.layout.spinner_selected)
                .also { adapter ->
                    adapter.setDropDownViewResource(R.layout.spinner_list_item)
                    this.adapter = adapter }
            setSelection(getHeightPosition(height), false)
        }
        viewbinding.spinnerWeight.apply {
            ArrayAdapter.createFromResource(context, R.array.signUp_weight, R.layout.spinner_selected)
                .also { adapter ->
                    adapter.setDropDownViewResource(R.layout.spinner_list_item)
                    this.adapter = adapter }
            setSelection(getWeightPosition(weight), false)
        }
    }
    private fun getUserName() : String = viewbinding.editTextName.text.toString().trim()
    private fun getUserBirth() : String = viewbinding.editTextBirthInfo.text.toString().trim()
    private fun getUserSex() : String {
        return if (viewbinding.spinnerSex.selectedItemPosition == 0) ""
        else viewbinding.spinnerSex.selectedItem.toString()
    }
    private fun getUserHeight() : String {
        return when (viewbinding.spinnerHeight.selectedItemPosition){
            0 -> ""
            1 -> "150미만대"
            2 -> "150대"
            3 -> "160대"
            4 -> "170대"
            5 -> "180대"
            6 -> "190이상대"
            else -> ""
        }
    }
    private fun getUserWeight() : String {
        return when (viewbinding.spinnerWeight.selectedItemPosition){
            0 -> ""
            1 -> "40미만대"
            2 -> "40대"
            3 -> "50대"
            4 -> "60대"
            5 -> "7대"
            6 -> "80대"
            7 -> "90대"
            8 -> "100이상대"
            else -> ""
        }
    }
    private fun setWarningMessage(errorMessage: String) { viewbinding.dialogTextWarning.text = errorMessage }
}

class WithdrawalDialog(context: Context) : Dialog(context) {
    var clickListener: DialogButtonClickListener? = null
    interface DialogButtonClickListener { fun dialogConfirmClickListener() }

    init {
        val binding = DialogWithdrawalBinding.inflate(layoutInflater)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setCancelable(false)
        setContentView(binding.root)
        window?.run {
            setGravity(Gravity.CENTER)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            attributes.width = ViewGroup.LayoutParams.MATCH_PARENT
            attributes.height = ViewGroup.LayoutParams.WRAP_CONTENT
        } ?: exitProcess(0)
        binding.message1Textview.text = "회원탈퇴가 정상처리 되었습니다."
        binding.message2Textview.text = "이제까지 골라드림을 이용해주셔서 감사합니다 :)"
        binding.confirmBtn.setOnClickListener { clickListener?.dialogConfirmClickListener() }
    }
}



