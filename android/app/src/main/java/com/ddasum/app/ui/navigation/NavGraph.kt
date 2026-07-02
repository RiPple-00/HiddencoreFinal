package com.ddasum.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ddasum.app.ui.chatbot.ChatbotScreen
import com.ddasum.app.ui.home.HomeScreen
import com.ddasum.app.ui.payment.PaymentScreen
import com.ddasum.app.ui.qrcode.QrCodeScreen
import com.ddasum.app.ui.report.ReportScreen

@Composable
fun DdasumNavGraph(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onPaymentClick = { navController.navigate(Screen.Payment.route) },
                onReportClick = { navController.navigate(Screen.Report.route) },
                onChatbotClick = { navController.navigate(Screen.Chatbot.route) },
                onQrCodeClick = { navController.navigate(Screen.QrCode.route) }
            )
        }
        composable(Screen.Payment.route) { PaymentScreen() }
        composable(Screen.Report.route) { ReportScreen() }
        composable(Screen.Chatbot.route) { ChatbotScreen() }
        composable(Screen.QrCode.route) { QrCodeScreen() }
    }
}
