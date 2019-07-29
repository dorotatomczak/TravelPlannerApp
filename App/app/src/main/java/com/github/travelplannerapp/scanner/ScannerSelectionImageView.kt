package com.github.travelplannerapp.scanner

import android.content.Context
import android.graphics.*
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.ImageView
import com.github.travelplannerapp.R
import java.util.ArrayList

//TODO [Dorota] Add info on repo about https://github.com/jbttn/SimpleDocumentScanner-Android, MIT license

class ScannerSelectionImageView : ImageView {

    companion object {
        const val DEFAULT_BORDER_COLOR = R.color.colorAccent
        const val DEFAULT_BACKGROUND_COLOR = -0x80000000
        const val DEFAULT_CIRCLE_RADIUS = 30f
        const val DEFAULT_PADDING_TOP = 100f
        const val DEFAULT_PADDING_BOTTOM = 150f
        const val DEFAULT_PADDING_VERTICAL = 100f
    }

    private var circleRadius: Float? = null
    private var paddingTop: Float? = null
    private var paddingBottom: Float? = null
    private var paddingVertical: Float? = null

    private var backgroundPaint: Paint = Paint()
    private var borderPaint: Paint = Paint()
    private var circlePaint: Paint = Paint()
    private var selectionPath: Path = Path()
    private var backgroundPath: Path = Path()

    private var upperLeftPoint: PointF? = null
    private var upperRightPoint: PointF? = null
    private var lowerLeftPoint: PointF? = null
    private var lowerRightPoint: PointF? = null
    private var lastTouchedPoint: PointF? = null

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {

        val attributes = context.obtainStyledAttributes(attrs, R.styleable.ScannerSelectionImageView)

        backgroundPaint.color = DEFAULT_BACKGROUND_COLOR

        borderPaint.color = attributes.getColor(R.styleable.ScannerSelectionImageView_borderColor,
                ContextCompat.getColor(context, DEFAULT_BORDER_COLOR))
        borderPaint.isAntiAlias = true
        borderPaint.style = Paint.Style.STROKE
        borderPaint.strokeWidth = 8f

        circlePaint = borderPaint

        circleRadius = attributes.getFloat(R.styleable.ScannerSelectionImageView_circleRadius,
                DEFAULT_CIRCLE_RADIUS)

        paddingTop = attributes.getFloat(R.styleable.ScannerSelectionImageView_paddingTop,
        DEFAULT_PADDING_TOP)
        paddingBottom = attributes.getFloat(R.styleable.ScannerSelectionImageView_paddingBottom,
                DEFAULT_PADDING_BOTTOM)
        paddingVertical = attributes.getFloat(R.styleable.ScannerSelectionImageView_paddingVertical,
                DEFAULT_PADDING_VERTICAL)

        attributes.recycle()
    }

