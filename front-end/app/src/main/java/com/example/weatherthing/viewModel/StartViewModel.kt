package com.example.weatherthing.viewModel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherthing.data.User
import com.example.weatherthing.network.Load
import com.example.weatherthing.repository.DBRepository
import com.example.weatherthing.utils.App
import com.example.weatherthing.utils.AppPref
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow

sealed class LoginState {
    object Loading : LoginState()
    object CompleteLogin : LoginState()
    object RequireLogin : LoginState()
    object RequireRegister : LoginState()
}

// sealed class SplashEvent {
//    object DisconnectedNetwork : SplashEvent()
//    object Banned : SplashEvent()
//    object Shutdown : SplashEvent()
//    object Success : SplashEvent()
// }

class StartViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val dbRepository = DBRepository
    val loginState = MutableStateFlow<LoginState>(LoginState.Loading)

    private val pref = AppPref(App.context)
    var regionCode = 0

    init {
        viewModelScope.launch {
            kotlin.runCatching {
                val list = listOf(
                    async {
                        delay(2500)
                        LoginState.Loading
                    },
                    async {
                        if (getLastSignedInAccount(App.context)) {
                            checkAfterLogin()
                        } else {
                            LoginState.RequireLogin
                        }
                    }
                ).awaitAll()
                loginState.value = list[1]
            }
        }
    }

    fun sign(nickname: String, age: Int, gender: Int, weather: Int, imgUri: Uri) {
        auth.currentUser?.let {
            val user = User(
                id = null,
                uid = it.uid,
                email = it.email ?: "",
                nickname = nickname,
                genderCode = gender,
                age = age,
                weatherCode = weather,
                regionCode = regionCode
            )
            viewModelScope.launch {
                val result = dbRepository.joinUser(user)
                if (result is Load.Success<*>) {
                    pref.setUserPref(user)
                    loginState.emit(LoginState.CompleteLogin)
                }
            }
        }
    }

    fun login() {
        viewModelScope.launch {
            loginState.value = checkAfterLogin()
        }
    }

    private suspend fun checkAfterLogin(): LoginState { // 구글 로그인 완료 후
        // sharedPreference에 저장된 데이터가 있으면 가입된 유저라고 간주
        val prefUser = pref.getUserPref()
        return if (prefUser != null) {
            LoginState.CompleteLogin
        } else {
            auth.currentUser?.let {
                val result = dbRepository.getProfile(uId = it.uid)
                if (result is Load.Success<*>) {
                    pref.setUserPref(result.data as User)
                    LoginState.CompleteLogin
                } else {
                    LoginState.RequireRegister
                }
            } ?: LoginState.RequireLogin
        }
    }

    fun checkNickname(nickname: String): Boolean {
        // 닉네임 중복 아니면 true
        return true
    }

    // 이전에 로그인 한 계정이 있는지 확인
    private fun getLastSignedInAccount(context: Context) =
        GoogleSignIn.getLastSignedInAccount(context) != null
}
