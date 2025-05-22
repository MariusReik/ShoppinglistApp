package com.example.shoppinglistapp.data.local

import androidx.room.*
import com.example.shoppinglistapp.data.local.entities.ShoppingItem
import kotlinx.coroutines.flow.Flow

@Dao
interface ShoppingItemDao {

    @Query("SELECT * FROM shopping_items WHERE listId = :listId ORDER BY `order` ASC")
    fun getItemsForList(listId: String): Flow<List<ShoppingItem>>

    @Query("SELECT * FROM shopping_items WHERE id = :id")
    suspend fun getItemById(id: String): ShoppingItem?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: ShoppingItem)

    @Update
    suspend fun updateItem(item: ShoppingItem)

    @Delete
    suspend fun deleteItem(item: ShoppingItem)

    @Query("DELETE FROM shopping_items WHERE id = :id")
    suspend fun deleteItemById(id: String)

    @Query("UPDATE shopping_items SET isCompleted = :isCompleted WHERE id = :id")
    suspend fun updateItemCompletionStatus(id: String, isCompleted: Boolean)

    @Query("UPDATE shopping_items SET quantity = :quantity WHERE id = :id")
    suspend fun updateItemQuantity(id: String, quantity: Int)

    @Query("UPDATE shopping_items SET `order` = :newOrder WHERE id = :id")
    suspend fun updateItemOrder(id: String, newOrder: Int)
}