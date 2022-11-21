package com.kenwang.kenapps.utils

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import com.kenwang.kenapps.BuildConfig
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

object FileUtil {

    // Must align with AndroidManifest FileProvider android:authorities
    private const val FILE_PROVIDER_AUTHORITY = "${BuildConfig.APPLICATION_ID}.fileprovider"

    fun saveImageToExternalDCIM(
        context: Context,
        bitmap: Bitmap,
        format: Bitmap.CompressFormat = Bitmap.CompressFormat.PNG
    ): Uri? {
        val suffix = format.name
        val fileName = TimeUtil.timestampToDate(
            System.currentTimeMillis(),
            "yyyyMMddHHmm"
        ) + ".$suffix"
        val contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

        val contentValues = ContentValues().apply {
            this.put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
            this.put(MediaStore.Images.Media.MIME_TYPE, "image/*")
            this.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_DCIM + "/KenApps/")
            this.put(MediaStore.MediaColumns.IS_PENDING, 1)
        }

        val uri = context.contentResolver.insert(contentUri, contentValues) ?: return null
        var outputStream: OutputStream? = null
        return try {
            outputStream = context.contentResolver.openOutputStream(uri)
            bitmap.compress(format, 100, outputStream)
            contentValues.clear()
            contentValues.put(MediaStore.MediaColumns.IS_PENDING, 0)
            context.contentResolver.update(uri, contentValues, null, null)
            uri
        } catch (e: Exception) {
            KenLog.e(Log.getStackTraceString(e))
            context.contentResolver.delete(uri, null, null)
            null
        } finally {
            outputStream?.close()
        }
    }

    fun saveImageToAppFolder(
        context: Context,
        bitmap: Bitmap,
        format: Bitmap.CompressFormat = Bitmap.CompressFormat.PNG
    ): Uri? {
        val fileName = TimeUtil.timestampToDate(
            System.currentTimeMillis(),
            "yyyyMMddHHmm"
        )
        val file = getAppFolderImageFile(context, fileName)
        val fileOutputStream = FileOutputStream(file)
        return try {
            bitmap.compress(format, 100, fileOutputStream)
            file.toUri()
        } catch (e: Exception) {
            KenLog.e(Log.getStackTraceString(e))
            null
        } finally {
            fileOutputStream.flush()
            fileOutputStream.close()
        }
    }

    fun getAppFolderImage(context: Context): File {
        val fileDir = File(context.filesDir.toString()+"/images")
        if (!fileDir.exists()) {
            fileDir.mkdir()
        }
        return fileDir
    }

    fun getAppFolderImageFile(
        context: Context,
        fileName: String,
        format: Bitmap.CompressFormat = Bitmap.CompressFormat.PNG
    ): File {
        val suffix = format.name.lowercase()
        val fileDir = getAppFolderImage(context)
        return File(fileDir, "$fileName.$suffix")
    }

    fun getAppFolderImageUri(
        context: Context,
        fileName: String,
        format: Bitmap.CompressFormat = Bitmap.CompressFormat.PNG
    ): Uri {
        val file = getAppFolderImageFile(context, fileName, format)
        return FileProvider.getUriForFile(
            context,
            FILE_PROVIDER_AUTHORITY,
            file
        )
    }

    fun deleteFileFromUri(context: Context, uri: Uri) {
        context.contentResolver.delete(uri, null, null)
    }

    fun getFileNameFromUri(context: Context, uri: Uri): String? {
        var fileName: String? = null
        context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            if (cursor.moveToFirst()) {
                val index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (index < 0) return null
                fileName = cursor.getString(index)
            }
        }
        return fileName
    }
}
