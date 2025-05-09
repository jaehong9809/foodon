package com.swallaby.foodon.core.util

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.OpenableColumns
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


fun Uri.toMultipartBodyPart(context: Context, paramName: String = "image"): MultipartBody.Part {
    // ContentResolver를 통해 파일 데이터 가져오기
    val contentResolver = context.contentResolver

    // 파일의 실제 이름 가져오기
    val fileName = getFileName(contentResolver) ?: "image_${System.currentTimeMillis()}.jpg"

    // 임시 파일 생성
    val file = createTempFile(context, this)
    kotlin.io.path.createTempFile()
    // RequestBody 생성
    val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())

    // MultipartBody.Part 생성하여 반환
    return MultipartBody.Part.createFormData(paramName, fileName, requestBody)
}

// 파일 이름 가져오기
fun Uri.getFileName(contentResolver: ContentResolver): String? {
    // 문서 타입 URI인 경우
    if (this.scheme == "content") {
        contentResolver.query(this, null, null, null, null)?.use { cursor ->
            if (cursor.moveToFirst()) {
                val displayNameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (displayNameIndex != -1) {
                    return cursor.getString(displayNameIndex)
                }
            }
        }
    }

    // 파일 URI인 경우
    if (this.scheme == "file") {
        return this.lastPathSegment
    }

    return null
}

// URI에서 임시 파일 생성
private fun createTempFile(context: Context, uri: Uri): File {
    val inputStream = context.contentResolver.openInputStream(uri)
        ?: throw IOException("Failed to open input stream")

    // kotlin.io.path.createTempFile()을 사용하여 임시 파일 생성
    val tempFile = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val tempPath = kotlin.io.path.createTempFile(
            prefix = "upload_", suffix = ".jpg", directory = context.cacheDir.toPath()
        )
        tempPath.toFile()
    } else {
        // Android O 미만 버전에서는 기존 File API 사용
        File.createTempFile(
            "upload_", ".jpg", context.cacheDir
        )
    }

    // 파일 복사
    FileOutputStream(tempFile).use { outputStream ->
        inputStream.use { input ->
            input.copyTo(outputStream)
        }
    }

    return tempFile
}
