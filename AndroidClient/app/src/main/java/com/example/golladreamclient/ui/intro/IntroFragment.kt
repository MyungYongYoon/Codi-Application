package com.example.golladreamclient.ui.intro

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.golladreamclient.R
import com.example.golladreamclient.base.BaseFragment
import com.example.golladreamclient.databinding.FragmentIntroBinding


class IntroFragment : BaseFragment<FragmentIntroBinding, IntroViewModel>(){
    override lateinit var viewbinding: FragmentIntroBinding
    override val viewmodel: IntroViewModel by viewModels()

    override fun initViewbinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewbinding = FragmentIntroBinding.inflate(inflater, container, false)
        return viewbinding.root
    }

    override fun initViewStart(savedInstanceState: Bundle?) {
        requireActivity().onBackPressedDispatcher.addCallback(this) { viewmodel.onBackPressed() }
    }

    override fun initDataBinding(savedInstanceState: Bundle?) {
        viewmodel.onBackPressedEventLiveData.observe(viewLifecycleOwner){ requireActivity().finish() }
    }

    override fun initViewFinal(savedInstanceState: Bundle?) {
        viewbinding.run {
            signupBtn.setOnClickListener {
                findNavController().navigate(R.id.action_introFragment_to_signUpGraph)
            }
            loginBtn.setOnClickListener {
                findNavController().navigate(R.id.action_introFragment_to_signInGraph)
            }
        }
    }
}

