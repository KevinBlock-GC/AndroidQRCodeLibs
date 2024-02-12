package com.example.qrcodeexamples

import android.graphics.Bitmap
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import com.example.qrcodeexamples.ui.theme.QRCodeExamplesTheme
import kotlinx.coroutines.launch
import kotlin.system.measureTimeMillis

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            QRCodeExamplesTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    var qrCodeBitmap: Bitmap? by remember { mutableStateOf(null) }
                    var textInput by remember { mutableStateOf("Edit me!") }
                    var generationText by remember { mutableStateOf("") }
                    val coroutineScope = rememberCoroutineScope()

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Column(
                            modifier = Modifier.weight(1f),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            val buttonConfigs = listOf(
                                "Generate with Zxing core" to QRCodeLibrary.ZxingCore,
                                "Generate with qrcode-kotlin" to QRCodeLibrary.QrcodeKotlin,
                                "Generate with QRGen" to QRCodeLibrary.QRGen,
                                "Generate with zxing-android-embedded" to QRCodeLibrary.ZxingAndroidEmbedded
                            )
                            buttonConfigs.forEach { (title, qrCodeLibrary) ->
                                Button(onClick = {
                                    coroutineScope.launch {
                                        measureTimeMillis {
                                            qrCodeBitmap = qrCodeLibrary.generate(textInput)
                                        }.let { time ->
                                            generationText = "Generated with ${qrCodeLibrary.name} in $time ms"
                                        }
                                    }
                                }) {
                                    Text(title)
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                value = textInput,
                                onValueChange = { textInput = it },
                                label = { Text("QR Code Data") }
                            )
                        }
                        Column(
                            modifier = Modifier.weight(1f),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            qrCodeBitmap?.let {
                                Image(
                                    modifier = Modifier,
                                    bitmap = it.asImageBitmap(),
                                    contentDescription = ""
                                )
                            }
                            if (generationText.isNotEmpty()) {
                                Text(generationText)
                            }
                         }
                    }
                }
            }
        }
    }
}