package com.work.sklad.feature.common.base

import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.work.sklad.data.dao.SkladDao
import com.work.sklad.domain.repository.ISharedPreferencesRepository
import com.work.sklad.feature.Screens
import javax.inject.Inject

abstract class BaseViewModel: ViewModel() {

    @Inject
    lateinit var sharedPreferences: ISharedPreferencesRepository

    @Inject
    lateinit var skladDao: SkladDao

    @Inject
    lateinit var navigationComponent: NavHostController

    fun navigate(screens: Screens) {
        navigationComponent.navigate(screens.route)
    }
}