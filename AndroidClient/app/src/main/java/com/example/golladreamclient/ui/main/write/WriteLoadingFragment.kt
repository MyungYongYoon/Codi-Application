package com.example.golladreamclient.ui.main.write

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import com.example.golladreamclient.R
import com.example.golladreamclient.base.BaseSessionFragment
import com.example.golladreamclient.databinding.FragmentWriteLoadingBinding

class WriteLoadingFragment : BaseSessionFragment<FragmentWriteLoadingBinding, WriteViewModel>() {
    override lateinit var viewbinding: FragmentWriteLoadingBinding
    override val viewmodel: WriteViewModel by navGraphViewModels(R.id.writeGraph)
    private val args : WriteLoadingFragmentArgs by navArgs()

    override fun initViewbinding(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewbinding = FragmentWriteLoadingBinding.inflate(inflater, container, false)
        return viewbinding.root
    }

    override fun initViewStart(savedInstanceState: Bundle?) {
        viewbinding.backBtn.setOnClickListener { findNavController().navigate(R.id.action_writeLoadingFragment_pop) }
    }

    override fun initDataBinding(savedInstanceState: Bundle?) { }

    override fun initViewFinal(savedInstanceState: Bundle?) {
        val animation = AnimationUtils.loadAnimation(context, R.anim.anim)
        viewbinding.loadingImage.startAnimation(animation)
        viewbinding.loadingText.startAnimation(animation)
        viewbinding.startBtn.startAnimation(animation)
        viewbinding.startBtn.setOnClickListener {
            findNavController().navigate(WriteLoadingFragmentDirections.actionWriteLoadingFragmentToWriteFirstFragment(args.userInfo))
        }
    }


}