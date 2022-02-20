package com.work.sklad.feature.common.compose.views

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.TextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.work.sklad.feature.common.utils.limitedChangeListener

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
        onValueChange = { onValueChange.invoke(it.limitedChangeListener(20)) },
        label = { Text(label) },
        modifier = modifier.fillMaxWidth(.8f),
        enabled = enabled,
        readOnly = readOnly,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        singleLine = true
    )
    Spacer(modifier = Modifier.padding(5.dp))
}