package com.hiroshisasmita.camerarecorder.utils

import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

fun File.createPartMap(publicId: String, apiKey: String): Map<String,RequestBody> {
    val map = mutableMapOf<String, RequestBody>()
    map["upload_preset"] = "ml_default".toRequestBody("text/plain;charset=utf-8".toMediaTypeOrNull())
    map["public_id"] = publicId.toRequestBody("text/plain;charset=utf-8".toMediaTypeOrNull())
    map["api_key"] = apiKey.toRequestBody("text/plain;charset=utf-8".toMediaTypeOrNull())
    return map
}

fun File.asMultipartBody(): MultipartBody.Part {
    val requestBodyFile = this.asRequestBody("video/*".toMediaTypeOrNull())
    return MultipartBody.Part.createFormData(
        name = "file",
        filename = this.name,
        requestBodyFile
    )
}