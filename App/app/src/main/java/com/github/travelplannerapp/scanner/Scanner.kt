package com.github.travelplannerapp.scanner

import android.graphics.Bitmap
import android.graphics.PointF
import org.opencv.android.Utils
import org.opencv.core.CvType
import org.opencv.core.Mat
import org.opencv.core.Point
import org.opencv.core.Size
import org.opencv.imgcodecs.Imgcodecs.imread
import org.opencv.imgproc.Imgproc.*
import java.util.*

object Scanner {

    fun cropAndScan(photoPath: String, corners: List<PointF>, scaleRatio: Int): Bitmap? {
        return if (corners.size == 4) {
            val photo = imread(photoPath)
            val convertedCorners = convertPointFsToPoints(corners)
            val transformed = perspectiveTransform(photo, convertedCorners, scaleRatio)
            val scan = applyThreshold(transformed)
            convertMatToBitmap(scan)
        } else null
    }

    private fun convertPointFsToPoints(points: List<PointF>): List<Point> {
        val convertedPoints = ArrayList<Point>()
        points.forEach { convertedPoints.add(Point(it.x.toDouble(), it.y.toDouble())) }
        return convertedPoints
    }

    private fun perspectiveTransform(photo: Mat, pts: List<Point>, ratio: Int): Mat {

        val maxWidth = photo.size().width * 1f / ratio.toFloat()
        val maxHeight = photo.size().height * 1f / ratio.toFloat()

        val resultMat = Mat(maxHeight.toInt(), maxWidth.toInt(), CvType.CV_8UC4)

        val srcMat = Mat(4, 1, CvType.CV_32FC2)
        val dstMat = Mat(4, 1, CvType.CV_32FC2)
        srcMat.put(0, 0, pts[0].x * ratio, pts[0].y * ratio, pts[1].x * ratio, pts[1].y * ratio,
                pts[2].x * ratio, pts[2].y * ratio, pts[3].x * ratio, pts[3].y * ratio)
        dstMat.put(0, 0, 0.0, 0.0, maxWidth, 0.0, maxWidth, maxHeight, 0.0, maxHeight)

        val warpMat = getPerspectiveTransform(srcMat, dstMat)
        warpPerspective(photo, resultMat, warpMat, resultMat.size())

        srcMat.release()
        dstMat.release()
        warpMat.release()

        return resultMat
    }

    private fun applyThreshold(photo: Mat): Mat {
        cvtColor(photo, photo, COLOR_BGR2GRAY)

        GaussianBlur(photo, photo, Size(5.0, 5.0), 0.0)
        adaptiveThreshold(photo, photo, 255.0, ADAPTIVE_THRESH_GAUSSIAN_C, THRESH_BINARY, 11, 2.0)

        return photo
    }

    private fun convertMatToBitmap(input: Mat): Bitmap? {
        val rgb = Mat()
        cvtColor(input, rgb, COLOR_BGR2RGB)
        val bmp = Bitmap.createBitmap(rgb.cols(), rgb.rows(), Bitmap.Config.ARGB_8888)
        Utils.matToBitmap(rgb, bmp)
        return bmp
    }

}