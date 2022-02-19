package com.work.sklad.feature.common.compose.views

import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun DropDownChangeDelete(expanded: Boolean, onDelete: () -> Unit, onEdit: () -> Unit, onDismiss: () -> Unit) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismiss
    ) {
        DropdownMenuItem(text = { Text("Редактировать") }, onClick = {
            onEdit.invoke()
            onDismiss.invoke()
        })
        DropdownMenuItem(text = { Text("Удалить") }, onClick = {
            onDelete.invoke()
            onDismiss.invoke()
        })
    }
}