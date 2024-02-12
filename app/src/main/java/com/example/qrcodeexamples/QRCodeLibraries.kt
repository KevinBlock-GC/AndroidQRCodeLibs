package com.example.qrcodeexamples

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import com.journeyapps.barcodescanner.BarcodeEncoder
import qrcode.QRCode


sealed class QRCodeLibrary {
    abstract fun generate(text: String): Bitmap
    abstract val name: String

    data object ZxingCore : QRCodeLibrary() {
        override fun generate(text: String): Bitmap {
            val writer = QRCodeWriter()
            val bitMatrix = writer.encode(text, BarcodeFormat.QR_CODE, 400, 400)

            val w = bitMatrix.width
            val h = bitMatrix.height
            val pixels = IntArray(w * h)
            for (y in 0 until h) {
                for (x in 0 until w) {
                    pixels[y * w + x] = if (bitMatrix[x, y]) Color.BLACK else Color.WHITE
                }
            }

            val bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
            bitmap.setPixels(pixels, 0, w, 0, 0, w, h)
            return bitmap
        }

        override val name: String
            get() = "Zxing Core"

    }
    data object QrcodeKotlin : QRCodeLibrary() {
        override fun generate(text: String): Bitmap {
            val bytes = QRCode.ofSquares()
                .withSize(15)
                .build(text)
                .render()
                .getBytes()

            return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        }

        override val name: String
            get() = "qrcode-kotlin"
    }

    data object QRGen : QRCodeLibrary() {
        override fun generate(text: String): Bitmap =
            net.glxn.qrgen.android.QRCode
                .from(text)
                .withSize(800, 800)
                .bitmap()

        override val name: String
            get() = "QRGen"
    }

    data object ZxingAndroidEmbedded : QRCodeLibrary() {
        override fun generate(text: String): Bitmap =
            // ZxingAndroidEmbedded's purpose is to scan barcodes however it can generate them too
            BarcodeEncoder()
                .encodeBitmap(text, BarcodeFormat.QR_CODE, 800, 800)

        override val name: String
            get() = "zxing-android-embedded"
    }
}