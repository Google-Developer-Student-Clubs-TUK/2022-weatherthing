package com.example.weatherthing.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherthing.utils.App
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
    val loginState = MutableStateFlow<LoginState>(LoginState.Loading)

    init {
        viewModelScope.launch {
            kotlin.runCatching {
                val list = listOf(
                    async {
                        delay(3000)
                        LoginState.Loading
                    },
                    async {
                        if (getLastSignedInAccount(App.context)) {
                            if ("SHARED_VALUE" == "SHARED_VALUE") {
                                LoginState.CompleteLogin
                            } else {
                                LoginState.RequireRegister
                            }
                        } else {
                            LoginState.RequireLogin
                        }
                    }
                ).awaitAll()
                loginState.value = list[1]
            }
        }
    }

    fun sign(): Boolean {
        loginState.value = LoginState.CompleteLogin
        return true
    }

    fun checkLogin() {
        // sharedPreference에 저장된 데이터가 있으면 가입된 유저라고 간주
        val a = 0
        viewModelScope.launch {
            if (true) {
                loginState.value = LoginState.RequireRegister
            } else {
                loginState.value = LoginState.CompleteLogin
            }
        }
    }

    fun checkNickname(nickname: String): Boolean {
        // 닉네임 중복 아니면 true
        return true
    }

    fun joinAccount(nickname: String) {
        auth.currentUser?.let {
        }
    }

    // 이전에 로그인 한 계정이 있는지 확인
    private fun getLastSignedInAccount(context: Context) =
        GoogleSignIn.getLastSignedInAccount(context) != null
}
