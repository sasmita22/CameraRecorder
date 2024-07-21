package com.hiroshisasmita.camerarecorder.data.service

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.PartMap

interface ApiService {

    @Multipart
    @POST("video/upload")
    @JvmSuppressWildcards
    suspend fun uploadFile(
        @Part file: MultipartBody.Part,
        @PartMap map: Map<String, RequestBody>,
    ): Response<Any>
}