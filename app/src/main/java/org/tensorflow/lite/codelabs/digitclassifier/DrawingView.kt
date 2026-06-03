package org.tensorflow.lite.codelabs.digitclassifier

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class DrawingView(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {

    private var paint = Paint().apply {
        color = Color.BLACK
        style = Paint.Style.STROKE
        strokeWidth = 60f
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
    }

    private var bitmap: Bitmap? = null
    private var canvas: Canvas? = null
    private var lastX = 0f
    private var lastY = 0f

    init {
        setBackgroundColor(Color.WHITE)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        if (w > 0 && h > 0) {
            bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
            canvas = Canvas(bitmap!!)
            canvas!!.drawColor(Color.WHITE)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                lastX = x
                lastY = y
                canvas?.drawPoint(x, y, paint)
            }
            MotionEvent.ACTION_MOVE -> {
                canvas?.drawLine(lastX, lastY, x, y, paint)
                lastX = x
                lastY = y
            }
        }

        invalidate()
        return true
    }

    override fun onDraw(canvas: Canvas) {
        bitmap?.let {
            canvas.drawBitmap(it, 0f, 0f, null)
        }
    }

    fun clear() {
        bitmap?.eraseColor(Color.WHITE)
        invalidate()
    }

    /**
     * Возвращает КОПИЮ текущего рисунка.
     * Это предотвращает случайное изменение оригинала при распознавании.
     */
    fun getBitmap(): Bitmap? {
        return bitmap?.copy(Bitmap.Config.ARGB_8888, false)
    }

    /**
     * Проверяет, есть ли на рисунке что-то нарисованное.
     */
    fun isDrawingNotEmpty(): Boolean {
        val bmp = bitmap ?: return false

        for (y in 0 until bmp.height step 20) {
            for (x in 0 until bmp.width step 20) {
                val pixel = bmp.getPixel(x, y)
                val r = (pixel shr 16 and 0xFF)
                val g = (pixel shr 8 and 0xFF)
                val b = (pixel and 0xFF)
                val brightness = (r + g + b) / 3.0f
                if (brightness < 200) {
                    return true
                }
            }
        }
        return false
    }
}