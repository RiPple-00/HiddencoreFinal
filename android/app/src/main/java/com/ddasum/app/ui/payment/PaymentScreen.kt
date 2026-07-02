package com.ddasum.app.ui.payment

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun PaymentScreen() {
    Text(text = "수납", modifier = Modifier.fillMaxSize().wrapContentSize(Alignment.Center))
}
