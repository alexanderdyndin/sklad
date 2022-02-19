package com.work.sklad.feature.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.work.sklad.R
import com.work.sklad.feature.common.Event
import com.work.sklad.feature.common.Events
import com.work.sklad.feature.common.base.BaseFragment
import com.work.sklad.feature.common.compose.ComposeScreen
import com.work.sklad.feature.common.compose.composeView
import com.work.sklad.feature.menu.MenuAction.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@ExperimentalMaterialApi
@AndroidEntryPoint
class MenuFragment: BaseFragment() {

    private val viewModel: MenuViewModel by viewModels()

    var backPressedToExit = false

    override val backPressAction: (() -> Unit) = {
        lifecycleScope.launch(Dispatchers.Default) {
            if (backPressedToExit) {
                requireActivity().finish()
            } else {
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Нажмите еще раз для выхода", Toast.LENGTH_SHORT).show()
                }
                backPressedToExit = true
            }
            delay(3000)
            backPressedToExit = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launchWhenCreated {
            viewModel.action.collectLatest {
                when (it) {
                    Client -> R.id.action_menuFragment_to_clientFragment.navigate()
                    Invoice -> R.id.action_menuFragment_to_invoiceFragment.navigate()
                    InvoiceComing -> R.id.action_menuFragment_to_invoiceComingFragment.navigate()
                    Order -> R.id.action_menuFragment_to_orderFragment.navigate()
                    Product -> R.id.action_menuFragment_to_productFragment.navigate()
                    ProductType -> R.id.action_menuFragment_to_productTypeFragment.navigate()
                    Supplier -> R.id.action_menuFragment_to_supplierFragment2.navigate()
                    Warehouse -> R.id.action_menuFragment_to_warehouseFragment.navigate()
                    Logout -> back()
                }
            }
        }
    }

    override fun view(): View = composeView(requireContext()) {
        ComposeScreen(
            title = "Меню",
            actions = {
                Icon(imageVector = Icons.Filled.Logout, contentDescription = "", modifier = Modifier.clickable {
                    viewModel.logOut()
                })
            }
        ) {
            MenuScreen(viewModel)
        }
    }

}