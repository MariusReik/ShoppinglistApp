package com.example.shoppinglistapp.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "shopping_items",
    foreignKeys = [
        ForeignKey(
            entity = ShoppingList::class,
            parentColumns = ["id"],
            childColumns = ["listId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ShoppingItem(
    @PrimaryKey
    val id: String,
    val listId: String,
    val name: String,
    val quantity: Int = 1,
    val isCompleted: Boolean = false,
    val addedBy: String = "",
    val addedAt: Long = System.currentTimeMillis(),
    val order: Int = 0
)