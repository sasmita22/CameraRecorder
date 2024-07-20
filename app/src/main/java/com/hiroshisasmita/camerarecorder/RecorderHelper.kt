package com.hiroshisasmita.camerarecorder

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.camera.video.FileDescriptorOutputOptions
import androidx.camera.video.FileOutputOptions
import androidx.camera.video.MediaStoreOutputOptions
import androidx.camera.video.Recording
import androidx.camera.video.VideoCapture
import androidx.camera.video.VideoRecordEvent
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.video.AudioConfig
import androidx.core.content.ContextCompat
import java.io.File

class RecorderHelper(
    private val context: Context,
    private val onRecordStateChange: (RecordState) -> Unit
) {

    companion object {
        sealed class RecordState {
            data object Recording: RecordState()
            data object Succeed: RecordState()
            data class Error(val error: Throwable?): RecordState()
        }
    }

    private var recording: Recording? = null

    @SuppressLint("MissingPermission")
    fun recordVideo(controller: LifecycleCameraController) {
        if(recording != null) {
            recording?.stop()
            recording = null
            return
        }

        val outputFile = File(context.filesDir, "video-file.mp4")
        recording = controller.startRecording(
            FileOutputOptions.Builder(outputFile).build(),
            AudioConfig.create(true),
            ContextCompat.getMainExecutor(context)
        ) { event ->
            when(event) {
                is VideoRecordEvent.Start -> {
                    onRecordStateChange.invoke(RecordState.Recording)
                }
                is VideoRecordEvent.Finalize -> {
                    if(event.hasError()) {
                        recording?.close()
                        recording = null

                        onRecordStateChange.invoke(RecordState.Error(event.cause))
                    } else {
                        onRecordStateChange.invoke(RecordState.Succeed)
                    }
                }
            }
        }
    }
}