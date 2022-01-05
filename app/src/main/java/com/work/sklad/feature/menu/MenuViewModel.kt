package com.work.sklad.feature.menu

import com.work.sklad.data.model.UserType
import com.work.sklad.feature.common.base.BaseMutator
import com.work.sklad.feature.common.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MenuViewModel @Inject constructor(): BaseViewModel<MenuState, MenuMutator, Nothing>(MenuState(), MenuMutator()) {
}

data class MenuState(val userType: UserType? = null)

class MenuMutator: BaseMutator<MenuState>() {

}