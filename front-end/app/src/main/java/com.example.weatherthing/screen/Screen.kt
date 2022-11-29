package com.example.weatherthing.screen

sealed class Screen(val route: String){
    object Home: Screen("Home")
}