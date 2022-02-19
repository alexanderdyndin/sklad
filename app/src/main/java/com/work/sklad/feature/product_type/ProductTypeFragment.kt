package com.work.sklad.feature.product_type

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.work.sklad.R
import com.work.sklad.feature.common.Event
import com.work.sklad.feature.common.Events
import com.work.sklad.feature.common.base.BaseFragment
import com.work.sklad.feature.common.compose.ComposeScreen
import com.work.sklad.feature.common.compose.composeView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@ExperimentalMaterialApi
@AndroidEntryPoint
class ProductTypeFragment: BaseFragment() {

    private val viewModel: ProductTypeViewModel by viewModels()

    override val eventsAction: ((Event) -> Unit) = {
        when (it) {
            is AddProductTypeEvent -> viewModel.add(it.type)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launchWhenCreated {
            viewModel.action.collectLatest {
                when (it) {
                    ProductTypeAction.OpenBottom -> R.id.action_productTypeFragment_to_addProductTypeFragment.navigate()
                }
            }
        }
    }

    override fun view(): View = composeView(requireContext()) {
        ComposeScreen(title = "Типы продукта", floatingActionButton = {
            FloatingActionButton(
                shape = CircleShape,
                onClick = { viewModel.openBottom() }) { Icon(Icons.Filled.Add,"") }
        }) {
            ProductTypesScreen(viewModel = viewModel)
        }
    }

}