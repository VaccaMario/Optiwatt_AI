package com.example.optiwatt_ai

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class RingChartView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    var percent: Float = 0.65f // da 0.0f a 1.0f

    private val backgroundPaint = Paint().apply {
        color = Color.LTGRAY
        style = Paint.Style.STROKE
        strokeWidth = 60f
        isAntiAlias = true
    }

    private val ringPaint = Paint().apply {
        color = Color.parseColor("#86B1A4")
        style = Paint.Style.STROKE
        strokeWidth = 60f
        isAntiAlias = true
        strokeCap = Paint.Cap.ROUND
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val size = Math.min(width, height) * 0.8f
        val left = (width - size) / 2
        val top = (height - size) / 2
        val right = left + size
        val bottom = top + size

        val oval = android.graphics.RectF(left, top, right, bottom)


        canvas.drawArc(oval, 0f, 360f, false, backgroundPaint)


        canvas.drawArc(oval, -90f, percent * 360f, false, ringPaint)
    }
}
// codice preso da github e modificato