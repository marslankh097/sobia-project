package com.example.budgetly.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.graphics.Canvas
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Matrix
import android.graphics.Paint
import android.media.ExifInterface
import android.os.Build
import com.example.budgetly.domain.models.enums.transaction.TransactionDuration

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.security.MessageDigest

object Utils {
    fun loadBitmapFromUri(context: Context, uri: Uri): Bitmap {
        val inputStream = context.contentResolver.openInputStream(uri)
            ?: throw IOException("Cannot open input stream")

        val exif: ExifInterface = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            ExifInterface(inputStream)
        } else {
            // For older Android versions, need a file path
            val fileDescriptor = context.contentResolver.openFileDescriptor(uri, "r")?.fileDescriptor
                ?: throw IOException("Cannot open file descriptor")
            ExifInterface(FileInputStream(fileDescriptor).fd)
        }

        // Reset stream as ExifInterface might have consumed it
        val bitmap = context.contentResolver.openInputStream(uri)?.use { input ->
            BitmapFactory.decodeStream(input)
        } ?: throw IOException("Cannot decode image from URI")

        return rotateBitmapIfRequired(bitmap, exif)
    }
    private fun rotateBitmapIfRequired(bitmap: Bitmap, exif: ExifInterface): Bitmap {
        val orientation = exif.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_NORMAL
        )

        val matrix = Matrix()
        when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> matrix.postRotate(90f)
            ExifInterface.ORIENTATION_ROTATE_180 -> matrix.postRotate(180f)
            ExifInterface.ORIENTATION_ROTATE_270 -> matrix.postRotate(270f)
            ExifInterface.ORIENTATION_FLIP_HORIZONTAL -> matrix.preScale(-1f, 1f)
            ExifInterface.ORIENTATION_FLIP_VERTICAL -> matrix.preScale(1f, -1f)
            else -> return bitmap // no rotation needed
        }

        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }


    //    fun loadBitmapFromUri(context: Context, uri: Uri): Bitmap {
//        return context.contentResolver.openInputStream(uri)?.use { input ->
//            BitmapFactory.decodeStream(input)
//        } ?: throw IOException("Cannot decode image from URI")
//    }
    fun preprocessBitmap(original: Bitmap): Bitmap {
        val resized = resizeBitmap(original)
        val grayscale = toGrayscale(resized)
        return binarize(grayscale)
    }
    fun saveBitmapToFile(context: Context, bitmap: Bitmap): File {
        val file = File(context.cacheDir, "processed_${System.currentTimeMillis()}.jpg")
        FileOutputStream(file).use { out ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
        }
        return file
    }
    fun binarize(bitmap: Bitmap): Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        val binarized = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

        for (x in 0 until width) {
            for (y in 0 until height) {
                val pixel = bitmap.getPixel(x, y)
                val gray = Color.red(pixel)  // since it's already grayscale
                val threshold = 128
                val newColor = if (gray < threshold) Color.BLACK else Color.WHITE
                binarized.setPixel(x, y, newColor)
            }
        }
        return binarized
    }
    fun toGrayscale(bitmap: Bitmap): Bitmap {
        val grayscale = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(grayscale)
        val paint = Paint()
        val matrix = ColorMatrix()
        matrix.setSaturation(0f)
        paint.colorFilter = ColorMatrixColorFilter(matrix)
        canvas.drawBitmap(bitmap, 0f, 0f, paint)
        return grayscale
    }
    fun resizeBitmap(bitmap: Bitmap, maxDimension: Int = 1024): Bitmap {
        val ratio = minOf(
            maxDimension.toFloat() / bitmap.width,
            maxDimension.toFloat() / bitmap.height
        )
        return Bitmap.createScaledBitmap(
            bitmap,
            (bitmap.width * ratio).toInt(),
            (bitmap.height * ratio).toInt(),
            true
        )
    }













    val referenceToRequisitionId = mutableMapOf<String, String>()
    fun storeRequisition(reference: String, id: String) {
        referenceToRequisitionId[reference] = id
    }
     fun getTimeStampsFromDays(days:Int):Pair<Long, Long>{
         log("days: $days")
        val now = System.currentTimeMillis()
        val fromTimestamp = now - days * 24 * 60 * 60 * 1000L  // convert days to millis
        val toTimestamp = System.currentTimeMillis()
        return Pair(fromTimestamp, toTimestamp)
    }
    fun lerp(start: Float, end: Float, t: Float) = start + (end - start) * t
    fun lerpColor(colorA: androidx.compose.ui.graphics.Color, colorB: androidx.compose.ui.graphics.Color, fraction: Float): androidx.compose.ui.graphics.Color {
        val r = lerp(colorA.red, colorB.red, fraction)
        val g = lerp(colorA.green, colorB.green, fraction)
        val b = lerp(colorA.blue, colorB.blue, fraction)
        val a = lerp(colorA.alpha, colorB.alpha, fraction)
        return androidx.compose.ui.graphics.Color(r, g, b, a)
    }
    fun File.md5(): String {
        val digest = MessageDigest.getInstance("MD5")
        inputStream().buffered().use {
            val buffer = ByteArray(8192)
            var bytes = it.read(buffer)
            while (bytes >= 0) {
                digest.update(buffer, 0, bytes)
                bytes = it.read(buffer)
            }
        }
        return digest.digest().joinToString("") { "%02x".format(it) }
    }

    val themeColors = listOf(
        primaryColor,
        secondaryColor,
        accentYellow,
        accentRed,
        accentGreen,
        accentOrange,
        accentPink
    )
}