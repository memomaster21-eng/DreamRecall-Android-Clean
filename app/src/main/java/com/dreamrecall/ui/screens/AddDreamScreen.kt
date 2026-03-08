package com.dreamrecall.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dreamrecall.ui.DreamViewModel
import com.dreamrecall.ui.components.DreamInputField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddDreamScreen(
    viewModel: DreamViewModel,
    onBackClick: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var body by remember { mutableStateOf("") }
    var tagsInput by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Record a Dream") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            DreamInputField(value = title, onValueChange = { title = it }, label = "Title", singleLine = true)
            DreamInputField(value = body, onValueChange = { body = it }, label = "Describe your dream...")
            DreamInputField(value = tagsInput, onValueChange = { tagsInput = it }, label = "Tags (comma separated)", singleLine = true)
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Button(
                onClick = {
                    val tags = tagsInput.split(",").map { it.trim() }.filter { it.isNotEmpty() }
                    viewModel.addDream(title, body, tags)
                    onBackClick()
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = title.isNotBlank() && body.isNotBlank()
            ) {
                Text("Save Dream")
            }
        }
    }
}
