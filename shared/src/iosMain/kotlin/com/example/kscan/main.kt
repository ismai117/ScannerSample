package com.example.kscan

import androidx.compose.ui.window.ComposeUIViewController
import platform.UIKit.UIViewController

fun MainViewController(
    scannerUIViewController: (
        onSuccess: (String) -> Unit,
        onFailed: (Exception) -> Unit,
        onCanceled: () -> Unit
    ) -> UIViewController
) = ComposeUIViewController {
    scannerViewController = scannerUIViewController
    App()
}