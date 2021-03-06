package com.work.sklad.feature.common.compose

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.work.sklad.feature.common.AppTheme
import com.work.sklad.feature.common.compose.views.EditText
import com.work.sklad.feature.common.utils.isNotNull

fun composeView(context: Context, content: @Composable () -> Unit): ComposeView =
    ComposeView(context).apply {
        setContent {
            AppTheme() {
                androidx.compose.material3.Surface(color = androidx.compose.material3.MaterialTheme.colorScheme.background, content = content)
            }
        }
    }
fun composeBottomView(context: Context, content: @Composable () -> Unit): ComposeView =
    ComposeView(context).apply {
        setContent {
            AppTheme() {
                androidx.compose.material3.Surface(color = androidx.compose.material3.MaterialTheme.colorScheme.background,
                    content = content, shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            }
        }
    }

@Composable
fun ComposeScreen(navigationListener: (() -> Unit)? = null, title: String? = null,
                  toolbarBackgroundColor: Color? = null,
                  actions: @Composable RowScope.() -> Unit = {},
                  floatingActionButton: @Composable () -> Unit = {},
                  content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        floatingActionButton = floatingActionButton,
        topBar = {
            TopAppBar(
                title = {
                    title?.let {
                        Text(
                            text = title,
                            style = TextStyle(
                                fontFamily = FontFamily.SansSerif,
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.Medium,
                                fontSize = 20.sp,
                                color = Color.White
                            )
                        )
                    }
                },
                navigationIcon = if (navigationListener.isNotNull()) {
                    {
                        IconButton(onClick = { navigationListener?.invoke() }) {
                            Icon(Icons.Filled.ArrowBack, "")
                        }
                    }
                } else null,
                actions = actions,
                backgroundColor = toolbarBackgroundColor?.let {
                    toolbarBackgroundColor
                } ?: MaterialTheme.colors.primarySurface,
            )
        },
        content = content,
        bottomBar = {

        }
    )
}

@Composable
fun ComposeSearchScreen(hint: String, text: String, textChange: (String) -> Unit,
                  floatingActionButton: @Composable () -> Unit = {},
                  content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        floatingActionButton = floatingActionButton,
        topBar = {
            Surface(color =  MaterialTheme.colors.primarySurface, elevation = AppBarDefaults.TopAppBarElevation) {
                TextField(
                    value = text,
                    onValueChange = textChange,
                    label = { Text(
                        text = hint,
                        style = TextStyle(
                            fontFamily = FontFamily.SansSerif,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Medium,
                            fontSize = 20.sp,
                            color = Color.White
                        )
                    ) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }
        },
        content = content,
        bottomBar = {

        }
    )
}

