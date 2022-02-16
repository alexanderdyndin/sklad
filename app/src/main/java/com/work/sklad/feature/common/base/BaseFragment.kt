package com.work.sklad.feature.common.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.work.sklad.feature.common.Event
import com.work.sklad.feature.common.Events
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

abstract class BaseFragment: Fragment() {

    @Inject
    lateinit var events: Events

    open val backPressAction: (() -> Unit)? = null

    open val eventsAction: ((Event) -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                backPressAction?.invoke() ?: back()
            }
        })
        lifecycleScope.launchWhenCreated {
            events.getFlow().collectLatest { eventsAction?.invoke(it) }
        }
    }

    abstract fun view(): View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = view()

    protected fun Int.navigate() {
        findNavController().navigate(this)
    }

    protected fun back() {
        findNavController().popBackStack()
    }

}