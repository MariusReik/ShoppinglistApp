package com.example.shoppinglistapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.shoppinglistapp.data.local.entities.ShoppingItem
import com.example.shoppinglistapp.viewmodel.ShoppingListDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingListDetailScreen(
    listId: String,
    onNavigateBack: () -> Unit,
    viewModel: ShoppingListDetailViewModel = viewModel()
) {
    val currentList by viewModel.currentList.collectAsState()
    val items by viewModel.items.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    var showAddDialog by remember { mutableStateOf(false) }
    var newItemName by remember { mutableStateOf("") }
    var newItemQuantity by remember { mutableStateOf(1) }

    LaunchedEffect(listId) {
        viewModel.loadList(listId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(currentList?.name ?: "Shopping List") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Item")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            if (items.isEmpty() && !isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No items in this list yet.\nTap + to add your first item!",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(items) { item ->
                        ShoppingItemCard(
                            item = item,
                            onToggleComplete = { viewModel.toggleItemCompletion(item) },
                            onQuantityChange = { newQuantity ->
                                viewModel.updateItemQuantity(item, newQuantity)
                            },
                            onDelete = { viewModel.deleteItem(item) }
                        )
                    }
                }
            }
        }
    }

    // Add new item dialog
    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = {
                showAddDialog = false
                newItemName = ""
                newItemQuantity = 1
            },
            title = { Text("Add New Item") },
            text = {
                Column {
                    OutlinedTextField(
                        value = newItemName,
                        onValueChange = { newItemName = it },
                        label = { Text("Item Name") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Quantity: ", modifier = Modifier.padding(end = 8.dp))

                        IconButton(
                            onClick = {
                                if (newItemQuantity > 1) newItemQuantity--
                            }
                        ) {
                            Text("-", style = MaterialTheme.typography.titleLarge)
                        }

                        Text(
                            text = newItemQuantity.toString(),
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )

                        IconButton(
                            onClick = { newItemQuantity++ }
                        ) {
                            Text("+", style = MaterialTheme.typography.titleLarge)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (newItemName.isNotBlank()) {
                            viewModel.addItem(listId, newItemName, newItemQuantity)
                            showAddDialog = false
                            newItemName = ""
                            newItemQuantity = 1
                        }
                    }
                ) {
                    Text("Add")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showAddDialog = false
                        newItemName = ""
                        newItemQuantity = 1
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingItemCard(
    item: ShoppingItem,
    onToggleComplete: () -> Unit,
    onQuantityChange: (Int) -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Checkbox
            Checkbox(
                checked = item.isCompleted,
                onCheckedChange = { onToggleComplete() }
            )

            // Item name
            Text(
                text = item.name,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 12.dp),
                textDecoration = if (item.isCompleted) TextDecoration.LineThrough else TextDecoration.None,
                color = if (item.isCompleted) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface
            )

            // Quantity controls
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        if (item.quantity > 1) {
                            onQuantityChange(item.quantity - 1)
                        }
                    }
                ) {
                    Text("-", style = MaterialTheme.typography.titleMedium)
                }

                Text(
                    text = item.quantity.toString(),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )

                IconButton(
                    onClick = { onQuantityChange(item.quantity + 1) }
                ) {
                    Text("+", style = MaterialTheme.typography.titleMedium)
                }
            }

            // Delete button
            IconButton(onClick = onDelete) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Delete Item",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}