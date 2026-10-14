package com.example.protek.ui.navigation

import com.example.protek.utils.DetectionCategory

sealed class Screen(val route: String) {
    object Home: Screen("home")
    object Detection: Screen("detection/{detectionType}") {
        fun createRoute(detectionType: DetectionCategory) = "detection/${detectionType.name}"
    }
}