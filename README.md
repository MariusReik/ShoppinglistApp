# 🛒 Shopping List App

A simple, modern shopping list application built with **Kotlin** and **Jetpack Compose**. Create multiple shopping lists, manage items with quantities, and a gradient-based UI design.

## ✨ Features

- 🏠 **Multiple Shopping Lists** - Create and manage different lists (groceries, party supplies, etc.)
- 📝 **Smart Item Management** - Add items with custom quantities
- ✅ **Progress Tracking** - Check off items as you shop
- 🎨 **Beautiful UI** - Modern Material 3 design with gradients
- 📱 **Responsive Design** - Works perfectly on all screen sizes
- 💾 **Local Storage** - All data saved locally with Room database
- 🌙 **Modern Architecture** - MVVM pattern with Jetpack Compose

## 📱 Screenshots

### Home Screen
*gradient background with all your shopping lists*

![Home Screen](screenshots/home_screen.jpg)

### Shopping List Detail
*Manage items with intuitive quantity controls*

![List Detail](screenshots/list_detail.jpg)

### Add New Item
*Clean, user-friendly dialog for adding items*

![Add Item](screenshots/add_item.jpg)

## 🛠 Tech Stack

- **Language:** Kotlin
- **UI Framework:** Jetpack Compose
- **Architecture:** MVVM (Model-View-ViewModel)
- **Database:** Room (SQLite)
- **Navigation:** Navigation Compose
- **Design:** Material 3 with custom theming

## 🏗 Project Structure

```
app/src/main/java/com/example/shoppinglistapp/
├── data/
│   ├── local/
│   │   ├── entities/          # Room entities
│   │   ├── ShoppingDatabase   # Room database
│   │   ├── ShoppingListDao    # Data access objects
│   │   └── ShoppingItemDao
│   └── repository/
│       └── ShoppingRepository # Data repository
├── ui/
│   ├── components/            # Reusable UI components
│   ├── screens/              # App screens
│   └── theme/                # Material 3 theming
├── viewmodel/                # ViewModels
├── MainActivity              # Main activity
└── Navigation               # Navigation setup
```

## 🚀 Getting Started

### Prerequisites

- Android Studio Hedgehog | 2023.1.1 or newer
- Android SDK API 24 or higher
- Kotlin 2.0.0

## 📋 How to Use

### Creating Your First Shopping List

1. **Launch the app** - You'll see the beautiful home screen
2. **Tap the + button** - Create your first shopping list
3. **Enter a name** - Like "Weekly Groceries" or "Party Supplies"
4. **Start adding items** - Tap your list to open it

### Managing Items

- **Add items:** Tap the + button in any list
- **Set quantities:** Use the +/- buttons when adding items
- **Mark complete:** Check the box when you've got the item
- **Delete items:** Tap the delete icon (🗑️)
- **Adjust quantities:** Use +/- buttons on existing items


