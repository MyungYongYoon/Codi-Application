package com.example.golladreamclient.utils

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.InsetDrawable
import android.view.ViewGroup
import android.view.Window
import com.example.golladreamclient.databinding.DialogBasicOneButtonBinding
import com.example.golladreamclient.databinding.DialogBasicTwobuttonBinding
import com.example.golladreamclient.databinding.DialogFullBasicOneButtonBinding
import kotlin.system.exitProcess


class WrapedDialogBasicTwoButton (context: Context, content: String, closeBtnText: String, customBtnText: String) : Dialog(context){

    var clickListener : DialogButtonClickListener ? = null
    interface DialogButtonClickListener {
        fun dialogCloseClickListener()
        fun dialogCustomClickListener()
    }

    init {
        //setCanceledOnTouchOutside(false)
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