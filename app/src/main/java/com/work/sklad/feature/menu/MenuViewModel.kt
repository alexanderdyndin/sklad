package com.work.sklad.feature.menu

import androidx.lifecycle.viewModelScope
import com.work.sklad.data.model.User
import com.work.sklad.data.model.UserType
import com.work.sklad.data.model.UserType.*
import com.work.sklad.feature.common.UserId
import com.work.sklad.feature.common.base.BaseMutator
import com.work.sklad.feature.common.base.BaseViewModel
import com.work.sklad.feature.menu.MenuAction.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MenuViewModel @Inject constructor(): BaseViewModel<MenuState, MenuMutator, MenuAction>(MenuState(), MenuMutator()) {

    fun init() {
        val id = sharedPreferences.get(UserId, -1)
        viewModelScope.launch {
            skladDao.searchUser(id).collectLatest {
                it.firstOrNull()?.let {
                    mutateState { setUser(it) }
                }
            }
        }
    }

    fun navigateClient() {
        when (state.value.user?.userType) {
            WarehouseMan, Picker -> message("У вас нет прав для просмотра этой категории")
            else -> action(Client)
        }
    }

    fun navigateInvoice() {
        action(Invoice)
    }

    fun navigateInvoiceComing() {
        action(InvoiceComing)
    }

    fun navigateOrder() {
        action(Order)
    }

    fun navigateProduct() {
        action(Product)
    }

    fun navigateProductType() {
        action(ProductType)
    }

    fun navigateSupplier() {
        when (state.value.user?.userType) {
            SalesManager, WarehouseManager, Admin -> action(Supplier)
            else -> message("У вас нет прав для просмотра этой категории")
        }
    }

    fun navigateWarehouse() {
        action(Warehouse)
    }

    fun navigateUsers() {
        action(Users)
    }

    fun logOut(){
        action(Logout)
        sharedPreferences.set(UserId, -1)
    }

}

data class MenuState(
    val user: User? = null
)

class MenuMutator: BaseMutator<MenuState>() {

    fun setUser(user: User) {
        state = state.copy(user = user)
    }

}

sealed class MenuAction {
    object Client : MenuAction()
    object Invoice : MenuAction()
    object InvoiceComing : MenuAction()
    object Order : MenuAction()
    object Product : MenuAction()
    object ProductType : MenuAction()
    object Supplier : MenuAction()
    object Warehouse : MenuAction()
    object Logout : MenuAction()
    object Users : MenuAction()
}