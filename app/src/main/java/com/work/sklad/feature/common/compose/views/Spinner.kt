package com.work.sklad.feature.common.compose.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun <T> Spinner(stateList: Array<T>, initialState: T, nameMapper: (T) -> String, onChanged: (T) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Box(Modifier.fillMaxWidth(),contentAlignment = Alignment.Center) {
        Row(
            Modifier
                .padding(16.dp)
                .clickable {
                    expanded = !expanded
                },
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = nameMapper.invoke(initialState),fontSize = 18.sp,modifier = Modifier.padding(end = 8.dp))
            Icon(imageVector = Icons.Filled.ArrowDropDown, contentDescription = "")
            DropdownMenu(expanded = expanded, onDismissRequest = {
                expanded = false
            }) {
                stateList.forEach{ item ->
                    DropdownMenuItem(onClick = {
                        expanded = false
                        onChanged.invoke(item)
                    }) {
                        Text(text = nameMapper.invoke(item))
                    }
                }
            }
        }
    }

}