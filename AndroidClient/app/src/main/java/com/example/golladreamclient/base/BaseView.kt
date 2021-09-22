package com.example.golladreamclient.base

import android.app.Activity
import android.view.View
import androidx.annotation.StringRes

interface BaseView {

    fun snackbarObserving()

    fun loadingIndicatorObserving()

    fun showSnackbar(message : String)

    fun showToast(message: String)

    fun showToast(@StringRes stringRes: Int)

}