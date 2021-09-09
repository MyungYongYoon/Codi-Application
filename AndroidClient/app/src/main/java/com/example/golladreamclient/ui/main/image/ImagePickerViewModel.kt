package com.example.golladreamclient.ui.main.image

import android.app.Application
import android.content.ContentUris
import android.database.ContentObserver
import android.database.Cursor
import android.provider.MediaStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.golladreamclient.data.model.MediaStoreImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ImagePickerViewModel(application: Application) : AndroidViewModel(application) {

    private val _mediaStoreImages = MutableLiveData<List<MediaStoreImage>>()
    val mediaStoreImages: LiveData<List<MediaStoreImage>> get() = _mediaStoreImages

    private var contentObserver: ContentObserver? = null

    override fun onCleared() {
        super.onCleared()
        contentObserver?.let {
            getApplication<Application>().contentResolver.unregisterContentObserver(it)
        }
    }

    fun loadMediaStoreImages() {
        viewModelScope.launch {
            _mediaStoreImages.postValue(queryImages())

            if (contentObserver == null) {
                contentObserver = getApplication<Application>().contentResolver.registerObserver(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                ) {
                    loadMediaStoreImages()
                }
            }
        }
    }

    private suspend inline fun queryImages(): List<MediaStoreImage> {
        val images = mutableListOf<MediaStoreImage>()
        // 이미지 가져오는 작업 늦어질 수 있으므로 main thread 와 분리하여 진행
        withContext(Dispatchers.IO) {
            /**
             * [MediaStoreImage] 형식에 맞게 projection 해야 한다.
             * uri 는 제외한 projection
             */
            /**
             * [MediaStoreImage] 형식에 맞게 projection 해야 한다.
             * uri 는 제외한 projection
             */
            val projection = arrayOf(
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DISPLAY_NAME
            )
            val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"
            getApplication<Application>().contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                sortOrder
            )?.use { cursor -> images.addAll(setMediaStoreImageList(cursor)) }
        }

        return images
    }

    private fun setMediaStoreImageList(cursor: Cursor): List<MediaStoreImage> {
        val images = mutableListOf<MediaStoreImage>()
        val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
        val displayNameColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)

        while (cursor.moveToNext()) {
            val id = cursor.getLong(idColumn)
            val displayName = cursor.getString(displayNameColumn)
            val contentUri = ContentUris.withAppendedId(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                id
            )
            images += MediaStoreImage(id, displayName, contentUri)
        }
        return images
    }
}