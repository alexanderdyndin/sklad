package com.work.sklad.feature.common.compose.views

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.TextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
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
        visualTransformation: VisualTransformation = VisualTransformation.None,
        maxChars: Int = 20,
        onlyDigits: Boolean = false,
        onValueChange: (String) -> Unit = {}) {
    TextField(
        value = value.orEmpty(),
        onValueChange = { onValueChange.invoke(it.limitedChangeListener(maxChars)
            .filter { char -> if (onlyDigits) char.isDigit() else true }) },
        label = { Text(label) },
        modifier = modifier.fillMaxWidth(.8f),
        enabled = enabled,
        readOnly = readOnly,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        singleLine = true,
        visualTransformation = visualTransformation
    )
    Spacer(modifier = Modifier.padding(5.dp))
}

class MaskPhoneTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val trimmed = if (text.text.length >= 11) text.text.substring(0..10) else text.text
        var out = ""
        for (i in trimmed.indices) {
            out += trimmed[i]
            if (i==0) out += "("
            if (i==3) out += ")"
            if (i==6) out += "-"
            if (i==8) out += "-"
        }

        val numberOffsetTranslator = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                if (offset <= 0) return offset
                if (offset <= 3) return offset + 1
                if (offset <= 6) return offset + 2
                if (offset <= 8) return offset + 3
                if (offset <= 10) return offset + 4
                return 15

            }

            override fun transformedToOriginal(offset: Int): Int {
                if (offset <= 1) return offset
                if (offset <= 4) return offset - 1
                if (offset <= 7) return offset - 2
                if (offset <= 9) return offset - 3
                if (offset <= 11) return offset - 4
                return 11
            }
        }

        return TransformedText(AnnotatedString(out), numberOffsetTranslator)
    }
}