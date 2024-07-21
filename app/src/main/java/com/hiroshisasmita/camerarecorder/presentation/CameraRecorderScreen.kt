package com.hiroshisasmita.camerarecorder.presentation

import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cameraswitch
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import com.hiroshisasmita.camerarecorder.utils.RecorderHelper
import com.hiroshisasmita.camerarecorder.utils.RecorderHelper.Companion.RecordState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun CameraRecorderScreen(viewModel: MainViewModel) {
    val context = LocalContext.current
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    var isRecording by remember {
        mutableStateOf(false)
    }
    var isLibraryShow by remember {
        mutableStateOf(false)
    }

    val localFiles = viewModel.files.collectAsStateWithLifecycle()
    val isUploading = viewModel.progressState.collectAsStateWithLifecycle()

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

    LaunchedEffect(Unit) {
        launch {
            viewModel.successEvent.flowWithLifecycle(lifecycle)
                .collectLatest {
                    Toast.makeText(context, "File uploaded", Toast.LENGTH_SHORT).show()
                }
        }
        launch {
            viewModel.errorEvent.flowWithLifecycle(lifecycle)
                .collectLatest {
                    Toast.makeText(context, "Something wrong happen", Toast.LENGTH_SHORT).show()
                }
        }
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
            Column(
                Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                if (isUploading.value) {
                    UploadLoadingComponent()
                } else {
                    Spacer(modifier = Modifier.fillMaxWidth())
                }
                CameraControllerComponent(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    isRecording = isRecording,
                    onLibraryButtonClick = {
                        if (isRecording) {
                            Toast.makeText(context, "Recording in progress", Toast.LENGTH_SHORT).show()
                            return@CameraControllerComponent
                        }
                        viewModel.fetchLocalVideos(context.filesDir)
                        isLibraryShow = true
                    },
                    onRecordButtonClick = {
                        recorderHelper.recordVideo(controller)
                    },
                    onSwitchCameraClick = {
                        if (isRecording) {
                            Toast.makeText(context, "Recording in progress", Toast.LENGTH_SHORT).show()
                            return@CameraControllerComponent
                        }
                        controller.cameraSelector =
                            if (controller.cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) {
                                CameraSelector.DEFAULT_FRONT_CAMERA
                            } else CameraSelector.DEFAULT_BACK_CAMERA
                    }
                )
            }
        }
    }
}

@Composable
fun UploadLoadingComponent() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Uploading...")
    }
}

@Composable
fun CameraControllerComponent(
    modifier: Modifier,
    isRecording: Boolean,
    onLibraryButtonClick: () -> Unit,
    onRecordButtonClick: () -> Unit,
    onSwitchCameraClick: () -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        IconButton(
            onClick = onLibraryButtonClick
        ) {
            Icon(
                modifier = Modifier.size(34.dp),
                imageVector = Icons.Default.VideoLibrary,
                contentDescription = "Library",
                tint = Color.White
            )
        }
        IconButton(
            onClick = onRecordButtonClick
        ) {
            Icon(
                modifier = Modifier.size(42.dp),
                imageVector = Icons.Default.Videocam,
                contentDescription = "Record Video",
                tint = if (isRecording) Color.Red else Color.White
            )
        }
        IconButton(
            onClick = onSwitchCameraClick
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
