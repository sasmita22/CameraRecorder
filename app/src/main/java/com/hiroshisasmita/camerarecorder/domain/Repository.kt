package com.hiroshisasmita.camerarecorder.domain

import java.io.File

interface Repository {

    suspend fun uploadFile(file: File)
}