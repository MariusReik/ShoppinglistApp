package com.example.shoppinglistapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppinglistapp.data.local.ShoppingDatabase
import com.example.shoppinglistapp.data.local.entities.ShoppingList
import com.example.shoppinglistapp.data.repository.ShoppingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ShoppingListsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ShoppingRepository(ShoppingDatabase.getDatabase(application))

    val shoppingLists = repository.getAllShoppingLists()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun createShoppingList(name: String) {
        if (name.isBlank()) return

        viewModelScope.launch {
            _isLoading.value = true
            try {
                repository.insertShoppingList(name.trim())
            } catch (e: Exception) {
                // Handle error - in a real app, you'd want proper error handling
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteShoppingList(shoppingList: ShoppingList) {
        viewModelScope.launch {
            try {
                repository.deleteShoppingList(shoppingList)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun updateShoppingList(shoppingList: ShoppingList) {
        viewModelScope.launch {
            try {
                repository.updateShoppingList(shoppingList)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}