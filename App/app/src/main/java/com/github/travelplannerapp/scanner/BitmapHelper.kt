package com.github.travelplannerapp.scanner

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import androidx.exifinterface.media.ExifInterface
import kotlin.math.roundToInt

object BitmapHelper {

    fun decodeBitmapFromFile(
            path: String,
            reqWidth: Int,
            reqHeight: Int
    ): Bitmap {
        // First decode with inJustDecodeBounds=true to check dimensions
        return BitmapFactory.Options().run {
            inJustDecodeBounds = true
            BitmapFactory.decodeFile(path, this)

            // Calculate inSampleSize
            inSampleSize = calculateInSampleSize(this, reqWidth, reqHeight)

            // Decode bitmap with inSampleSize set
            inJustDecodeBounds = false

            val bitmap = BitmapFactory.decodeFile(path, this)
            rotateBitmapIfRequired(bitmap, path)
        }
    }

    private fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {

        // the height and width of the source image
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1
        if (height > reqHeight || width > reqWidth) {
            // Calculate the ratio of the actual width and height to the target width and height
            val heightRatio = (height.toFloat() / reqHeight.toFloat()).roundToInt()
            val widthRatio = (width.toFloat() / reqWidth.toFloat()).roundToInt()
            // Select the minimum ratio of width and height as the value of inSampleSize, which will ensure the width and height of the final image.
            // Must be greater than or equal to the width and height of the target.
            inSampleSize = if (heightRatio < widthRatio) heightRatio else widthRatio
        }

        return inSampleSize
    }

    private fun rotateBitmapIfRequired(bitmap: Bitmap, path: String): Bitmap {

        val ei = ExifInterface(path)

        when (ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)) {
            ExifInterface.ORIENTATION_ROTATE_90 -> return rotateBitmap(bitmap, 90)
            ExifInterface.ORIENTATION_ROTATE_180 -> return rotateBitmap(bitmap, 180)
            ExifInterface.ORIENTATION_ROTATE_270 -> return rotateBitmap(bitmap, 270)
            else -> return bitmap
        }
    }

    private fun rotateBitmap(bitmap: Bitmap, degree: Int): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(degree.toFloat())
        val rotatedImg = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        bitmap.recycle()
        return rotatedImg
    }
}