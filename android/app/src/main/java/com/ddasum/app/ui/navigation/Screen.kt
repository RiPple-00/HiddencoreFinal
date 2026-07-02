package com.ddasum.app.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
    data object Home : Screen("home", "홈", Icons.Filled.Home)
    data object Payment : Screen("payment", "수납", Icons.Filled.Payment)
    data object Report : Screen("report", "리포트", Icons.Filled.Description)
    data object Chatbot : Screen("chatbot", "챗봇", Icons.AutoMirrored.Filled.Chat)
    data object QrCode : Screen("qrcode", "QR코드", Icons.Filled.QrCode)

    companion object {
        val bottomNavItems = listOf(Home, Payment, Report, Chatbot, QrCode)
    }
}
