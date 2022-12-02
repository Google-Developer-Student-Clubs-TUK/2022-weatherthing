package com.example.weatherthing.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.weatherthing.data.User

class AppPref(context: Context) {
    private val prefFilename = "userPref"
    private val prefs: SharedPreferences = context.getSharedPreferences(prefFilename, 0)

    fun getUserPref(): User? {
        val uId = getString("uId")
        return if (uId != null) {
            User(uId = uId, nickname = getString("nickname") ?: "", email = getString("email") ?: "", age = getInt("age"), gender = getInt("gender"))
        } else {
            null
        }
    }

    fun setUserPref(user: User) {
        setString("uId", user.uId)
        setString("email", user.email)
        setString("nickname", user.nickname)
        setInt("age", user.age)
        setInt("gender", user.gender)
    }

    private fun setString(key: String, value: String) {
        prefs.edit().putString(key, value).apply()
    }
    private fun setInt(key: String, value: Int) {
        prefs.edit().putInt(key, value).apply()
    }
    fun setBoolean(key: String, value: Boolean) {
        prefs.edit().putBoolean(key, value).apply()
    }

    private fun getString(key: String): String? = prefs.getString(key, "")

    private fun getInt(key: String): Int = prefs.getInt(key, 0)

    private fun getBoolean(key: String): Boolean =
        prefs.getBoolean(key, false)
}
