package com.hiroshisasmita.camerarecorder.data.repository

import com.hiroshisasmita.camerarecorder.data.service.ApiService
import com.hiroshisasmita.camerarecorder.domain.Repository
import com.hiroshisasmita.camerarecorder.utils.asMultipartBody
import com.hiroshisasmita.camerarecorder.utils.createPartMap
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.util.UUID

class RepositoryImpl(
    private val service: ApiService
): Repository {
    override suspend fun uploadFile(file: File) {
        val response = service.uploadFile(
            file.asMultipartBody(),
            file.createPartMap(UUID.randomUUID().toString(), "xxxxx")
        )

        if (!response.isSuccessful) {
            // response error happen here
            throw Exception()
        }
    }
}