package com.example.protek.ui.navigation

sealed class Screen(val route: String) {
    object Home: Screen("home")
}