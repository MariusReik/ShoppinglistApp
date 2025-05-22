package com.example.shoppinglistapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppinglistapp.data.local.ShoppingDatabase
import com.example.shoppinglistapp.data.local.entities.ShoppingItem
import com.example.shoppinglistapp.data.local.entities.ShoppingList
import com.example.shoppinglistapp.data.repository.ShoppingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ShoppingListDetailViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ShoppingRepository(ShoppingDatabase.getDatabase(application))

    private val _currentList = MutableStateFlow<ShoppingList?>(null)
    val currentList: StateFlow<ShoppingList?> = _currentList.asStateFlow()

    private val _items = MutableStateFlow<List<ShoppingItem>>(emptyList())
    val items: StateFlow<List<ShoppingItem>> = _items.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun loadList(listId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // Load the shopping list
                _currentList.value = repository.getShoppingListById(listId)

                // Load items for this list
                repository.getItemsForList(listId).collect { itemsList ->
                    _items.value = itemsList
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun addItem(listId: String, name: String, quantity: Int = 1) {
        if (name.isBlank()) return

        viewModelScope.launch {
            try {
                repository.insertShoppingItem(listId, name.trim(), quantity)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun toggleItemCompletion(item: ShoppingItem) {
        viewModelScope.launch {
            try {
                repository.toggleItemCompletion(item.id, !item.isCompleted)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun updateItemQuantity(item: ShoppingItem, newQuantity: Int) {
        if (newQuantity <= 0) return

        viewModelScope.launch {
            try {
                repository.updateItemQuantity(item.id, newQuantity)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun deleteItem(item: ShoppingItem) {
        viewModelScope.launch {
            try {
                repository.deleteShoppingItem(item)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun updateItem(item: ShoppingItem) {
        viewModelScope.launch {
            try {
                repository.updateShoppingItem(item)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}