    private fun isAnyPointNull(): Boolean{
        if (upperLeftPoint == null || upperRightPoint == null || lowerRightPoint == null || lowerLeftPoint == null) {
            return true
        }
        return false
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        if (isAnyPointNull()) {
            setDefaultSelection()
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (isAnyPointNull()) return

        selectionPath.reset()
        selectionPath.fillType = Path.FillType.EVEN_ODD
        selectionPath.moveTo(upperLeftPoint!!.x, upperLeftPoint!!.y)
        selectionPath.lineTo(upperRightPoint!!.x, upperRightPoint!!.y)
        selectionPath.lineTo(lowerRightPoint!!.x, lowerRightPoint!!.y)
        selectionPath.lineTo(lowerLeftPoint!!.x, lowerLeftPoint!!.y)
        selectionPath.close()

        backgroundPath.reset()
        backgroundPath.fillType = Path.FillType.EVEN_ODD
        backgroundPath.addRect(0f, 0f, width.toFloat(), height.toFloat(), Path.Direction.CW)
        backgroundPath.addPath(selectionPath)

        canvas.drawPath(backgroundPath, backgroundPaint)
        canvas.drawPath(selectionPath, borderPaint)

        canvas.drawCircle(upperLeftPoint!!.x, upperLeftPoint!!.y, circleRadius!!, circlePaint)
        canvas.drawCircle(upperRightPoint!!.x, upperRightPoint!!.y, circleRadius!!, circlePaint)
        canvas.drawCircle(lowerRightPoint!!.x, lowerRightPoint!!.y, circleRadius!!, circlePaint)
        canvas.drawCircle(lowerLeftPoint!!.x, lowerLeftPoint!!.y, circleRadius!!, circlePaint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        super.onTouchEvent(event)

        if (isAnyPointNull()) return true

        when (event.actionMasked) {
            MotionEvent.ACTION_MOVE -> {
                var isConvex = false
                val eventPoint = PointF(event.x, event.y)

                // Determine if the shape will still be convex when we apply the users next drag
                if (lastTouchedPoint === upperLeftPoint) {
                    isConvex = isConvexQuadrilateral(eventPoint, upperRightPoint!!, lowerRightPoint!!, lowerLeftPoint!!)
                } else if (lastTouchedPoint === upperRightPoint) {
                    isConvex = isConvexQuadrilateral(upperLeftPoint!!, eventPoint, lowerRightPoint!!, lowerLeftPoint!!)
                } else if (lastTouchedPoint === lowerRightPoint) {
                    isConvex = isConvexQuadrilateral(upperLeftPoint!!, upperRightPoint!!, eventPoint, lowerLeftPoint!!)
                } else if (lastTouchedPoint === lowerLeftPoint) {
                    isConvex = isConvexQuadrilateral(upperLeftPoint!!, upperRightPoint!!, lowerRightPoint!!, eventPoint)
                }

                if (isConvex && lastTouchedPoint != null) {
                    lastTouchedPoint?.set(event.x, event.y)
                }
            }
            MotionEvent.ACTION_DOWN -> {
                if (event.x < upperLeftPoint!!.x + paddingVertical!! && event.x > upperLeftPoint!!.x - paddingVertical!! &&
                        event.y < upperLeftPoint!!.y + paddingBottom!! && event.y > upperLeftPoint!!.y - paddingTop!!) {
                    lastTouchedPoint = upperLeftPoint
                } else if (event.x < upperRightPoint!!.x + paddingVertical!! && event.x > upperRightPoint!!.x - paddingVertical!! &&
                        event.y < upperRightPoint!!.y + paddingBottom!! && event.y > upperRightPoint!!.y - paddingTop!!) {
                    lastTouchedPoint = upperRightPoint
                } else if (event.x < lowerRightPoint!!.x + paddingVertical!! && event.x > lowerRightPoint!!.x - paddingVertical!! &&
                        event.y < lowerRightPoint!!.y + paddingTop!! && event.y > lowerRightPoint!!.y - paddingBottom!!) {
                    lastTouchedPoint = lowerRightPoint
                } else if (event.x < lowerLeftPoint!!.x + paddingVertical!! && event.x > lowerLeftPoint!!.x - paddingVertical!! &&
                        event.y < lowerLeftPoint!!.y + paddingTop!! && event.y > lowerLeftPoint!!.y - paddingBottom!!) {
                    lastTouchedPoint = lowerLeftPoint
                } else {
                    lastTouchedPoint = null
                }
            }
        }
        invalidate()
        return true
    }

    /**
     * Translate the given point from view coordinates to image coordinates
     *
     * @param point The point to translate
     * @return The translated point
     */
    private fun viewPointToImagePoint(point: PointF): PointF? {
        val matrix = Matrix()
        imageMatrix.invert(matrix)
        return mapPointToMatrix(point, matrix)
    }

    /**
     * Translate the given point from image coordinates to view coordinates
     *
     * @param imgPoint The point to translate
     * @return The translated point
     */
    private fun imagePointToViewPoint(imgPoint: PointF): PointF? {
        return mapPointToMatrix(imgPoint, imageMatrix)
    }

    /**
     * Helper to map a given PointF to a given Matrix
     *
     * NOTE: http://stackoverflow.com/questions/19958256/custom-imageview-imagematrix-mappoints-and-invert-inaccurate
     *
     * @param point The point to map
     * @param matrix The matrix
     * @return The mapped point
     */
    private fun mapPointToMatrix(point: PointF, matrix: Matrix): PointF? {
        val points = floatArrayOf(point.x, point.y)
        matrix.mapPoints(points)
        return if (points.size > 1) {
            PointF(points[0], points[1])
        } else {
            null
        }
    }

    /**
     * Returns a list of points representing the quadrilateral.  The points are converted to represent
     * the location on the image itself, not the view.
     *
     * @return A list of points translated to map to the image
     */
    fun getPoints(): List<PointF?> {
        val list = ArrayList<PointF?>()
        if (!isAnyPointNull()){
            list.add(viewPointToImagePoint(upperLeftPoint!!))
            list.add(viewPointToImagePoint(upperRightPoint!!))
            list.add(viewPointToImagePoint(lowerRightPoint!!))
            list.add(viewPointToImagePoint(lowerLeftPoint!!))
        }
        return list
    }

    /**
     * Set the points in order to control where the selection will be drawn.  The points should
     * be represented in regards to the image, not the view.  This method will translate from image
     * coordinates to view coordinates.
     *
     * NOTE: Calling this method will invalidate the view
     *
     * @param points A list of points. Passing null will set the selector to the default selection.
     */
    fun setPoints(points: List<PointF>?) {
        if (points != null) {
            upperLeftPoint = imagePointToViewPoint(points[0])
            upperRightPoint = imagePointToViewPoint(points[1])
            lowerRightPoint = imagePointToViewPoint(points[2])
            lowerLeftPoint = imagePointToViewPoint(points[3])
        } else {
            setDefaultSelection()
        }

        invalidate()
    }

    /**
     * Gets the coordinates representing a rectangles corners.
     *
     * The order of the points is
     * 0------->1
     * ^        |
     * |        v
     * 3<-------2
     *
     * @param rect The rectangle
     * @return An array of 8 floats
     */
    private fun getCornersFromRect(rect: RectF): FloatArray {
        return floatArrayOf(rect.left, rect.top, rect.right, rect.top, rect.right, rect.bottom, rect.left, rect.bottom)
    }

    /**
     * Sets the points into a default state (A rectangle following the image view frame with
     * padding)
     */
    private fun setDefaultSelection() {
        val rect = RectF(paddingVertical!!, paddingTop!!, width - paddingVertical!!, height - paddingBottom!!)

        val pts = getCornersFromRect(rect)
        upperLeftPoint = PointF(pts[0], pts[1])
        upperRightPoint = PointF(pts[2], pts[3])
        lowerRightPoint = PointF(pts[4], pts[5])
        lowerLeftPoint = PointF(pts[6], pts[7])
    }

    /**
     * Determine if the given points are a convex quadrilateral.  This is used to prevent the
     * selection from being dragged into an invalid state.
     *
     * NOTE: http://stackoverflow.com/questions/9513107/find-if-4-points-form-a-quadrilateral
     *
     * @param ul The upper left point
     * @param ur The upper right point
     * @param lr The lower right point
     * @param ll The lower left point
     * @return True is the quadrilateral is convex
     */
    private fun isConvexQuadrilateral(ul: PointF, ur: PointF, lr: PointF, ll: PointF): Boolean {
        val point1 = subtractPoints(ur, ll)
        val point2 = subtractPoints(ul, lr)

        val crossProduct = crossProduct(point1, point2).toDouble()
        val t = crossProduct(subtractPoints(lr, ll), point2) / crossProduct
        val u = crossProduct(subtractPoints(lr, ll), point1) / crossProduct

        return !(t < 0 || t > 1.0 || u < 0 || u > 1.0)
    }

    private fun subtractPoints(p1: PointF, p2: PointF): PointF {
        return PointF(p1.x - p2.x, p1.y - p2.y)
    }

    private fun crossProduct(p1: PointF, p2: PointF): Float {
        return p1.x * p2.y - p1.y * p2.x
    }
}