package com.example.golladreamclient.base

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import com.example.golladreamclient.R
import com.example.golladreamclient.restartActivity
import com.google.android.material.snackbar.Snackbar
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BaseSessionFragment<VB : ViewBinding, VM : BaseSessionViewModel> : Fragment() , BaseView  {


    abstract val viewbinding : VB
    abstract val viewmodel : VM
    // abstract val layoutResourceId: Int -> 프래그먼트의 경우 패키지 구조 - 없는게 나은듯.

    private val compositeDisposable = CompositeDisposable()

    override fun onDestroy() {
        compositeDisposable.dispose()
        super.onDestroy()
    }

    private fun addDisposable(disposable: Disposable){
        compositeDisposable.add(disposable)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        /*if (!viewmodel.isTokenAvailable) { this.sessionRestart() }*/
        return initViewbinding(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewStart(savedInstanceState)
        initDataBinding(savedInstanceState)
        initViewFinal(savedInstanceState)
        snackbarObserving()
        viewmodel.sessionInvalidEvent.observe(viewLifecycleOwner, { this.sessionRestart() })
    }


    abstract fun initViewbinding(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) : View?
    abstract fun initViewStart(savedInstanceState: Bundle?)     //첫번째, 레이아웃 초기 설정- 뷰&액티비티 (ex.리사이클러뷰, 툴바, 드로어뷰)
    abstract fun initDataBinding(savedInstanceState: Bundle?)   //두번째, 데이터바인딩& RxJava 설정 (ex.RxJava observe, Databinding observe)
    abstract fun initViewFinal(savedInstanceState: Bundle?)     //세번째, 마무리 커스텀 (ex. 클릭리스너 이벤트)



    fun snackbarObserving() {
        viewmodel.observeSnackbarMessageString(viewLifecycleOwner) { str ->
            if (isDetached)
                return@observeSnackbarMessageString
            //Snackbar.make(getFragmentBinding().root.rootView.findViewById(android.R.id.content), it, Snackbar.LENGTH_LONG).show()
            activity?.let { activity ->
                val snackbar : Snackbar = Snackbar.make(activity.findViewById(android.R.id.content), str, Snackbar.LENGTH_SHORT)
                snackbar.view.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
                snackbar.show() }
            //(snackbar.view.findViewById(R.id.snackbar_text) as TextView).maxLines = 5
        }
    }

    @Throws(IllegalArgumentException::class)
    override fun showSnackbar(message: String) {
        if (isDetached) return
        activity?.let { activity ->
            val snackbar : Snackbar = Snackbar.make(activity.findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT)
            snackbar.view.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
            snackbar.show() } }


    override fun showToast(message: String) { Toast.makeText(activity, message, Toast.LENGTH_SHORT).show() }
    override fun showToast(stringRes: Int) { Toast.makeText(activity, stringRes, Toast.LENGTH_SHORT).show() }

    /*override fun setupKeyboardHide(view: View, activity: Activity?) {
        if (view !is EditText || view !is Button) {
            view.setOnTouchListener { _, _ ->
                activity?.let {
                    val imm = it.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(it.currentFocus?.windowToken, 0)
                }
                return@setOnTouchListener false
            }
        }
        if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                setupKeyboardHide(view.getChildAt(i), activity)
            }
        }
    }*/

/*    override fun setToolbarTitle(title: String?) {
        (activity as AppCompatActivity).apply {
            setSupportActionBar(findViewById(R.id.toolbar))
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            when (!TextUtils.isEmpty(title)) {
                true -> supportActionBar?.title = title
                false -> supportActionBar?.setDisplayShowTitleEnabled(false)
            }
        }
    }*/


    // lifecycle aware dialog
    fun showDialog(dialog: Dialog, lifecycleOwner: LifecycleOwner?, cancelable: Boolean = true, dismissHandler: (() -> Unit)? = null) {
        val targetEvent = if (cancelable) Lifecycle.Event.ON_STOP else Lifecycle.Event.ON_DESTROY
        val observer = LifecycleEventObserver { _: LifecycleOwner, event: Lifecycle.Event ->
            if (event == targetEvent && dialog.isShowing) {
                dialog.dismiss()
                dismissHandler?.invoke()
            }
        }
        dialog.show()
        lifecycleOwner?.lifecycle?.addObserver(observer)
        dialog.setOnDismissListener { lifecycleOwner?.lifecycle?.removeObserver(observer) }

    }
    // Permission
    fun requestPermission(
        requestPermissionLauncher: ActivityResultLauncher<String>,
        permission: String,
        rationaleMessage: String,
        onGranted: () -> Unit
    ) {
        when {
            ContextCompat.checkSelfPermission(requireContext(), permission) == PackageManager.PERMISSION_GRANTED -> {
                onGranted()
            }   //이미 승인 된 경우
            shouldShowRequestPermissionRationale(permission) -> {
                showPermissionRationale(rationaleMessage)
            }   //처음인 경우
            else -> requestPermissionLauncher.launch(permission)
        }
    }

    fun showPermissionRationale(message: String) {
        androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setTitle("권한 요청")
            .setMessage(message)
            .setPositiveButton("확인") { _, _ ->
                goToSettings()
            }
            .setNegativeButton("취소") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun goToSettings() {
        val uri = Uri.parse("package:${requireActivity().packageName}")
        startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, uri))
    }
}

fun <VB: ViewBinding, VM: BaseSessionViewModel> BaseSessionFragment<VB, VM>.sessionRestart() {
    showSnackbar("세션이 만료되어서 앱을 재시작합니다.")
    Handler(Looper.getMainLooper()).postDelayed({
        this.restartActivity()
    }, 1000L)
}