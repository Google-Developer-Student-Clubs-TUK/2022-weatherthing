package com.example.weatherthing.scenarios.intro

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.weatherthing.R
import com.example.weatherthing.scenarios.main.MainActivity
import com.example.weatherthing.utils.App
import com.example.weatherthing.utils.getRegionCode
import com.example.weatherthing.viewModel.LoginState
import com.example.weatherthing.viewModel.StartViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.location.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class StartActivity : ComponentActivity() {
    private val viewModel by viewModels<StartViewModel>()
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val RC_SIGN_IN = 1313

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launchWhenCreated {
            viewModel.loginState.collect {
                when (it) {
                    LoginState.Loading -> {
                        setContent {
                            SplashScreen()
                        }
                    }
                    LoginState.RequireLogin -> {
                        setContent {
                            Box() {
                                SplashScreen()
                                Column() {
                                    Spacer(modifier = Modifier.weight(3f))
                                    SignInGoogleButton { googleLogin() }
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                            }
                        }
                    }
                    LoginState.RequireRegister -> {
                        getCurrentLocation()
                        setContent {
                            val navController = rememberNavController()
                            Screen(
                                startRoute = "Weather",
                                navController = navController,
                                startViewModel = viewModel
                            )
                        }
                    }
                    LoginState.CompleteLogin -> {
                        toMainActivity()
                    }
                }
            }
        }
    }

    private fun toMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
    }

    private fun googleLogin() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            // 빨간줄이지만 토큰 문제라 실행 가능
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        googleSignIn()
    }

    // 구글 회원가입
    private fun googleSignIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account)
            } catch (e: ApiException) {
                Toast.makeText(this, "구글 회원가입에 실패하였습니다: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        } else {
            /*no-op*/
        }
    }

    // account 객체에서 id 토큰 가져온 후 Firebase 인증
    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount?) {
        val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
        val auth = FirebaseAuth.getInstance()
        auth.signInWithCredential(credential).addOnCompleteListener(this) { task ->
            task.addOnFailureListener {
                Toast.makeText(App.context, "로그인에 실패했습니다: $it", Toast.LENGTH_SHORT).show()
            }.addOnSuccessListener {
                viewModel.login()
            }
        }
    }

    private fun getCurrentLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        permissionCheck()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        val fLocationPermission =
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        val cLocationPermission =
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)

        if (requestCode == 200) {
            if (fLocationPermission == PackageManager.PERMISSION_GRANTED && cLocationPermission == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation()
            } else Toast.makeText(
                this,
                "앱을 실행하기 위해서 위치 권한이 필요합니다. 위치 권한을 설정해주세요.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun permissionCheck() {
        val fLocationPermission =
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        val cLocationPermission =
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)

        if (fLocationPermission == PackageManager.PERMISSION_GRANTED && cLocationPermission == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener {
                val code = getRegionCode(it)
                viewModel.regionCode = code
            }
        } else {
            val permissions = arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            ActivityCompat.requestPermissions(this, permissions, 200)
        }
    }
}

sealed class StartScreen(val title: String, val route: String) {
    object SignUp : StartScreen("유저 정보 입력 페이지", "SignUp")
    object WeatherSel : StartScreen("날씨 선택 페이지", "Weather")
}

@Composable
fun Screen(startRoute: String, navController: NavHostController, startViewModel: StartViewModel) {
    // NavHost 로 네비게이션 결정
    NavHost(navController, startRoute) {
        composable(
            "${StartScreen.SignUp.route}/{weatherCode}",
            arguments = listOf(
                navArgument("weatherCode") { type = NavType.IntType }
            )
        ) {
            SignUpScreen(
                viewModel = startViewModel,
                navController = navController,
                weatherCode = it.arguments?.getInt("weatherCode") ?: 0
            )
        }
        composable(StartScreen.WeatherSel.route) {
            WeatherSelectScreen(navController, startViewModel)
        }
    }
}

@Composable
fun ColumnScope.SignInGoogleButton(onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(horizontal = 20.dp)
            .fillMaxWidth()
            .height(55.dp)
            .background(Color.Transparent),
        shape = MaterialTheme.shapes.medium,
        elevation = 5.dp
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier.padding(
                horizontal = 15.dp
            )
        ) {
            Image(
                painter = painterResource(id = R.drawable.icon_google),
                contentDescription = "Google sign button",
                modifier = Modifier.size(35.dp)
            )
            Spacer(modifier = Modifier.width(20.dp))
            Text(
                text = "Sign in with Google",
                style = MaterialTheme.typography.overline,
                color = Color.Gray,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
