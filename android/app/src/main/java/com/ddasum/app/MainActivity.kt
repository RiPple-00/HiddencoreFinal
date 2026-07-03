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

// MainActivity는 앱의 진입점으로, 앱이 시작될 때 가장 먼저 실행되는 Activity.
// Hilt를 사용하여 의존성 주입하려면 @AndroidEntryPoint 어노테이션이 필요.
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

// AuthViewModel을 주입받아 로그인 상태를 확인하고, 이에 따라 로그인 화면 또는 메인 화면을 표시.
// isLoggedIn by authViewModel.isLoggedIn.collectAsState() 상태가 null이면 로딩 화면을 표시하고, true이면 메인 화면, false이면 로그인 화면으로 이동.
// NavHost를 사용하여 화면 전환을 관리하며, 로그인 성공 시 메인 화면으로 이동하고, 이전 화면을 스택에서 제거.
@Composable
fun DdasumApp(authViewModel: AuthViewModel = hiltViewModel()) {
    // isLoggedIn 원본은 StateFlow<Boolean?> — Compose 스냅샷 시스템이 모르는 타입이라 그대로 못 읽는다.
    // collectAsState(): Flow를 구독해 Compose가 추적 가능한 State<Boolean?>로 변환.
    // by(프로퍼티 위임): State<Boolean?>.getValue()를 자동 호출해서 매번 .value 안 붙이고 Boolean?로 바로 씀.
    val isLoggedIn by authViewModel.isLoggedIn.collectAsState()

    // null은 "로그인 안 됨"이 아니라 "DataStore 응답이 아직 안 와서 모름" (AuthViewModel의 stateIn 초기값).
    // 진짜 로그인 여부(true/false)는 Repository 레벨(AuthRepositoryImpl.isLoggedIn)에서만 확정된다.
    when (isLoggedIn) {
        null -> Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        else -> {
            val rootNavController = rememberNavController()
            // startDestination은 이 NavHost가 "처음 컴포지션될 때 딱 1번"만 평가됨.
            // 즉 이후 isLoggedIn이 true로 바뀌어도 NavHost가 알아서 화면을 바꿔주지 않음
            // → 로그인 성공 시엔 아래 onLoginSuccess에서 navigate()를 직접 호출해야 하는 이유.
            NavHost(
                navController = rootNavController,
                startDestination = if (isLoggedIn == true) ROUTE_MAIN else ROUTE_LOGIN
            ) {
                composable(ROUTE_LOGIN) {
                    LoginScreen(
                        onLoginSuccess = {
                            // 백스택은 List/Stack 구조. 이 시점 백스택 = [ROUTE_LOGIN] 하나뿐.
                            // popUpTo(ROUTE_LOGIN){inclusive=true}: ROUTE_LOGIN까지(자신 포함) pop → [ ]
                            // 그다음 navigate(ROUTE_MAIN)이 push → 최종 백스택 [ROUTE_MAIN]
                            // inclusive=true 없으면 [ROUTE_LOGIN, ROUTE_MAIN]으로 남아서,
                            // 메인 화면에서 뒤로가기 누르면 로그인 화면으로 되돌아가버림 — 그래서 반드시 필요.
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

// navController 자체는 List가 아니라, 그래프(NavGraph=정적 화면 설계도) + 백스택(List=실제 이동 기록)
// + 조작 함수(navigate/popBackStack=그래프를 참고해 백스택을 바꾸는 다리 역할)를 합쳐 관리하는 컨트롤러.
// 아래 MainScreen()의 navController는 위 rootNavController와는 완전히 별개의 독립된 백스택을 가진다
// (바텀탭 5개 전환은 이 안쪽 백스택 얘기고, 바깥쪽 [ROUTE_MAIN] 엔트리엔 영향 없음).

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
