package com.bizarrecoding.sleeptracker.ui.stats

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import androidx.core.view.children
import androidx.core.view.get
import androidx.recyclerview.widget.RecyclerView
import com.bizarrecoding.sleeptracker.R

class DividerDecorator(ctx: Context) : RecyclerView.ItemDecoration() {
    private val divider: Drawable

    init{
        val bgColor = ctx.resources.getColor(R.color.backgroundGray, null)
        divider = ColorDrawable(bgColor)
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        val left = parent.paddingLeft
        val right = parent.width-parent.paddingRight
        for ( i in 0 until parent.childCount-1){
            val child = parent[i]
            val top = child.bottom
            divider.bounds = Rect(left,top,right,top+5)
            divider.draw(c)
        }

    }
}