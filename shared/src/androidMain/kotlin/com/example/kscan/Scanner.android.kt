package com.example.kscan

import android.content.Context
import androidx.compose.runtime.Composable
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScanner
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning

actual object Scanner {

    var scanner: GmsBarcodeScanner

    init {
        scanner =
            provideBarcodeScanner(AndroidApp.INSTANCE.applicationContext, provideBarcodeOptions())
    }

    @Composable
    actual operator fun invoke(
        onSuccess: (String) -> Unit,
        onFailed: (Exception) -> Unit,
        onCanceled: () -> Unit
    ) {
        scanner.startScan()
            .addOnSuccessListener {
                onSuccess(getDetails(it))
            }
            .addOnFailureListener {
                onFailed(it)
            }
            .addOnCanceledListener {
                onCanceled()
            }
    }

    private fun getDetails(barcode: Barcode): String {
        return barcode.rawValue ?: ""
    }

    private fun provideBarcodeScanner(
        context: Context,
        options: GmsBarcodeScannerOptions
    ): GmsBarcodeScanner {
        return GmsBarcodeScanning.getClient(context, options)
    }

    private fun provideBarcodeOptions(): GmsBarcodeScannerOptions {
        return GmsBarcodeScannerOptions.Builder()
            .setBarcodeFormats(Barcode.FORMAT_ALL_FORMATS)
            .enableAutoZoom()
            .build()
    }

}