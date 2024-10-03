package com.example.kscan

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() = MaterialTheme {

    var showScanner by remember { mutableStateOf(true) }
    var code by remember { mutableStateOf("") }


    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Scanned Code: $code")
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick = {
                    showScanner = true
                }
            ){
                Text("Scan")
            }
        }
    }

    if (showScanner) {
        Scanner(
            onSuccess = {
                code = it.also { showScanner = false }
            },
            onFailed = {
                it.printStackTrace()
            },
            onCanceled = {
                showScanner = false
            }
        )
    }

}