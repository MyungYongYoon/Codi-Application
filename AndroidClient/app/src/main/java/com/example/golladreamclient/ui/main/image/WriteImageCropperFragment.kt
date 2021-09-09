package com.example.golladreamclient.ui.main.image

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.example.golladreamclient.R
import com.example.golladreamclient.base.BaseSessionFragment
import com.example.golladreamclient.databinding.FragmentImageCropperBinding
import com.example.golladreamclient.ui.main.write.WriteViewModel

class WriteImageCropperFragment : BaseSessionFragment<FragmentImageCropperBinding, WriteViewModel>() {

    override lateinit var viewbinding: FragmentImageCropperBinding
    override val viewmodel: WriteViewModel by navGraphViewModels(R.id.writeGraph)
    private val imageUri by lazy { WriteImageCropperFragmentArgs.fromBundle(requireArguments()).imageUriStr.toUri() }

    override fun initViewbinding(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewbinding = FragmentImageCropperBinding.inflate(inflater, container, false)
        return viewbinding.root
    }

    override fun initViewStart(savedInstanceState: Bundle?) {
        viewbinding.imageCropperView.setImageUriAsync(imageUri)
    }

    override fun initDataBinding(savedInstanceState: Bundle?) { }

    override fun initViewFinal(savedInstanceState: Bundle?) {
        viewbinding.imageCropperCompleteButton.setOnClickListener { updateCroppedImage() }
        viewbinding.imageCropperCancelButton.setOnClickListener { findNavController().navigateUp() }
    }

    private fun updateCroppedImage() {
        val croppedBitmapImage = viewbinding.imageCropperView.croppedImage
        val croppedImageFile = croppedBitmapImage.convertBitmapToFile(requireContext())
        viewmodel.receiveThirdWriteInfo(croppedImageFile)
        findNavController().navigate(R.id.action_imageCropperFragment_pop_including_writeImagePickerFragment)
    }
}