package com.example.app.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import ru.practice.homeworks.R
import kotlin.math.*

class ChartView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    init {
        isClickable = true
    }

    private var sectorCount = 4
    private var colors = listOf(Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW)
    private var activeIndex = -1

    private val paint = Paint()
    private val textPaint = Paint()
    private val rectF = RectF()

    private var centerX = 0f
    private var centerY = 0f
    private var radius = 0f
    private var innerRadius = 0f

    private var sectorAngle = 0f


    private var downX = 0f
    private var downY = 0f
    private val touchSlop by lazy { ViewConfiguration.get(context).scaledTouchSlop }

    fun setup(sectorCount: Int, colors: List<Int>) {
        require(colors.size == sectorCount) {context.getString(R.string.list_size) }
        for (i in 0 until colors.size) {
            if (colors[i] == colors[(i + 1) % colors.size]) {
                throw IllegalArgumentException(context.getString(R.string.colors))
            }
        }
        this.sectorCount = sectorCount
        this.colors = colors
        activeIndex = -1
        sectorAngle = 360f / sectorCount
        invalidate()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        val padding = minOf(w, h) * 0.1f
        centerX = w / 2f
        centerY = h / 2f
        radius = minOf(w, h) / 2f - padding
        innerRadius = radius * 0.6f

        rectF.set(
            centerX - radius,
            centerY - radius,
            centerX + radius,
            centerY + radius
        )

        textPaint.apply {
            textSize = radius * 0.3f
            textAlign = Paint.Align.CENTER
            color = Color.BLACK
            isAntiAlias = true
        }

        sectorAngle = 360f / sectorCount
    }

    override fun onDraw(canvas: Canvas) {
        val startAngle = 180f
        for (i in 0 until sectorCount) {
            val currentColor = if (i == activeIndex) getLighterColor(colors[i]) else colors[i]
            drawSingleSector(canvas, i, startAngle, currentColor)
        }

        for (i in 0 until sectorCount){
            val currentColor = if (i == activeIndex) getLighterColor(colors[i]) else colors[i]
            drawCircles(canvas,i,startAngle,currentColor)
        }

        canvas.drawText("$sectorCount", centerX, centerY + textPaint.textSize / 2, textPaint)
    }

    private fun drawCircles(canvas: Canvas, index: Int, startAngle: Float, currentColor: Int){

        paint.apply {
            reset()
            style = Paint.Style.STROKE
            strokeCap = Paint.Cap.BUTT
            strokeWidth = radius - innerRadius
            color = currentColor
            isAntiAlias = true
        }

        val angle = startAngle + index * sectorAngle

        val endAngleRad = Math.toRadians((angle + sectorAngle).toDouble()).toFloat()

        val x = centerX + radius * cos(endAngleRad.toDouble()).toFloat()
        val y = centerY + radius * sin(endAngleRad.toDouble()).toFloat()

        canvas.drawCircle(x, y, 0.5f, paint)
    }

    private fun drawSingleSector(canvas: Canvas, index: Int, startAngle: Float, currentColor: Int) {
        val angle = startAngle + index * sectorAngle

        paint.apply {
            reset()
            style = Paint.Style.STROKE
            strokeCap = Paint.Cap.BUTT
            strokeWidth = radius - innerRadius
            color = currentColor
            isAntiAlias = true
        }


        canvas.drawArc(
            rectF,
            angle,
            sectorAngle,
            false,
            paint
        )

    }

    private fun getLighterColor(color: Int): Int {
        val hsv = FloatArray(3)
        Color.colorToHSV(color, hsv)
        hsv[2] = minOf(hsv[2] * 1.3f, 1f)
        return Color.HSVToColor(hsv)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                downX = event.x
                downY = event.y
            }
            MotionEvent.ACTION_UP -> {
                val dx = event.x - downX
                val dy = event.y - downY
                if (sqrt((dx * dx + dy * dy).toDouble()) < touchSlop) {
                    handleTap(event.x, event.y)
                    return true
                }
            }
        }
        return super.onTouchEvent(event)
    }

    private fun handleTap(x: Float, y: Float) {
        val dx = x - centerX
        val dy = y - centerY
        val distance = sqrt(dx * dx + dy * dy)

        if (distance > radius || distance < innerRadius) {
            activeIndex = -1
        } else {
            var angle = Math.toDegrees(atan2(dy.toDouble(), dx.toDouble())).toFloat()
            if (angle < 0) angle += 360f

            angle = (angle - +180 + 360) % 360

            activeIndex = (angle / sectorAngle).toInt()
            if (activeIndex < 0 || activeIndex >= sectorCount) {
                activeIndex = -1
            }
        }
        invalidate()
    }
}