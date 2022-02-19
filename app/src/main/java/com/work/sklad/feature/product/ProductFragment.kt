package com.work.sklad.feature.product

import android.os.Bundle
import android.view.View
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.work.sklad.R
import com.work.sklad.feature.common.Event
import com.work.sklad.feature.common.base.BaseFragment
import com.work.sklad.feature.common.compose.ComposeScreen
import com.work.sklad.feature.common.compose.composeView
import com.work.sklad.feature.product.ProductAction.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@ExperimentalMaterialApi
@AndroidEntryPoint
class ProductFragment: BaseFragment() {

    private val viewModel: ProductViewModel by viewModels()

    override val eventsAction: ((Event) -> Unit) = {
        when (it) {
            is AddProductEvent -> viewModel.add(it.name, it.unit, it.typeId)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launchWhenCreated {
            viewModel.action.collectLatest {
                when (it) {
                    is OpenBottom -> {
                        val bundle = bundleOf("types" to it.types.toTypedArray())
                        R.id.action_productFragment_to_addProductFragment.navigate(bundle)
                    }
                }
            }
        }
    }

    override fun view(): View = composeView(requireContext()) {
        ComposeScreen(title = "Товары", floatingActionButton = {
            FloatingActionButton(
                shape = CircleShape,
                onClick = { viewModel.openBottom() }) { Icon(Icons.Filled.Add,"") }
        }) {
            ProductsScreen(viewModel = viewModel)
        }
    }

}