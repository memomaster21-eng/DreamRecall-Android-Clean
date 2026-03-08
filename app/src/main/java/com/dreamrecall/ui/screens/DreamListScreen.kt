package com.dreamrecall.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dreamrecall.ui.DreamViewModel
import com.dreamrecall.ui.components.DreamCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DreamListScreen(
    viewModel: DreamViewModel,
    onAddDreamClick: () -> Unit
) {
    val dreams by viewModel.dreams.collectAsState()
    var searchQuery by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("DreamRecall") })
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onAddDreamClick,
                icon = { Icon(Icons.Default.Add, contentDescription = "Add") },
                text = { Text("New Dream") }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(horizontal = 16.dp)) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { 
                    searchQuery = it
                    viewModel.setSearchQuery(it)
                },
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                placeholder = { Text("Search dreams...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                shape = MaterialTheme.shapes.medium
            )
            
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(dreams, key = { it.id }) { dream ->
                    DreamCard(
                        dream = dream,
                        onDeleteClick = { viewModel.deleteDream(dream.id) }
                    )
                }
                item { Spacer(modifier = Modifier.height(80.dp)) }
            }
        }
    }
}
