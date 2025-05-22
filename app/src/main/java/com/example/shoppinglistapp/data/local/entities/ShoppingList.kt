package com.example.shoppinglistapp.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shopping_lists")
data class ShoppingList(
    @PrimaryKey
    val id: String,
    val name: String,
    val createdAt: Long = System.currentTimeMillis(),
    val isShared: Boolean = false,
    val ownerId: String = "",
    val lastModified: Long = System.currentTimeMillis()
)