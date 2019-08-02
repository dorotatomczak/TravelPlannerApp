package com.github.travelplannerapp.scanner

import org.opencv.core.*
import org.opencv.imgcodecs.Imgcodecs.imread
import org.opencv.imgproc.Imgproc.*
import java.util.*


object Scanner {

    /**
     * Attempt to find the four corner points for the largest contour in the image.
     *
     * @return A list of points, or null if a valid rectangle cannot be found.
     */
    fun findCorners(photoPath: String): Array<Point>? {
        var corners: Array<Point>? = null

        val photo = imread(photoPath)
        val edges = detectEdges(photo)
        val largestContour = findLargestContour(edges)

        largestContour?.let {
            corners = sortCorners(largestContour.toArray())
            it.release()}

        edges.release()
        photo.release()

        return corners
    }

    private fun downScalePhoto(photoPath: String, percent: Int): Mat {
        val photo = imread(photoPath)
        val resizedPhoto = Mat()
        val scaleSize = Size((photo.width() * percent) / 100.0, (photo.height() * percent) / 100.0)
        resize(photo, resizedPhoto, scaleSize, 0.0, 0.0, INTER_AREA)
        return resizedPhoto
    }

    /**
     * Detect the edges in the given Mat
     * @param src A valid Mat object
     * @return A Mat processed to find edges
     */
    private fun detectEdges(photo: Mat): Mat {
        val edges = Mat(photo.size(), CvType.CV_8UC1)
        cvtColor(photo, edges, COLOR_RGB2GRAY, 4)
        Canny(edges, edges, 80.0, 100.0)
        return edges
    }

    /**
     * Find the largest 4 point contour in the given Mat.
     *
     * @param src A valid Mat
     * @return The largest contour as a Mat
     */
    private fun findLargestContour(edges: Mat): MatOfPoint2f? {
        val contours = ArrayList<MatOfPoint>()
        findContours(edges, contours, Mat(), RETR_LIST, CHAIN_APPROX_SIMPLE)

        contours.sortWith(Comparator { o1, o2 ->
            val area1 = contourArea(o1)
            val area2 = contourArea(o2)
            (area2 - area1).toInt()
        })
        if (contours.size > 5) contours.subList(4, contours.size - 1).clear()

        var largest: MatOfPoint2f? = null
        for (contour in contours) {
            val approx = MatOfPoint2f()
            val c = MatOfPoint2f()
            contour.convertTo(c, CvType.CV_32FC2)
            approxPolyDP(c, approx, arcLength(c, true) * 0.02, true)

            if (approx.total() == 4L && contourArea(contour) > 150) {
                largest = approx
                break
            }
        }

        return largest
    }

    /**
     * Sort the corners
     *
     * The order of the points after sorting:
     * 0------->1
     * ^        |
     * |        v
     * 3<-------2
     *
     * NOTE:
     * Based off of http://www.pyimagesearch.com/2014/08/25/4-point-opencv-getperspective-transform-example/
     *
     * @param src The points to sort
     * @return An array of sorted points
     */
    private fun sortCorners(src: Array<Point>): Array<Point>? {
        val srcPoints = ArrayList(listOf(*src))
        val sortedCorners = arrayOf<Point?>(null, null, null, null)

        val sumComparator = Comparator<Point> { lhs, rhs -> java.lang.Double.valueOf(lhs.y + lhs.x).compareTo(rhs.y + rhs.x) }
        val differenceComparator = Comparator<Point> { lhs, rhs -> java.lang.Double.valueOf(lhs.y - lhs.x).compareTo(rhs.y - rhs.x) }

        sortedCorners[0] = Collections.min(srcPoints, sumComparator)        // Upper left has the minimal sum
        sortedCorners[2] = Collections.max(srcPoints, sumComparator)        // Lower right has the maximal sum
        sortedCorners[1] = Collections.min(srcPoints, differenceComparator) // Upper right has the minimal difference
        sortedCorners[3] = Collections.max(srcPoints, differenceComparator) // Lower left has the maximal difference

        if (sortedCorners.contains(null)) {
            return null
        } else {
            return arrayOf(sortedCorners[0]!!, sortedCorners[1]!!, sortedCorners[2]!!, sortedCorners[3]!!)
        }
    }

}