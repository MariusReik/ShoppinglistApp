package com.example.shoppinglistapp.data.local

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.example.shoppinglistapp.data.local.entities.ShoppingItem
import com.example.shoppinglistapp.data.local.entities.ShoppingList

@Database(
    entities = [ShoppingList::class, ShoppingItem::class],
    version = 1,
    exportSchema = false
)
abstract class `ShoppingDatabase.kt` : RoomDatabase() {

    abstract fun shoppingListDao(): ShoppingListDao
    abstract fun shoppingItemDao(): ShoppingItemDao

    companion object {
        @Volatile
        private var INSTANCE: `ShoppingDatabase.kt`? = null

        fun getDatabase(context: Context): `ShoppingDatabase.kt` {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    `ShoppingDatabase.kt`::class.java,
                    "shopping_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}