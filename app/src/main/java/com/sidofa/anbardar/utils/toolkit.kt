package com.sidofa.anbardar.utils

import android.graphics.Bitmap
import android.icu.util.Calendar
import android.icu.util.ULocale
import androidx.camera.core.ImageProxy
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

fun imageProxyToBitmap(imageProxy: ImageProxy) : Bitmap{
    val width = imageProxy.width
    val height = imageProxy.height

    val yPlane = imageProxy.planes[0]
    val uPlane = imageProxy.planes[1]
    val vPlane = imageProxy.planes[2]

    val yBuffer = yPlane.buffer
    val uBuffer = uPlane.buffer
    val vBuffer = vPlane.buffer

    val yRowStride = yPlane.rowStride
    val uRowStride = uPlane.rowStride
    val vRowStride = vPlane.rowStride

    val yPixelStride = yPlane.pixelStride
    val uPixelStride = uPlane.pixelStride
    val vPixelStride = vPlane.pixelStride

    val pixels = IntArray(width * height)

    for (y in 0 until height) {
        val yRowStart = y * yRowStride

        for (x in 0 until width) {

            val yIndex = yRowStart + x * yPixelStride

            val uvX = x / 2
            val uvY = y / 2

            val uIndex =
                uvY * uRowStride + uvX * uPixelStride

            val vIndex =
                uvY * vRowStride + uvX * vPixelStride

            val yValue = yBuffer.get(yIndex).toInt() and 0xFF
            val uValue = uBuffer.get(uIndex).toInt() and 0xFF
            val vValue = vBuffer.get(vIndex).toInt() and 0xFF

            val yCorrected = yValue - 16
            val uCorrected = uValue - 128
            val vCorrected = vValue - 128

            val r = (1.164 * yCorrected + 1.596 * vCorrected)
                .toInt()
                .coerceIn(0, 255)

            val g = (1.164 * yCorrected
                    - 0.392 * uCorrected
                    - 0.813 * vCorrected)
                .toInt()
                .coerceIn(0, 255)

            val b = (1.164 * yCorrected + 2.017 * uCorrected)
                .toInt()
                .coerceIn(0, 255)

            pixels[y * width + x] =
                (255 shl 24) or
                        (r shl 16) or
                        (g shl 8) or
                        b
        }
    }

    return Bitmap.createBitmap(
        pixels,
        width,
        height,
        Bitmap.Config.ARGB_8888
    )
}


fun Instant.toFormattedString(
    pattern: String = "yyyy-MM-dd | HH:mm",
    zoneId: ZoneId = ZoneId.systemDefault()
): String {
    val formatter = DateTimeFormatter.ofPattern(pattern, Locale.getDefault())
        .withZone(zoneId)
    return formatter.format(this)
}


fun Instant.toPersianDateString(): String {
    val uLocale = ULocale("fa_IR@calendar=persian")
    val calendar = Calendar.getInstance(uLocale)

    calendar.timeInMillis = this.toEpochMilli()

    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH) + 1
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    val minute = calendar.get(Calendar.MINUTE)

    return String.format(Locale.US, "%04d/%02d/%02d - %02d:%02d", year, month, day, hour, minute)
}