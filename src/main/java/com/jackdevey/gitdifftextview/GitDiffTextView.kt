package com.jackdevey.gitdifftextview

import android.annotation.TargetApi
import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.os.Build
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.TextUtils
import android.text.style.BackgroundColorSpan
import android.util.AttributeSet
import android.widget.TextView
import com.jackdevey.gitdifftextview.R

/**
 * Created by Bernat on 22/12/2014.
 */
class GitDiffTextView : TextView {
    private var additionColor = 0
    private var deletionColor = 0
    private var maxLines = -1

    constructor(context: Context?) : super(context) {
        init(Color.parseColor("#CCFFCC"), Color.parseColor("#FFDDDD"))
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init(Color.parseColor("#CCFFCC"), Color.parseColor("#FFDDDD"))
    }

    constructor(context: Context?, attrs: AttributeSet?, addColor: String, delColor: String) : super(context, attrs) {
        init(Color.parseColor(addColor), Color.parseColor(delColor))
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(Color.parseColor("#CCFFCC"), Color.parseColor("#FFDDDD"))
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        init(Color.parseColor("#CCFFCC"), Color.parseColor("#FFDDDD"))
    }

    private fun init(addColor: Int, delColor: Int) {
        isInEditMode
        additionColor = addColor
        deletionColor = delColor
    }

    override fun setText(text: CharSequence, type: BufferType) {
        if (!TextUtils.isEmpty(text) && type == BufferType.NORMAL) {
            val diff = text.toString()
            val split = diff.split("\\r?\\n|\\r").toTypedArray()
            if (split.size > 0) {
                val builder = SpannableStringBuilder()
                val lines: Int
                lines = if (maxLines > 0) {
                    Math.min(maxLines, split.size)
                } else {
                    split.size
                }
                for (i in 0 until lines) {
                    var token = split[i]
                    if (!token.startsWith("@@")) {
                        if (i < lines - 1) {
                            token = """
                                $token
                                
                                """.trimIndent()
                        }
                        var span: BackgroundColorSpan? = null
                        var spannableDiff = SpannableString(token)
                        val firstChar = token[0]
                        var color = 0
                        if (firstChar == '+') {
                            color = additionColor
                        } else if (firstChar == '-') {
                            color = deletionColor
                        }
                        spannableDiff = SpannableString(token)
                        if (color == additionColor || color == deletionColor) {
                            span = BackgroundColorSpan(color)
                            spannableDiff.setSpan(span, 0, token.length, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
                        }
                        builder.append(spannableDiff)
                    }
                }
                super.setText(builder, type)
            }
        } else {
            super.setText(text, type)
        }
    }

    override fun getMaxLines(): Int {
        return maxLines
    }

    override fun setMaxLines(maxlines: Int) {
        maxLines = maxlines
    }
}