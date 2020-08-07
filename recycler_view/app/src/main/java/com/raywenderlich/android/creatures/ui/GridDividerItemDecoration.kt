package com.raywenderlich.android.creatures.ui

import android.graphics.Canvas
import android.graphics.Paint
import androidx.recyclerview.widget.RecyclerView

class GridDividerItemDecoration(
    color: Int,
    private val spanCount: Int,
    private val heightInPixels: Int,
    private val widthInPixels: Int
) : RecyclerView.ItemDecoration() {

    private val paint = Paint()

    init {
        paint.color = color
        paint.isAntiAlias = true
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)

        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams

            val top = child.top - params.topMargin
            val bottom = child.bottom + params.bottomMargin
            val left = child.left - params.leftMargin
            val right = child.right + params.rightMargin


            c.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), (top + heightInPixels).toFloat(), paint)
            if ((i + 1) % spanCount != 0) {
                c.drawRect(right.toFloat(), top.toFloat(), (right + widthInPixels).toFloat(), bottom.toFloat(), paint)
            }
        }
    }
}