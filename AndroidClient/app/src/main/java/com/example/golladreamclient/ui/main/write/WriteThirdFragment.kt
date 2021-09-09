package com.example.golladreamclient.ui.main.write

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import coil.load
import com.example.golladreamclient.R
import com.example.golladreamclient.base.BaseSessionFragment
import com.example.golladreamclient.data.model.InputData
import com.example.golladreamclient.databinding.FragmentWriteThirdBinding
import java.io.File

class WriteThirdFragment  : BaseSessionFragment<FragmentWriteThirdBinding, WriteViewModel>(){
    override lateinit var viewbinding: FragmentWriteThirdBinding
    override val viewmodel: WriteViewModel by navGraphViewModels(R.id.writeGraph)
    private var nextAvailable : Boolean = false
    private var selectedPicture : File?= null
    private val requestReadStoragePermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()) {
        if (it) startAlbum()
        else showSnackbar(resources.getString(R.string.permission_denied))
    }

    override fun initViewbinding(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewbinding = FragmentWriteThirdBinding.inflate(inflater, container, false)
        return viewbinding.root
    }

    override fun initViewStart(savedInstanceState: Bundle?) {
        viewbinding.backBtn.setOnClickListener { findNavController().navigate(R.id.action_writeThirdFragment_pop) }
    }

    override fun initDataBinding(savedInstanceState: Bundle?) {
        viewmodel.selectedImageLiveData.observe(viewLifecycleOwner) { imageFile ->
            when (imageFile) {
                null -> {
                    viewbinding.photoEmptyLayout.visibility = View.VISIBLE
                    viewbinding.photoImage.visibility = View.GONE
                    this.selectedPicture = null
                    nextAvailable = false
                    setButtonView()
                }
                else -> {
                    viewbinding.photoEmptyLayout.visibility = View.GONE
                    viewbinding.photoImage.visibility = View.VISIBLE
                    viewbinding.photoImage.load(imageFile.toUri())
                    this.selectedPicture = imageFile
                    nextAvailable = true
                    setButtonView()
                }
            }
        }
        viewmodel.onSuccessGetRecommend.observe(viewLifecycleOwner){
            //TODO
            //이거 가져올때까지 apiCall -> 로딩 처리 필요.
            //다음 페이지로 넘어가면서 (it) 전해주고 이를 뷰에 띄우기.
        }
    }

    override fun initViewFinal(savedInstanceState: Bundle?) {
        viewbinding.photoEmptyLayout.setOnClickListener { checkPhotoAlbumPermission() }
        viewbinding.photoImage.setOnClickListener { checkPhotoAlbumPermission() }
        viewbinding.submitBtn.setOnClickListener {
            if (nextAvailable) {
                selectedPicture?.let { viewmodel.saveThirdWriteInfo(it) }
                val totalInfo : InputData? = viewmodel.getWriteInfo()
                if (totalInfo == null) showToast("에러가 발생했습니다.")         //TODO : 에러처리
                else viewmodel.postRecommendInput(totalInfo)
            }
        }
    }

    private fun checkPhotoAlbumPermission() {
        requestPermission(
            requestReadStoragePermissionLauncher,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            resources.getString(R.string.permission_read_external_storage),
            this::startAlbum
        )
    }
    private fun startAlbum() {
        findNavController().navigate(R.id.action_writeThirdFragment_to_writeImagePickerFragment)
    }

    private fun setButtonView(){
        viewbinding.run {
            if (nextAvailable) submitBtn.setBackgroundResource(R.drawable.button_5dp_rectangle_purple)
            else submitBtn.setBackgroundResource(R.drawable.button_5dp_rectangle_black20)
        }
    }

}