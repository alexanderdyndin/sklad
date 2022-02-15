package com.work.sklad.feature.common.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.work.sklad.R

abstract class BaseBottomSheet: BottomSheetDialogFragment() {

    override fun getTheme() = R.style.BaseBottomSheetDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = view()

    abstract fun view(): View

}