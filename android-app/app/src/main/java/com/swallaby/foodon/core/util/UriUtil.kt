package com.swallaby.foodon.core.util

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.provider.OpenableColumns
import java.io.File
import java.io.FileOutputStream

/**
 * Uri에서 파일 경로를 가져오는 유틸리티 (Android N 미만용)
 */
object UriUtils {
    /**
     * Uri에서 파일 경로를 추출하는 메서드
     * @param context 컨텍스트
     * @param uri 파일 경로를 추출할 Uri
     * @return 파일 경로 문자열 또는 null
     */
    fun getPathFromUri(context: Context, uri: Uri): String? {
        // URI 스키마 확인
        when {
            // DocumentProvider
            DocumentsContract.isDocumentUri(context, uri) -> {
                when {
                    // ExternalStorageProvider
                    isExternalStorageDocument(uri) -> {
                        val docId = DocumentsContract.getDocumentId(uri)
                        val split = docId.split(":").toTypedArray()
                        val type = split[0]

                        if ("primary".equals(type, ignoreCase = true)) {
                            return "${Environment.getExternalStorageDirectory()}/${split[1]}"
                        }

                        // 일부 기기에서는 SD 카드 등의 추가 저장소에 대해 다른 경로를 사용할 수 있음
                        // 예: /storage/sdcard1/
                    }

                    // DownloadsProvider
                    isDownloadsDocument(uri) -> {
                        val id = DocumentsContract.getDocumentId(uri)

                        if (id.startsWith("raw:")) {
                            // Android 8.0 이상에서 raw: 접두사를 사용하는 경우
                            return id.substring(4)
                        }

                        try {
                            val contentUri = ContentUris.withAppendedId(
                                Uri.parse("content://downloads/public_downloads"), id.toLong()
                            )
                            return getDataColumn(context, contentUri, null, null)
                        } catch (e: NumberFormatException) {
                            // id가 숫자가 아닌 경우 다른 방법 시도
                            val contentUri = Uri.parse(
                                "content://downloads/all_downloads/document/$id"
                            )
                            return getDataColumn(context, contentUri, null, null)
                        }
                    }

                    // MediaProvider
                    isMediaDocument(uri) -> {
                        val docId = DocumentsContract.getDocumentId(uri)
                        val split = docId.split(":").toTypedArray()
                        val type = split[0]

                        val contentUri = when (type) {
                            "image" -> MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                            "video" -> MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                            "audio" -> MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                            else -> null
                        }

                        val selection = "_id=?"
                        val selectionArgs = arrayOf(split[1])

                        return contentUri?.let {
                            getDataColumn(
                                context, it, selection, selectionArgs
                            )
                        }
                    }
                }
            }

            // MediaStore (및 일반 ContentProvider)
            "content".equals(uri.scheme, ignoreCase = true) -> {
                // Google Photos/Google Drive에서 선택한 이미지 처리
                return if (isGooglePhotosUri(uri) || isGoogleDriveUri(uri)) {
                    // Google Photos에서는 직접 경로를 얻기 어려움
                    // 임시 파일로 복사하는 방법 사용
                    copyFileToTemp(context, uri)
                } else {
                    // 일반 콘텐츠 URI
                    getDataColumn(context, uri, null, null)
                }
            }

            // 파일 URI
            "file".equals(uri.scheme, ignoreCase = true) -> {
                return uri.path
            }
        }

        return null
    }

    /**
     * ContentResolver를 통해 데이터 컬럼 값 획득
     */
    private fun getDataColumn(
        context: Context,
        uri: Uri,
        selection: String?,
        selectionArgs: Array<String>?,
    ): String? {
        var cursor: Cursor? = null
        val column = MediaStore.MediaColumns.DATA
        val projection = arrayOf(column)

        try {
            cursor = context.contentResolver.query(
                uri, projection, selection, selectionArgs, null
            )

            if (cursor != null && cursor.moveToFirst()) {
                val columnIndex = cursor.getColumnIndexOrThrow(column)
                return cursor.getString(columnIndex)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            cursor?.close()
        }

        return null
    }

    /**
     * 파일을 임시 파일로 복사
     * Google Photos 등에서 직접 파일 경로를 얻을 수 없는 경우 사용
     */
    private fun copyFileToTemp(context: Context, uri: Uri): String? {
        val fileName = getFileName(context, uri) ?: "temp_file"
        val tempFile = File(context.cacheDir, fileName)

        try {
            val inputStream = context.contentResolver.openInputStream(uri) ?: return null
            val outputStream = FileOutputStream(tempFile)

            val buffer = ByteArray(4 * 1024) // 4KB 버퍼
            var read: Int

            while (inputStream.read(buffer).also { read = it } != -1) {
                outputStream.write(buffer, 0, read)
            }

            outputStream.flush()
            outputStream.close()
            inputStream.close()

            return tempFile.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    /**
     * Uri에서 파일 이름 추출
     */
    private fun getFileName(context: Context, uri: Uri): String? {
        var fileName: String? = null

        if ("content".equals(uri.scheme, ignoreCase = true)) {
            val cursor = context.contentResolver.query(uri, null, null, null, null)
            cursor?.use {
                if (it.moveToFirst()) {
                    val displayNameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    if (displayNameIndex != -1) {
                        fileName = it.getString(displayNameIndex)
                    }
                }
            }
        }

        if (fileName == null) {
            fileName = uri.path
            val cut = fileName?.lastIndexOf('/')
            if (cut != -1 && cut != null) {
                fileName = fileName?.substring(cut + 1)
            }
        }

        return fileName
    }

    /**
     * URI 유형 확인 함수들
     */
    private fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri.authority
    }

    private fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.authority
    }

    private fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.authority
    }

    private fun isGooglePhotosUri(uri: Uri): Boolean {
        return "com.google.android.apps.photos.content" == uri.authority || "com.google.android.apps.photos.contentprovider" == uri.authority
    }

    private fun isGoogleDriveUri(uri: Uri): Boolean {
        return "com.google.android.apps.docs.storage" == uri.authority || "com.google.android.apps.docs.storage.legacy" == uri.authority
    }
}