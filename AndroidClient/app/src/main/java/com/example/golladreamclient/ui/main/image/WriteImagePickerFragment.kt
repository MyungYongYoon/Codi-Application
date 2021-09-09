package com.example.golladreamclient.ui.main.image

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.golladreamclient.R
import com.example.golladreamclient.base.BaseSessionFragment
import com.example.golladreamclient.databinding.FragmentImagePickerBinding
import com.example.golladreamclient.ui.main.image.ImageUtils.EMPTY_IMAGE
import com.example.golladreamclient.ui.main.image.ImageUtils.getActivityResultLauncherForCamera
import com.example.golladreamclient.ui.main.write.WriteViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.IOException

class WriteImagePickerFragment : BaseSessionFragment<FragmentImagePickerBinding, WriteViewModel>() {
    override lateinit var viewbinding: FragmentImagePickerBinding
    override val viewmodel: WriteViewModel by navGraphViewModels(R.id.writeGraph)
    private val imageViewModel: ImagePickerViewModel by viewModels()
    private lateinit var imagePickerListAdapter: ImagePickerAdapter

    private val requestCameraPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
        if (it) startCamera() //TODO : 보기 1
        else showSnackbar(resources.getString(R.string.permission_denied))
    }
    //TODO : 보기 2
    private val activityResultLauncher = this.getActivityResultLauncherForCamera {
        startImageCropper(it) //TODO : 보기 3
    }

    override fun initViewbinding(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewbinding = FragmentImagePickerBinding.inflate(inflater, container, false)
        return viewbinding.root
    }

    override fun initViewStart(savedInstanceState: Bundle?) {
        viewbinding.backBtn.setOnClickListener { findNavController().navigateUp() }
        initImagePickerListAdapter()
    }

    override fun initDataBinding(savedInstanceState: Bundle?) {
        imageViewModel.mediaStoreImages.observe(viewLifecycleOwner) {
            val convertedList = it.toMutableList().apply { add(0, EMPTY_IMAGE) }
            imagePickerListAdapter.submitList(convertedList.toList())
        }
    }

    override fun initViewFinal(savedInstanceState: Bundle?) {
        imageViewModel.loadMediaStoreImages()
    }

    private fun initImagePickerListAdapter() {
        imagePickerListAdapter = ImagePickerAdapter { position, image ->
            when (position) {
                0 -> requestPermission(
                    requestCameraPermissionLauncher,
                    Manifest.permission.CAMERA,
                    resources.getString(R.string.permission_camera),
                    this::startCamera
                )
                else -> startImageCropper(ImageUtils.getImageFileFromUri(requireContext(), image.contentUri))
            }
        }
        viewbinding.imagePickerRv.apply {
            adapter = imagePickerListAdapter
            layoutManager = GridLayoutManager(requireContext(), 3)
        }
    }
    private fun startCamera() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { intent ->
            intent.resolveActivity(requireActivity().packageManager)?.also {
                val newFile = try {
                    ImageUtils.createImageFile(requireContext())
                } catch (e: IOException) {
                    null
                }
                newFile?.also { file ->
                    val photoUri = FileProvider.getUriForFile(
                        requireContext(),
                        "com.example.golladreamclient.fileprovider",
                        file
                    )
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                    activityResultLauncher.launch(intent)
                }
            }
        }
    }
    private fun startImageCropper(file: File) {
        val navAction = WriteImagePickerFragmentDirections.actionWriteImagePickerFragmentToImageCropperFragment(file.toUri().toString())
        findNavController().navigate(navAction)
    }
}