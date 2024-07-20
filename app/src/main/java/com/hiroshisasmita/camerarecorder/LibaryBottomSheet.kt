package com.hiroshisasmita.camerarecorder

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryBottomSheet(files: List<File>, onDismissRequest: () -> Unit) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest
    ) {
        if (files.isEmpty()) {
            Row(
                modifier = Modifier.fillMaxWidth()
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

            }
        }
    }
}