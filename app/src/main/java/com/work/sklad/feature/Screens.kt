package com.work.sklad.feature

sealed class Screens(val route: String) {
    object Login : Screens("login")
}