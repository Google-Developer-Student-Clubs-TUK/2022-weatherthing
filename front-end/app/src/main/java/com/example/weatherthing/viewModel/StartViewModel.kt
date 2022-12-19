package com.example.weatherthing.viewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherthing.data.User
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
    val loginState = MutableStateFlow<LoginState>(LoginState.Loading)
    val pref = AppPref(App.context)
    val prefUser = pref.getUserPref()

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
                            if (prefUser != null) {
                                LoginState.CompleteLogin
                            } else {
                                // val user : User? = db get
//                                if(user != null){
//                                    pref.setUserPref(user)
//                                    LoginState.CompleteLogin
//                                }else{
//                                    LoginState.RequireRegister
//                                }
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

    fun sign(nickname: String, age: Int, gender: Int, weather: Int) {
        auth.currentUser?.let {
            val user = User(it.uid, it.email ?: "", nickname, gender, age, weather)
            pref.setUserPref(user)
            loginState.value = LoginState.CompleteLogin
        }
    }

    fun checkAfterGoogleLogin() { // 구글 로그인 완료 후
        // sharedPreference에 저장된 데이터가 있으면 가입된 유저라고 간주
        loginState.value = if (prefUser != null) {
            Log.d("유저", prefUser.toString())
            LoginState.RequireRegister
        } else {
            // val user : User? = db get
//                                if(user != null){
//                                    pref.setUserPref(user)
//                                    LoginState.CompleteLogin
//                                }else{
//                                    LoginState.RequireRegister
//                                }
            LoginState.RequireRegister
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
