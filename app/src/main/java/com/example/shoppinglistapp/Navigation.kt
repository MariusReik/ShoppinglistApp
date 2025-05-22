package com.example.shoppinglistapp

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.shoppinglistapp.ui.screens.ShoppingListDetailScreen
import com.example.shoppinglistapp.ui.screens.ShoppingListsScreen

sealed class Screen(val route: String) {
    object ShoppingLists : Screen("shopping_lists")
    object ShoppingListDetail : Screen("shopping_list_detail/{listId}") {
        fun createRoute(listId: String) = "shopping_list_detail/$listId"
    }
}

@Composable
fun ShoppingListNavigation(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.ShoppingLists.route
    ) {
        composable(Screen.ShoppingLists.route) {
            ShoppingListsScreen(
                onNavigateToDetail = { listId ->
                    navController.navigate(Screen.ShoppingListDetail.createRoute(listId))
                }
            )
        }

        composable(Screen.ShoppingListDetail.route) { backStackEntry ->
            val listId = backStackEntry.arguments?.getString("listId") ?: ""
            ShoppingListDetailScreen(
                listId = listId,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}