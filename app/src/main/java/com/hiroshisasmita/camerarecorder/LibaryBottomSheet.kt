package com.hiroshisasmita.camerarecorder

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FileUpload
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryBottomSheet(files: List<File>, onDismissRequest: () -> Unit, onClickUploadFile: (File) -> Unit) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest
    ) {
        if (files.isEmpty()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "No video has been recorded")
            }
            return@ModalBottomSheet
        }
        LazyColumn {
            items(files) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    VideoComponent(file = it) {
                        onClickUploadFile(it)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun VideoComponent(file: File, onClick: () -> Unit) {
    Row(
        modifier = Modifier.padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        GlideImage(
            modifier = Modifier
                .size(52.dp)
                .clip(RoundedCornerShape(4.dp)),
            model = file.toUri(),
            contentDescription = "image_video",
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(modifier = Modifier.weight(1f), text = file.name)
        Spacer(modifier = Modifier.width(16.dp))
        IconButton(
            onClick = onClick
        ) {
            Icon(
                modifier = Modifier.size(26.dp),
                imageVector = Icons.Default.FileUpload,
                contentDescription = "Upload",
                tint = Color.Black
            )
        }
    }
}
