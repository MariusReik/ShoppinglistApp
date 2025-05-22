package com.example.shoppinglistapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.shoppinglistapp.ui.components.ShoppingItemCard
import com.example.shoppinglistapp.ui.components.StyledFloatingActionButton
import com.example.shoppinglistapp.ui.theme.*
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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        BackgroundGradientStart,
                        BackgroundGradientEnd,
                        MintGreen.copy(alpha = 0.1f)
                    )
                )
            )
    ) {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(SoftTeal40, SoftBlue40)
                            )
                        )
                ) {
                    TopAppBar(
                        title = {
                            Text(
                                text = currentList?.name ?: "Shopping List",
                                color = Color.White,
                                fontWeight = FontWeight.Medium
                            )
                        },
                        navigationIcon = {
                            IconButton(onClick = onNavigateBack) {
                                Icon(
                                    Icons.Default.ArrowBack,
                                    contentDescription = "Back",
                                    tint = Color.White
                                )
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = Color.Transparent
                        )
                    )
                }
            },
            floatingActionButton = {
                StyledFloatingActionButton(
                    onClick = { showAddDialog = true }
                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = "Add Item",
                        tint = Color.White
                    )
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
                        modifier = Modifier.fillMaxWidth().padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = SoftTeal40
                        )
                    }
                }

                if (items.isEmpty() && !isLoading) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "🛍️ No items in this list yet.\nTap + to add your first item!",
                            style = MaterialTheme.typography.titleMedium,
                            color = SoftTeal40,
                            fontWeight = FontWeight.Medium
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(items) { item ->
                            ShoppingItemCard(
                                itemName = item.name,
                                quantity = item.quantity,
                                isCompleted = item.isCompleted,
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
                title = {
                    Text(
                        "🛒 Add New Item",
                        color = SoftTeal40
                    )
                },
                text = {
                    Column {
                        OutlinedTextField(
                            value = newItemName,
                            onValueChange = { newItemName = it },
                            label = { Text("Item Name") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = SoftTeal40,
                                focusedLabelColor = SoftTeal40,
                                cursorColor = SoftTeal40
                            )
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                "Quantity:",
                                color = SoftTeal40,
                                fontWeight = FontWeight.Medium
                            )

                            OutlinedButton(
                                onClick = { if (newItemQuantity > 1) newItemQuantity-- }
                            ) {
                                Text("−", color = SoftTeal40)
                            }

                            Text(
                                text = newItemQuantity.toString(),
                                style = MaterialTheme.typography.titleMedium,
                                color = SoftTeal40,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp)
                            )

                            OutlinedButton(
                                onClick = { newItemQuantity++ }
                            ) {
                                Text("+", color = SoftTeal40)
                            }
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (newItemName.isNotBlank()) {
                                viewModel.addItem(listId, newItemName, newItemQuantity)
                                showAddDialog = false
                                newItemName = ""
                                newItemQuantity = 1
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SoftTeal40
                        )
                    ) {
                        Text("Add", color = Color.White)
                    }
                },
                dismissButton = {
                    OutlinedButton(
                        onClick = {
                            showAddDialog = false
                            newItemName = ""
                            newItemQuantity = 1
                        }
                    ) {
                        Text("Cancel", color = SoftBlue40)
                    }
                }
            )
        }
    }
}