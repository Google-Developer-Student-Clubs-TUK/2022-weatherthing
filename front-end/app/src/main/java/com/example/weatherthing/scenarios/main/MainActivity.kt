package com.example.weatherthing.scenarios.main

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.core.app.ActivityCompat
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.weatherthing.scenarios.Home
import com.example.weatherthing.scenarios.main.Post.CommentScreen
import com.example.weatherthing.scenarios.main.Post.PostScreen
import com.example.weatherthing.scenarios.main.chat.ChatListScreen
import com.example.weatherthing.viewModel.MainViewModel
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import com.google.android.gms.tasks.Task

sealed class BottomNavItem(val title: String, val icon: ImageVector, val route: String) {
    object BoardScreen : BottomNavItem("게시판", Icons.Filled.Home, "Post")
    object UserScreen : BottomNavItem("매칭 유저", Icons.Filled.Person, "SEARCH")
    object ChatListScreen : BottomNavItem("채팅목록", Icons.Filled.MailOutline, "SHORTVIDEO")
    object MyPageScreen : BottomNavItem("마이페이지", Icons.Filled.Person, "MYPAGE")
}

enum class NavItem(val routeName: String, val description: String) {
    POST("POST", "게시물 작성 페이지"),
    COMMENT("COMMENT", "댓글 작성 페이지")
}

class MainActivity : ComponentActivity(), LocationListener {
    lateinit var fusedLocationClient: FusedLocationProviderClient
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        permissionCheck()
        getCurrentLocation()

        setContent {
            val navController = rememberNavController()

            Scaffold(bottomBar = {
                BottomNavigationBar(
                    items = listOf(
                        BottomNavItem.BoardScreen,
                        BottomNavItem.UserScreen,
                        BottomNavItem.ChatListScreen,
                        BottomNavItem.MyPageScreen
                    ), navController = navController
                )
            }) {
                Screen(
                    mainViewModel = viewModel,
                    startRoute = BottomNavItem.BoardScreen.route,
                    navController = navController
                )
            }
        }
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

        when (requestCode) {
            1 -> {
                if (fLocationPermission == PackageManager.PERMISSION_GRANTED && cLocationPermission == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "위치 권한 접근이 허용되었습니다.", Toast.LENGTH_SHORT).show()
                    getCurrentLocation()
                } else Toast.makeText(
                    this,
                    "앱을 실행하기 위해서 위치 권한이 필요합니다. 위치 권한을 설정해주세요.",
                    Toast.LENGTH_SHORT
                ).show()
                return
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun getCurrentLocation() {
        val locationManager =
            applicationContext.getSystemService(LOCATION_SERVICE) as LocationManager
        val gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

        if (!gps) {
            showLocationPrompt()
        }

        fusedLocationClient.getCurrentLocation(
            LocationRequest.PRIORITY_HIGH_ACCURACY,
            object : CancellationToken() {
                override fun onCanceledRequested(p0: OnTokenCanceledListener) =
                    CancellationTokenSource().token

                override fun isCancellationRequested() = false
            }
        ).addOnSuccessListener { location: Location? ->
            if (location == null) Toast.makeText(this, "위치를 가져올 수 없습니다.", Toast.LENGTH_SHORT).show()
            else {
                location.let {
                    Log.d("showing", "나옵니다용")
                    viewModel.getCurrentWeather(it.latitude, it.longitude)
                }
            }
        }
    }

    private fun showLocationPrompt() {
        val locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)

        val result: Task<LocationSettingsResponse> =
            LocationServices.getSettingsClient(this).checkLocationSettings(builder.build())

        result.addOnCompleteListener { task ->
            try {
                val response = task.getResult(ApiException::class.java)
            } catch (exception: ApiException) {
                when (exception.statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                        try {
                            val resolvable: ResolvableApiException =
                                exception as ResolvableApiException
                            resolvable.startResolutionForResult(
                                this,
                                LocationRequest.PRIORITY_HIGH_ACCURACY
                            )
                        } catch (e: IntentSender.SendIntentException) {
                        } catch (e: ClassCastException) {
                        }
                    }
                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                        showLocationPrompt()
                    }
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            LocationRequest.PRIORITY_HIGH_ACCURACY -> {
                if (resultCode == Activity.RESULT_OK) {
                    fusedLocationClient.getCurrentLocation(
                        LocationRequest.PRIORITY_HIGH_ACCURACY,
                        object : CancellationToken() {
                            override fun onCanceledRequested(p0: OnTokenCanceledListener) =
                                CancellationTokenSource().token

                            override fun isCancellationRequested() = false
                        }
                    )
                        .addOnSuccessListener { location: Location? ->
                            if (location == null) {
                                Toast.makeText(this, "위치를 가져올 수 없습니다.", Toast.LENGTH_SHORT).show()
                            } else {
                                location.let {
                                    viewModel.getCurrentWeather(it.latitude, it.longitude)
                                }
                            }
                        }
                }
            }
        }
    }

    private fun permissionCheck() {
        val fLocationPermission =
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        val cLocationPermission =
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)

        if (fLocationPermission != PackageManager.PERMISSION_GRANTED && cLocationPermission != PackageManager.PERMISSION_GRANTED
        ) {
            val permissions = arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            ActivityCompat.requestPermissions(this, permissions, 1)
        } else {
            getCurrentLocation()
        }
    }

    override fun onLocationChanged(p0: Location) {
        viewModel.getCurrentWeather(p0.latitude, p0.longitude)
    }
}

@Composable
fun Screen(
    mainViewModel: MainViewModel,
    startRoute: String,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    // NavHost 로 네비게이션 결정
    NavHost(navController, startRoute) {
        composable("${BottomNavItem.BoardScreen.route}/{weatherCode",
            arguments = listOf(
                navArgument("weatherCode") { type = NavType.IntType }
            )) {
            Home(
                viewModel = mainViewModel,
                navController = navController,
                weatherCode = it.arguments?.getInt("weatherCode") ?: 0
            )
        }
        composable(
            "${BottomNavItem.MyPageScreen.route}/{userId}",
            arguments = listOf(
                navArgument("userId") { type = NavType.StringType }
            )
        ) {
        }
        composable(BottomNavItem.ChatListScreen.route) {
            ChatListScreen(navController = navController)
        }
        composable(BottomNavItem.UserScreen.route) {
        }
        composable(
            NavItem.POST.routeName
        ) {
            PostScreen(navController = navController)
        }
        composable(
            NavItem.COMMENT.routeName
        ) {
            CommentScreen(navController = navController)
        }
    }
}

@Composable
fun BottomNavigationBar(
    items: List<BottomNavItem>,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val backStackEntry = navController.currentBackStackEntryAsState()
    BottomNavigation(backgroundColor = Color.White, modifier = modifier) {
        items.forEach { item ->
            val selected = item.route == backStackEntry.value?.destination?.route
            BottomNavigationItem(
                selected = selected,
                onClick = { navController.navigate(item.route) },
                icon = {
                    Icon(imageVector = item.icon, contentDescription = item.title)
                }
            )
        }
    }
}
