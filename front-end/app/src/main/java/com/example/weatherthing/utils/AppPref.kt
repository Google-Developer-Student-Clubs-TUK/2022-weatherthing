package com.example.weatherthing.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.weatherthing.data.User

class AppPref(context: Context) {
    private val prefFilename = "userPref"
    private val prefs: SharedPreferences = context.getSharedPreferences(prefFilename, 0)

    fun getUserPref(): User? {
        val id = getInt("id")
        return if (id == -99) {
            null
        } else {
            User(
                id = getInt("id"),
                uid = getString("uid") ?: "",
                nickname = getString("nickname") ?: "",
                email = getString("email") ?: "",
                age = getInt("age"),
                genderCode = getInt("gender"),
                weatherCode = getInt("weather"),
                regionCode = getInt("regionCode")
            )
        }
    }

    fun setUserPref(user: User) {
        setInt("id", user.id!!)
        setString("uId", user.uid)
        setString("email", user.email)
        setString("nickname", user.nickname)
        setInt("age", user.age)
        setInt("gender", user.genderCode)
        setInt("weather", user.weatherCode)
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

    private fun getString(key: String): String? = prefs.getString(key, null)

    private fun getInt(key: String): Int = prefs.getInt(key, -99)

    private fun getBoolean(key: String): Boolean =
        prefs.getBoolean(key, false)
}
