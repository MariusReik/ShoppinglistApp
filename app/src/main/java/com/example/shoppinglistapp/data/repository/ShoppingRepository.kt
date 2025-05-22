package com.example.shoppinglistapp.data.repository

import com.example.shoppinglistapp.data.local.ShoppingDatabase
import com.example.shoppinglistapp.data.local.entities.ShoppingItem
import com.example.shoppinglistapp.data.local.entities.ShoppingList
import kotlinx.coroutines.flow.Flow
import java.util.UUID

class ShoppingRepository(private val database: ShoppingDatabase) {

    private val listDao = database.shoppingListDao()
    private val itemDao = database.shoppingItemDao()

    // Shopping List operations
    fun getAllShoppingLists(): Flow<List<ShoppingList>> = listDao.getAllShoppingLists()

    suspend fun getShoppingListById(id: String): ShoppingList? = listDao.getShoppingListById(id)

    suspend fun insertShoppingList(name: String): String {
        val id = UUID.randomUUID().toString()
        val shoppingList = ShoppingList(
            id = id,
            name = name
        )
        listDao.insertShoppingList(shoppingList)
        return id
    }

    suspend fun updateShoppingList(shoppingList: ShoppingList) {
        listDao.updateShoppingList(shoppingList.copy(lastModified = System.currentTimeMillis()))
    }

    suspend fun deleteShoppingList(shoppingList: ShoppingList) {
        listDao.deleteShoppingList(shoppingList)
    }

    // Shopping Item operations
    fun getItemsForList(listId: String): Flow<List<ShoppingItem>> = itemDao.getItemsForList(listId)

    suspend fun insertShoppingItem(listId: String, name: String, quantity: Int = 1) {
        val id = UUID.randomUUID().toString()

        val item = ShoppingItem(
            id = id,
            listId = listId,
            name = name,
            quantity = quantity,
            order = 0 // We'll handle ordering later when we add drag-and-drop
        )
        itemDao.insertItem(item)

        // Update the shopping list's last modified time
        getShoppingListById(listId)?.let { list ->
            updateShoppingList(list)
        }
    }

    suspend fun updateShoppingItem(item: ShoppingItem) {
        itemDao.updateItem(item)

        // Update the shopping list's last modified time
        getShoppingListById(item.listId)?.let { list ->
            updateShoppingList(list)
        }
    }

    suspend fun deleteShoppingItem(item: ShoppingItem) {
        itemDao.deleteItem(item)

        // Update the shopping list's last modified time
        getShoppingListById(item.listId)?.let { list ->
            updateShoppingList(list)
        }
    }

    suspend fun toggleItemCompletion(itemId: String, isCompleted: Boolean) {
        itemDao.updateItemCompletionStatus(itemId, isCompleted)
    }

    suspend fun updateItemQuantity(itemId: String, quantity: Int) {
        itemDao.updateItemQuantity(itemId, quantity)
    }
}