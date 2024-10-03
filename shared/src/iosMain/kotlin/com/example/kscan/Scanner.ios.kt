package com.example.kscan

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.interop.UIKitViewController
import kotlinx.cinterop.ExperimentalForeignApi
import platform.UIKit.UIViewController

actual object Scanner {

    @OptIn(ExperimentalForeignApi::class)
    @Composable
    actual operator fun invoke(
        onSuccess: (String) -> Unit,
        onFailed: (Exception) -> Unit,
        onCanceled: () -> Unit
    ) {
        UIKitViewController(
            factory = { scannerViewController.invoke(onSuccess, onFailed, onCanceled) },
            modifier = Modifier.fillMaxSize()
        )
    }

}

lateinit var scannerViewController: (
    onSuccess: (String) -> Unit,
    onFailed: (Exception) -> Unit,
    onCanceled: () -> Unit
) -> UIViewController