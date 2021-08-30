package com.example.golladreamclient.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.golladreamclient.base.BaseFragment
import com.example.golladreamclient.databinding.FragmentMainBinding

class MainFragment  : BaseFragment<FragmentMainBinding, MainViewModel>(){
    override lateinit var viewbinding: FragmentMainBinding
    override val viewmodel: MainViewModel by viewModels()

    override fun initViewbinding(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewbinding = FragmentMainBinding.inflate(inflater, container, false)
        return viewbinding.root
    }

    override fun initViewStart(savedInstanceState: Bundle?) {

    }

    override fun initDataBinding(savedInstanceState: Bundle?) {

    }

    override fun initViewFinal(savedInstanceState: Bundle?) {

    }

}