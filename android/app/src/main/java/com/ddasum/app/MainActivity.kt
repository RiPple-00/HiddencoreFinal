package com.ddasum.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ddasum.app.ui.auth.AuthViewModel
import com.ddasum.app.ui.auth.LoginScreen
import com.ddasum.app.ui.navigation.BottomNavBar
import com.ddasum.app.ui.navigation.DdasumNavGraph
import com.ddasum.app.ui.theme.DdasumTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DdasumTheme {
                DdasumApp()
            }
        }
    }
}

private const val ROUTE_LOGIN = "login"
private const val ROUTE_MAIN = "main"

@Composable
fun DdasumApp(authViewModel: AuthViewModel = hiltViewModel()) {
    val isLoggedIn by authViewModel.isLoggedIn.collectAsState()

    when (isLoggedIn) {
        null -> Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        else -> {
            val rootNavController = rememberNavController()
            NavHost(
                navController = rootNavController,
                startDestination = if (isLoggedIn == true) ROUTE_MAIN else ROUTE_LOGIN
            ) {
                composable(ROUTE_LOGIN) {
                    LoginScreen(
                        onLoginSuccess = {
                            rootNavController.navigate(ROUTE_MAIN) {
                                popUpTo(ROUTE_LOGIN) { inclusive = true }
                            }
                        }
                    )
                }
                composable(ROUTE_MAIN) { MainScreen() }
            }
        }
    }
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = { BottomNavBar(navController) }
    ) { innerPadding ->
        DdasumNavGraph(
            navController = navController,
            modifier = Modifier.padding(innerPadding)
        )
    }
}
