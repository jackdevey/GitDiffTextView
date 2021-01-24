package com.alorma.diff.lib

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
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        init()
    }

    private fun init() {
        isInEditMode
        when (context.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
            Configuration.UI_MODE_NIGHT_YES -> {
                additionColor = Color.parseColor("#2f9e2f")
                deletionColor = Color.parseColor("#cf2929")
            }
            Configuration.UI_MODE_NIGHT_NO -> {
                additionColor = Color.parseColor("#CCFFCC")
                deletionColor = Color.parseColor("#FFDDDD")
            }
        }
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