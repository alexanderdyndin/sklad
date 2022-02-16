package com.work.sklad.feature.common.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.work.sklad.R
import com.work.sklad.feature.common.Event
import com.work.sklad.feature.common.Events
import com.work.sklad.feature.common.compose.composeBottomView
import com.work.sklad.feature.common.compose.composeView
import com.work.sklad.feature.login.Registration
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

abstract class BaseBottomSheet: BottomSheetDialogFragment() {

    @Inject
    lateinit var events: Events

    open val eventsAction: ((Event) -> Unit)? = null

    open val composable: @Composable (() -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launchWhenCreated {
            events.getFlow().collectLatest { eventsAction?.invoke(it) }
        }
    }

    override fun getTheme() = R.style.BaseBottomSheetDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = composeBottomView(requireContext()){ composable?.invoke() }



}