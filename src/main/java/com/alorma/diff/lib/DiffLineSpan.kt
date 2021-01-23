package com.alorma.diff.lib

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Region
import android.text.style.LineBackgroundSpan

/**
 * Line span used to highlight a diff change
 */
class DiffLineSpan(private val color: Int, private val padding: Int) : LineBackgroundSpan {
    override fun drawBackground(c: Canvas, p: Paint, left: Int, right: Int, top: Int, baseline: Int,
                                bottom: Int, text: CharSequence, start: Int, end: Int, lnum: Int) {
        // expand canvas bounds by padding
        val clipBounds = c.clipBounds
        clipBounds.inset(-padding, 0)
        c.clipRect(clipBounds, Region.Op.REPLACE)
        val paintColor = p.color
        p.color = color
        mTmpRect[left - padding, top, right + padding] = bottom
        c.drawRect(mTmpRect, p)
        p.color = paintColor
    }

    companion object {
        private val mTmpRect = Rect()
    }
}