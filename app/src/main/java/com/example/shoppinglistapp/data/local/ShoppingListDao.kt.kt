package com.example.shoppinglistapp.data.local

import androidx.room.*
import com.example.shoppinglistapp.data.local.entities.ShoppingList
import kotlinx.coroutines.flow.Flow

@Dao
interface `ShoppingListDao.kt` {

    @Query("SELECT * FROM shopping_lists ORDER BY lastModified DESC")
    fun getAllShoppingLists(): Flow<List<ShoppingList>>

    @Query("SELECT * FROM shopping_lists WHERE id = :id")
    suspend fun getShoppingListById(id: String): ShoppingList?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShoppingList(shoppingList: ShoppingList)

    @Update
    suspend fun updateShoppingList(shoppingList: ShoppingList)

    @Delete
    suspend fun deleteShoppingList(shoppingList: ShoppingList)

    @Query("DELETE FROM shopping_lists WHERE id = :id")
    suspend fun deleteShoppingListById(id: String)
}