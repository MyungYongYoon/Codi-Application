package com.example.golladreamclient.ui.main.image

import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.database.ContentObserver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.graphics.Matrix
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.exifinterface.media.ExifInterface
import androidx.fragment.app.Fragment
import com.example.golladreamclient.data.model.MediaStoreImage
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.net.URLEncoder
import java.text.SimpleDateFormat
import java.util.*

object ImageUtils {
    const val MAIN_SETTINGS_PROFILE_QUALITY = 30
    const val SIGN_UP_COMPANY_INFO_LICENSE_IMAGE_QUALITY = 50

    private lateinit var currentPhotoPath: String

    val EMPTY_IMAGE = MediaStoreImage(id = 0, displayName = "CAMERA", contentUri = "CAMERA".toUri())

    fun Fragment.getActivityResultLauncherForCamera(callback: (image: File) -> Unit): ActivityResultLauncher<Intent> {
        return registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult ->
            if (activityResult.resultCode == Activity.RESULT_OK) {
                val imageFile = getResizedImageFile(
                    requireContext(),
                    currentPhotoPath,
                    SIGN_UP_COMPANY_INFO_LICENSE_IMAGE_QUALITY
                )

                callback(imageFile)
            } else Toast.makeText(requireContext(),"요청이 취소되었습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    @Throws(IOException::class)
    fun createImageFile(context: Context): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.KOREA).format(Date())
        val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)

        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply { currentPhotoPath = absolutePath }
    }

    @Throws(IOException::class, NullPointerException::class)
    fun getImageFileFromUri(context: Context, imageUri: Uri, quality: Int = 50): File {
        // get image bitmap through using imageUri
        val selectedImage = BitmapFactory.Options().run {
            inJustDecodeBounds = true

            BitmapFactory.decodeStream(context.contentResolver.openInputStream(imageUri), null, this)

            inSampleSize = this.calculateInSampleSize()

            inJustDecodeBounds = false

            BitmapFactory.decodeStream(context.contentResolver.openInputStream(imageUri), null, this)
        }

        // imageUri 사용하여 cacheDir 위치에 같은 이름의 File 생성
        val savedImageFile = File(context.cacheDir, context.contentResolver.getFileName(imageUri))
        // image file EXIF Interface 생성
        val exif = ExifInterface(context.contentResolver.getFilePath(imageUri))

        val rotatedImage = when (exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)) {
            ExifInterface.ORIENTATION_ROTATE_90 -> selectedImage?.rotate(90)
            ExifInterface.ORIENTATION_ROTATE_180 -> selectedImage?.rotate(180)
            ExifInterface.ORIENTATION_ROTATE_270 -> selectedImage?.rotate(270)
            else -> selectedImage
        }

        val imageOutputStream = FileOutputStream(savedImageFile)

        rotatedImage!!.compress(Bitmap.CompressFormat.JPEG, quality, imageOutputStream)
        imageOutputStream.flush()
        imageOutputStream.close()

        return savedImageFile
    }

    @Throws(IOException::class)
    fun getResizedImageFile(context: Context, imageFilePath: String, quality: Int): File {
        //val imageUri = data?.data
        val file = File(imageFilePath)
        val selectedImage = BitmapFactory.Options().run {
            inJustDecodeBounds = true
            BitmapFactory.decodeStream(FileInputStream(file), null, this)
            inSampleSize = this.calculateInSampleSize()

            inJustDecodeBounds = false
            BitmapFactory.decodeStream(FileInputStream(file), null, this)
        }

        val savedImageFile = File(context.cacheDir, SimpleDateFormat("yyyyMMdd_HHmmss", Locale.KOREA).format(
            Date()
        ) + ".jpg")
        val exif = ExifInterface(imageFilePath)
        val rotatedImage = when (exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)) {
            ExifInterface.ORIENTATION_ROTATE_90 -> selectedImage?.rotate(90)
            ExifInterface.ORIENTATION_ROTATE_180 -> selectedImage?.rotate(180)
            ExifInterface.ORIENTATION_ROTATE_270 -> selectedImage?.rotate(270)
            else -> selectedImage
        }
        val imageOutputStream = FileOutputStream(savedImageFile)
        rotatedImage!!.compress(Bitmap.CompressFormat.JPEG, quality, imageOutputStream)
        imageOutputStream.flush()
        imageOutputStream.close()
        return savedImageFile
    }
}

fun Uri.convertUriToBitmap(contentResolver: ContentResolver): Bitmap {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        ImageDecoder.decodeBitmap(ImageDecoder.createSource(contentResolver, this))
    } else MediaStore.Images.Media.getBitmap(contentResolver, this)
}

fun Bitmap.convertBitmapToFile(context: Context): File? {
    return try {
        val newFile = ImageUtils.createImageFile(context)
        val outputStream = FileOutputStream(newFile)

        compress(Bitmap.CompressFormat.JPEG, 50, outputStream)

        outputStream.close()

        newFile
    } catch (e: Exception) {
        null
    }
}

fun Bitmap.rotate(degrees: Int): Bitmap? {
    val matrix = Matrix()
    matrix.postRotate(degrees.toFloat())
    return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
}

fun BitmapFactory.Options.calculateInSampleSize(reqWidth: Int = 1024, reqHeight: Int = 1024): Int {
    // Raw height and width of image
    val (height: Int, width: Int) = this.run { outHeight to outWidth }
    var inSampleSize = 1

    if (height > reqHeight || width > reqWidth) {

        val halfHeight: Int = height / 2
        val halfWidth: Int = width / 2

        // Calculate the largest inSampleSize value that is a power of 2 and keeps both
        // height and width larger than the requested height and width.
        while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
            inSampleSize *= 2
        }
    }

    return inSampleSize
}

fun ContentResolver.getFileName(fileUri: Uri): String {
    var name = ""
    val returnCursor = this.query(fileUri, null, null, null, null)
    if (returnCursor != null) {
        val nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        returnCursor.moveToFirst()
        name = returnCursor.getString(nameIndex)
        returnCursor.close()
    }
    return name
}

fun ContentResolver.getFilePath(uri: Uri): String {
    var path = ""
    val cursor = this.query(uri, null, null, null, null)
    if (cursor != null) {
        val pathIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        path = cursor.getString(pathIndex)
        cursor.close()
    }

    return path
}

fun ContentResolver.registerObserver(uri: Uri, observer: (selfChange: Boolean) -> Unit): ContentObserver {
    val contentObserver = object : ContentObserver(Handler(Looper.getMainLooper())) {
        override fun onChange(selfChange: Boolean) {
            observer(selfChange)
        }
    }
    registerContentObserver(uri, true, contentObserver)
    return contentObserver
}

fun String.getURLEncodedFileName(): String = URLEncoder.encode(this, "UTF-8")