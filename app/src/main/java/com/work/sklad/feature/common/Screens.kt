package com.work.sklad.feature.common

sealed class Screens(val route: String) {
    object Login : Screens("login")
    object Menu : Screens("menu")
}