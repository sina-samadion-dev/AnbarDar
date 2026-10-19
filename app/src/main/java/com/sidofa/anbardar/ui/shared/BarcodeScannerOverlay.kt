package com.sidofa.anbardar.ui.components

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import com.sidofa.anbardar.utils.imageProxyToBitmap
import com.sidofa.anbardar.ui.theme.Yellow40
import java.util.concurrent.Executors

@Composable
fun BarcodeScannerOverlay(
    onBarcodeDetected: (barcode : String) -> Unit,
    onClose: () -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var hasPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted -> hasPermission = granted }

    LaunchedEffect(Unit) {
        if (!hasPermission) permissionLauncher.launch(Manifest.permission.CAMERA)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        if (hasPermission) {
            val cameraExecutor = remember { Executors.newSingleThreadExecutor() }
            val barcodeScanner = remember {
                BarcodeScanning.getClient(
                    BarcodeScannerOptions.Builder()
                        .setBarcodeFormats(Barcode.FORMAT_ALL_FORMATS)
                        .build()
                )
            }
            var scanned by remember { mutableStateOf(false) }

            DisposableEffect(Unit) {
                onDispose {
                    cameraExecutor.shutdown()
                    barcodeScanner.close()
                }
            }

            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { ctx ->
                    val previewView = PreviewView(ctx).apply {
                        implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                    }
                    val future = ProcessCameraProvider.getInstance(ctx)
                    future.addListener({
                        val provider = future.get()

                        val preview = Preview.Builder().build().also {
                            it.surfaceProvider = previewView.surfaceProvider
                        }

                        val analysis = ImageAnalysis.Builder()
                            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                            .build()

                        analysis.setAnalyzer(cameraExecutor) { imageProxy ->
                            if (!scanned) {
                                val bitmap = imageProxyToBitmap(imageProxy)
                                barcodeScanner
                                    .process(
                                        InputImage.fromBitmap(
                                            bitmap,
                                            imageProxy.imageInfo.rotationDegrees
                                        )
                                    )
                                    .addOnSuccessListener { barcodes ->
                                        val value = barcodes.firstOrNull()?.rawValue
                                        if (value != null && !scanned) {
                                            scanned = true
                                            onBarcodeDetected(value)
                                            onClose()
                                        }
                                    }
                            }
                            imageProxy.close()
                        }

                        provider.unbindAll()
                        provider.bindToLifecycle(
                            lifecycleOwner,
                            CameraSelector.DEFAULT_BACK_CAMERA,
                            preview,
                            analysis
                        )
                    }, ContextCompat.getMainExecutor(ctx))

                    previewView
                }
            )

            Box(modifier = Modifier.fillMaxSize()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.4f))
                )

                Box(
                    modifier = Modifier.align(Alignment.Center),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(250.dp)
                            .background(Color.Transparent)
                    )
                    ScanFrame(size = 250.dp)
                }

                Column(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(top = 290.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "بارکد را داخل کادر قرار دهید",
                        color = Color.White,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        } else {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = "دسترسی به دوربین مجاز نیست.\nلطفاً مجوز دوربین را فعال کنید.",
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(32.dp)
                )
            }
        }

        TextButton(
            onClick = onClose,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(12.dp)
        ) {
            Text(
                text = "بستن  ✕",
                color = Yellow40,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
@Composable
private fun ScanFrame(size: Dp) {
    val arm = 32.dp
    val stroke = 4.dp
    val half = size / 2

    Box(modifier = Modifier.size(size)) {
        Corner(Modifier.align(Alignment.TopStart), arm, stroke, flipH = false, flipV = false)
        Corner(Modifier.align(Alignment.TopEnd), arm, stroke, flipH = true, flipV = false)
        Corner(Modifier.align(Alignment.BottomStart), arm, stroke, flipH = false, flipV = true)
        Corner(Modifier.align(Alignment.BottomEnd), arm, stroke, flipH = true, flipV = true)
    }
}

@Composable
private fun Corner(
    modifier: Modifier,
    armLen: Dp,
    stroke: Dp,
    flipH: Boolean,
    flipV: Boolean
) {
    Box(modifier = modifier.size(armLen)) {
        Box(
            modifier = Modifier
                .width(armLen)
                .height(stroke)
                .align(if (flipV) Alignment.BottomStart else Alignment.TopStart)
                .background(Yellow40, RoundedCornerShape(stroke))
        )
        Box(
            modifier = Modifier
                .width(stroke)
                .height(armLen)
                .align(if (flipH) Alignment.TopEnd else Alignment.TopStart)
                .background(Yellow40, RoundedCornerShape(stroke))
        )
    }
}
