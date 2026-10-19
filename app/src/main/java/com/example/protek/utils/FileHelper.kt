package com.example.protek.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream

fun prepareImageFilePart(context: Context, uri: Uri): MultipartBody.Part? {
    return try {
        val contentResolver = context.contentResolver
        val inputStream = contentResolver.openInputStream(uri) ?: return null

        var bitmap = BitmapFactory.decodeStream(inputStream)
        inputStream.close()

        val maxDimension = 1600
        if (bitmap.width > maxDimension || bitmap.height > maxDimension) {
            val ratio = bitmap.width.toFloat() / bitmap.height.toFloat()
            val width: Int
            val height: Int
            if (ratio > 1) {
                width = maxDimension
                height = (maxDimension / ratio).toInt()
            } else {
                height = maxDimension
                width = (maxDimension * ratio).toInt()
            }
            bitmap = Bitmap.createScaledBitmap(bitmap, width, height, true)
        }

        val tempFile = File(context.cacheDir, "upload_compressed_${System.currentTimeMillis()}.jpg")
        val outputStream = FileOutputStream(tempFile)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream)
        outputStream.flush()
        outputStream.close()

        val requestFile = tempFile.asRequestBody("image/*".toMediaTypeOrNull())
        MultipartBody.Part.createFormData("file", tempFile.name, requestFile)
    } catch (e: Exception) {
        Log.e("FileHelper", "prepareImageFilePart: ${e.message}", )
        null
    }
}