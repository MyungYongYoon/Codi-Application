package com.example.golladreamclient.data.model

import android.net.Uri

data class MediaStoreImage(
    val id: Long = 0,
    val displayName: String = "captured",
    val contentUri: Uri
)