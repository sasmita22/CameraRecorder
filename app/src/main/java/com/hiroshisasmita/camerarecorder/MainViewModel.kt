package com.hiroshisasmita.camerarecorder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class MainViewModel: ViewModel() {

    private val _files by lazy { MutableStateFlow(listOf<File>()) }
    val files = _files.asStateFlow()

    fun fetchLocalVideos(filesDir: File) {
        viewModelScope.launch {
            val files = loadVideos(filesDir)
            _files.emit(files)
        }
    }

    private suspend fun loadVideos(filesDir: File): List<File> = withContext(Dispatchers.IO) {
        return@withContext filesDir.listFiles()?.toList().orEmpty()
            .filter {
                it.isFile && it.name.endsWith(".mp4")
            }
    }

    fun uploadFile(fileToUpload: File) {
        viewModelScope.launch {

        }
    }
}