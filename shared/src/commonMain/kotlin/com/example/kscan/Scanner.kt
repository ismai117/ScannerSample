package com.example.kscan

import androidx.compose.runtime.Composable

expect object Scanner {
    @Composable
    operator fun invoke(
        onSuccess: (String) -> Unit,
        onFailed: (Exception) -> Unit,
        onCanceled: () -> Unit
    )
}