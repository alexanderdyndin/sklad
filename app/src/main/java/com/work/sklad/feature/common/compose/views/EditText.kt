package com.work.sklad.feature.common.compose.views

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.TextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun EditText(
        modifier: Modifier = Modifier,
        value: String? = null,
        label: String = "",
        enabled: Boolean = true,
        readOnly: Boolean = false,
        keyboardType: KeyboardType = KeyboardType.Text,
        onValueChange: (String) -> Unit = {}) {
    TextField(
        value = value.orEmpty(),
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = modifier,
        enabled = enabled,
        readOnly = readOnly,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType)
    )
}