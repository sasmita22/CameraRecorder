package com.hiroshisasmita.camerarecorder.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hiroshisasmita.camerarecorder.domain.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository
): ViewModel() {

    private val _files by lazy { MutableStateFlow(listOf<File>()) }
    val files = _files.asStateFlow()

    private val _successEvent by lazy { MutableSharedFlow<Unit>() }
    val successEvent = _successEvent.asSharedFlow()

    private val _errorEvent by lazy { MutableSharedFlow<Exception>() }
    val errorEvent = _errorEvent.asSharedFlow()

    private val _progressState by lazy { MutableStateFlow(false) }
    val progressState = _progressState.asStateFlow()

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
            _progressState.emit(true)
            try {
                repository.uploadFile(fileToUpload)
                _successEvent.emit(Unit)
            } catch (e: Exception) {
                _errorEvent.emit(e)
            } finally {
                _progressState.emit(false)
            }
        }
    }
}