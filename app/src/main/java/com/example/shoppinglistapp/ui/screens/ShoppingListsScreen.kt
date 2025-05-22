package com.example.shoppinglistapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import com.example.shoppinglistapp.ui.components.GradientTopAppBar
import com.example.shoppinglistapp.ui.components.ShoppingListCard
import com.example.shoppinglistapp.ui.components.StyledFloatingActionButton
import com.example.shoppinglistapp.ui.theme.*
import com.example.shoppinglistapp.viewmodel.ShoppingListsViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingListsScreen(
    onNavigateToDetail: (String) -> Unit,
    viewModel: ShoppingListsViewModel = viewModel()
) {
    val shoppingLists by viewModel.shoppingLists.collectAsState(initial = emptyList())
    val isLoading by viewModel.isLoading.collectAsState()

    var showAddDialog by remember { mutableStateOf(false) }
    var newListName by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        BackgroundGradientStart,
                        BackgroundGradientEnd,
                        LightCyan.copy(alpha = 0.3f)
                    )
                )
            )
    ) {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                GradientTopAppBar(title = "🛒 Shopping Lists")
            },
            floatingActionButton = {
                StyledFloatingActionButton(
                    onClick = { showAddDialog = true }
                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = "Add Shopping List",
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
                            color = SoftBlue40
                        )
                    }
                }

                if (shoppingLists.isEmpty() && !isLoading) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "✨ No shopping lists yet.\nTap + to create your first list!",
                            style = MaterialTheme.typography.titleMedium,
                            color = SoftBlue40,
                            fontWeight = FontWeight.Medium
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(shoppingLists) { shoppingList ->
                            ShoppingListCard(
                                title = shoppingList.name,
                                subtitle = "Created ${formatDate(shoppingList.createdAt)}",
                                onClick = { onNavigateToDetail(shoppingList.id) },
                                onDelete = { viewModel.deleteShoppingList(shoppingList) }
                            )
                        }
                    }
                }
            }
        }

        // Add new list dialog
        if (showAddDialog) {
            AlertDialog(
                onDismissRequest = {
                    showAddDialog = false
                    newListName = ""
                },
                title = {
                    Text(
                        "✨ Create New Shopping List",
                        color = SoftBlue40
                    )
                },
                text = {
                    OutlinedTextField(
                        value = newListName,
                        onValueChange = { newListName = it },
                        label = { Text("List Name") },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = SoftBlue40,
                            focusedLabelColor = SoftBlue40,
                            cursorColor = SoftBlue40
                        )
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (newListName.isNotBlank()) {
                                viewModel.createShoppingList(newListName)
                                showAddDialog = false
                                newListName = ""
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SoftBlue40
                        )
                    ) {
                        Text("Create", color = Color.White)
                    }
                },
                dismissButton = {
                    OutlinedButton(
                        onClick = {
                            showAddDialog = false
                            newListName = ""
                        }
                    ) {
                        Text("Cancel", color = SoftTeal40)
                    }
                }
            )
        }
    }
}

private fun formatDate(timestamp: Long): String {
    val formatter = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    return formatter.format(Date(timestamp))
}