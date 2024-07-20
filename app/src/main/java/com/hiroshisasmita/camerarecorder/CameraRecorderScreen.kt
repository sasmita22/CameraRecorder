package com.hiroshisasmita.camerarecorder

import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cameraswitch
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hiroshisasmita.camerarecorder.RecorderHelper.Companion.RecordState

@Composable
fun CameraRecorderScreen(viewModel: MainViewModel) {
    val context = LocalContext.current
    var isRecording by remember {
        mutableStateOf(false)
    }
    var isLibraryShow by remember {
        mutableStateOf(false)
    }

    val localFiles = viewModel.files.collectAsStateWithLifecycle()

    val recorderHelper: RecorderHelper = remember {
        RecorderHelper(
            context,
            onRecordStateChange = {
                isRecording = it is RecordState.Recording
                when (it) {
                    is RecordState.Error -> {
                        Toast.makeText(context, "Video Recording Failed", Toast.LENGTH_SHORT).show()
                    }
                    is RecordState.Succeed -> {
                        Toast.makeText(context, "Video Saved", Toast.LENGTH_SHORT).show()
                    }
                    else -> {}
                }
            }
        )
    }

    val controller = remember {
        LifecycleCameraController(context).apply {
            setEnabledUseCases(
                CameraController.VIDEO_CAPTURE
            )
        }
    }

    Scaffold {
        if (isLibraryShow) {
            LibraryBottomSheet(
                files = localFiles.value,
                onDismissRequest = {
                    isLibraryShow = false
                },
                onClickUploadFile = { fileToUpload ->
                    Toast.makeText(context, "upload on progress - dummy", Toast.LENGTH_SHORT).show()
                    viewModel.uploadFile(fileToUpload)
                }
            )
        }

        Box(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            CameraPreview(
                modifier = Modifier.fillMaxSize(),
                controller = controller
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                IconButton(
                    onClick = {
                        viewModel.fetchLocalVideos(context.filesDir)
                        isLibraryShow = true
                    }
                ) {
                    Icon(
                        modifier = Modifier.size(34.dp),
                        imageVector = Icons.Default.VideoLibrary,
                        contentDescription = "Library",
                        tint = Color.White
                    )
                }
                IconButton(
                    onClick = {
                        recorderHelper.recordVideo(controller)
                    }
                ) {
                    Icon(
                        modifier = Modifier.size(42.dp),
                        imageVector = Icons.Default.Videocam,
                        contentDescription = "Record Video",
                        tint = if (isRecording) Color.Red else Color.White
                    )
                }
                IconButton(
                    onClick = {
                        controller.cameraSelector =
                            if (controller.cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) {
                                CameraSelector.DEFAULT_FRONT_CAMERA
                            } else CameraSelector.DEFAULT_BACK_CAMERA
                    }
                ) {
                    Icon(
                        modifier = Modifier.size(42.dp),
                        imageVector = Icons.Default.Cameraswitch,
                        contentDescription = "Switch camera",
                        tint = Color.White
                    )
                }
            }
        }
    }
}