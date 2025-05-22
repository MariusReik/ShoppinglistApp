package com.example.shoppinglistapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete  // ADD THIS IMPORT
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.shoppinglistapp.ui.theme.*

@Composable
fun GradientCard(
    modifier: Modifier = Modifier,
    elevation: Dp = 4.dp,
    cornerRadius: Dp = 16.dp,
    gradientColors: List<Color> = listOf(
        Color.White,
        LightCyan.copy(alpha = 0.3f)
    ),
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .shadow(
                elevation = elevation,
                shape = RoundedCornerShape(cornerRadius),
                ambientColor = SoftBlue40.copy(alpha = 0.1f),
                spotColor = SoftBlue40.copy(alpha = 0.1f)
            )
            .clip(RoundedCornerShape(cornerRadius))
            .background(
                brush = Brush.verticalGradient(gradientColors)
            )
            .padding(16.dp)
    ) {
        content()
    }
}

@Composable
fun StyledFloatingActionButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    FloatingActionButton(
        onClick = onClick,
        modifier = modifier
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(16.dp),
                ambientColor = SoftBlue40.copy(alpha = 0.2f),
                spotColor = SoftBlue40.copy(alpha = 0.2f)
            ),
        containerColor = SoftBlue40,
        contentColor = Color.White,
        content = content
    )
}

@OptIn(ExperimentalMaterial3Api::class)  // ADD THIS ANNOTATION
@Composable
fun GradientTopAppBar(
    title: String,
    navigationIcon: @Composable (() -> Unit)? = null
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(SoftBlue40, SoftTeal40)
                )
            )
    ) {
        TopAppBar(
            title = {
                Text(
                    text = title,
                    color = Color.White
                )
            },
            navigationIcon = navigationIcon ?: {},
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent
            )
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)  // ADD THIS ANNOTATION
@Composable
fun ShoppingListCard(
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    GradientCard(
        modifier = modifier.fillMaxWidth(),
        gradientColors = listOf(
            Color.White,
            SoftBlue80.copy(alpha = 0.2f),
            SoftTeal80.copy(alpha = 0.1f)
        )
    ) {
        Card(
            onClick = onClick,
            colors = CardDefaults.cardColors(
                containerColor = Color.Transparent
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        color = SoftBlue40
                    )
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = SoftTeal40
                    )
                }

                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Default.Delete,  // FIXED THIS LINE
                        contentDescription = "Delete",
                        tint = DeleteColor
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)  // ADD THIS ANNOTATION
@Composable
fun ShoppingItemCard(
    itemName: String,
    quantity: Int,
    isCompleted: Boolean,
    onToggleComplete: () -> Unit,
    onQuantityChange: (Int) -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val cardColors = if (isCompleted) {
        listOf(
            CompletedItemColor.copy(alpha = 0.1f),
            CompletedItemColor.copy(alpha = 0.05f),
            Color.White
        )
    } else {
        listOf(
            Color.White,
            PendingItemColor.copy(alpha = 0.08f),
            SoftBlue80.copy(alpha = 0.1f)
        )
    }

    GradientCard(
        modifier = modifier.fillMaxWidth(),
        gradientColors = cardColors
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Checkbox
            Checkbox(
                checked = isCompleted,
                onCheckedChange = { onToggleComplete() },
                colors = CheckboxDefaults.colors(
                    checkedColor = CompletedItemColor,
                    uncheckedColor = SoftBlue40
                )
            )

            // Item name
            Text(
                text = itemName,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 12.dp),
                color = if (isCompleted) CompletedItemColor else SoftBlue40
            )

            // Quantity controls
            Row {
                IconButton(
                    onClick = { if (quantity > 1) onQuantityChange(quantity - 1) }
                ) {
                    Text("-", color = SoftTeal40)
                }

                Text(
                    text = quantity.toString(),
                    modifier = Modifier.padding(horizontal = 8.dp),
                    color = SoftBlue40
                )

                IconButton(
                    onClick = { onQuantityChange(quantity + 1) }
                ) {
                    Text("+", color = SoftTeal40)
                }
            }

            // Delete button
            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,  // FIXED THIS LINE
                    contentDescription = "Delete",
                    tint = DeleteColor
                )
            }
        }
    }
}