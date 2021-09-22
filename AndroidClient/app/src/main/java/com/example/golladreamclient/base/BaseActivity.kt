package com.example.userapp.base

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding
import com.example.golladreamclient.base.BaseActivityViewModel
import com.example.golladreamclient.utils.LoadingIndicator
import com.google.android.material.snackbar.Snackbar
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BaseActivity<VB : ViewBinding, VM : BaseActivityViewModel>: AppCompatActivity() {

    abstract val viewbinding : VB
    abstract val viewmodel : VM
    abstract val layoutResourceId: Int

    private val compositeDisposable = CompositeDisposable()

    override fun onDestroy() {
        compositeDisposable.dispose()
        super.onDestroy()
    }

    private fun addDisposable(disposable: Disposable){
        compositeDisposable.add(disposable)
    }

    private var mLoadingIndicator: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initViewbinding()
        mLoadingIndicator = LoadingIndicator(applicationContext)

        initViewStart(savedInstanceState)
        initDataBinding(savedInstanceState)
        initViewFinal(savedInstanceState)

        initToolbar()
        //snackbarObserving()
    }

    abstract fun initViewbinding()
    abstract fun initViewStart(savedInstanceState: Bundle?)     //첫번째, 레이아웃 초기 설정- 뷰&액티비티 (ex.리사이클러뷰, 툴바, 드로어뷰)
    abstract fun initDataBinding(savedInstanceState: Bundle?)   //두번째, 데이터바인딩& RxJava 설정 (ex.RxJava observe, Databinding observe)
    abstract fun initViewFinal(savedInstanceState: Bundle?)     //세번째, 마무리 커스텀 (ex. 클릭리스너 이벤트)

    abstract fun initToolbar()

    private fun stopLoadingIndicator() {
        mLoadingIndicator?.let {
            if(it.isShowing){
                it.dismiss()
                mLoadingIndicator = null
            }
        }
    }

    @Throws(IllegalArgumentException::class)
    fun showSnackbar(message: String) {
            Snackbar.make(viewbinding.root.findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT).show() }

    //네트워크 콜처리 & 서비스 관련 추가해주기.


}

