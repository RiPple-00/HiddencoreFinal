package com.ddasum.app.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ddasum.app.ui.navigation.Screen

@Composable
fun ActionButtonsSection(
    onPaymentClick: () -> Unit,
    onReportClick: () -> Unit,
    onChatbotClick: () -> Unit,
    onQrCodeClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        ActionButton(screen = Screen.Payment, onClick = onPaymentClick)
        ActionButton(screen = Screen.Report, onClick = onReportClick)
        ActionButton(screen = Screen.Chatbot, onClick = onChatbotClick)
        ActionButton(screen = Screen.QrCode, onClick = onQrCodeClick)
    }
}

@Composable
private fun ActionButton(screen: Screen, onClick: () -> Unit) {
    OutlinedButton(onClick = onClick) {
        Column {
            Icon(imageVector = screen.icon, contentDescription = screen.label)
            Text(text = screen.label)
        }
    }
}
