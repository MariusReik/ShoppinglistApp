package com.example.shoppinglistapp.data.local

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import android.content.Context
import com.example.shoppinglistapp.data.local.entities.ShoppingItem
import com.example.shoppinglistapp.data.local.entities.ShoppingList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [ShoppingList::class, ShoppingItem::class],
    version = 1,
    exportSchema = false
)
abstract class ShoppingDatabase : RoomDatabase() {

    abstract fun shoppingListDao(): ShoppingListDao
    abstract fun shoppingItemDao(): ShoppingItemDao

    companion object {
        @Volatile
        private var INSTANCE: ShoppingDatabase? = null

        fun getDatabase(context: Context): ShoppingDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ShoppingDatabase::class.java,
                    "shopping_database"
                )
                    .addCallback(DatabaseCallback()) // Add sample data on first creation
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    // Callback to add sample data when database is created
    private class DatabaseCallback : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                CoroutineScope(Dispatchers.IO).launch {
                    populateDatabase(database.shoppingListDao(), database.shoppingItemDao())
                }
            }
        }
    }
}

// Function to add sample data
suspend fun populateDatabase(listDao: ShoppingListDao, itemDao: ShoppingItemDao) {
    // Sample Shopping Lists
    val groceryListId = "grocery-list-1"
    val partyListId = "party-list-1"
    val hardwareListId = "hardware-list-1"

    val groceryList = ShoppingList(
        id = groceryListId,
        name = "Weekly Groceries",
        createdAt = System.currentTimeMillis() - 86400000 // 1 day ago
    )

    val partyList = ShoppingList(
        id = partyListId,
        name = "Birthday Party Supplies",
        createdAt = System.currentTimeMillis() - 172800000 // 2 days ago
    )

    val hardwareList = ShoppingList(
        id = hardwareListId,
        name = "Home Improvement",
        createdAt = System.currentTimeMillis()
    )

    // Insert lists
    listDao.insertShoppingList(groceryList)
    listDao.insertShoppingList(partyList)
    listDao.insertShoppingList(hardwareList)

    // Sample items for Grocery List
    val groceryItems = listOf(
        ShoppingItem("item-1", groceryListId, "Milk", 2, false, "", System.currentTimeMillis(), 1),
        ShoppingItem("item-2", groceryListId, "Bread", 1, true, "", System.currentTimeMillis(), 2),
        ShoppingItem("item-3", groceryListId, "Eggs", 12, false, "", System.currentTimeMillis(), 3),
        ShoppingItem("item-4", groceryListId, "Apples", 6, false, "", System.currentTimeMillis(), 4),
        ShoppingItem("item-5", groceryListId, "Chicken Breast", 2, true, "", System.currentTimeMillis(), 5),
        ShoppingItem("item-6", groceryListId, "Rice", 1, false, "", System.currentTimeMillis(), 6)
    )

    // Sample items for Party List
    val partyItems = listOf(
        ShoppingItem("item-7", partyListId, "Balloons", 20, false, "", System.currentTimeMillis(), 1),
        ShoppingItem("item-8", partyListId, "Birthday Cake", 1, false, "", System.currentTimeMillis(), 2),
        ShoppingItem("item-9", partyListId, "Paper Plates", 2, true, "", System.currentTimeMillis(), 3),
        ShoppingItem("item-10", partyListId, "Sodas", 6, false, "", System.currentTimeMillis(), 4),
        ShoppingItem("item-11", partyListId, "Napkins", 3, false, "", System.currentTimeMillis(), 5)
    )

    // Sample items for Hardware List
    val hardwareItems = listOf(
        ShoppingItem("item-12", hardwareListId, "Screws", 50, false, "", System.currentTimeMillis(), 1),
        ShoppingItem("item-13", hardwareListId, "Paint Brush", 2, false, "", System.currentTimeMillis(), 2),
        ShoppingItem("item-14", hardwareListId, "Sandpaper", 5, true, "", System.currentTimeMillis(), 3)
    )

    // Insert all items
    groceryItems.forEach { itemDao.insertItem(it) }
    partyItems.forEach { itemDao.insertItem(it) }
    hardwareItems.forEach { itemDao.insertItem(it) }
}