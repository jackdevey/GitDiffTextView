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

/**
 * Created by Bernat on 22/12/2014.
 * Converted to kotlin & adapted for dark mode by Jack Devey 24/01/2021.
 */
class TextView : TextView {
    private var additionColor = 0
    private var deletionColor = 0
    private var showInfo = false
    private var maxLines = -1

    @JvmOverloads
    constructor(context: Context, attrs: AttributeSet? = null) : super(context, attrs) {
        init(context, attrs, R.attr.diff_theme)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs, defStyleAttr)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        init(context, attrs, defStyleAttr)
    }

    private fun init(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        isInEditMode
        val array = context.obtainStyledAttributes(attrs,
                R.styleable.DiffTextViewStyle, defStyleAttr, 0)
        when (context.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
            Configuration.UI_MODE_NIGHT_YES -> {
                additionColor = array.getColor(R.styleable.DiffTextViewStyle_diff_addition_color, Color.parseColor("#2f9e2f"))
                deletionColor = array.getColor(R.styleable.DiffTextViewStyle_diff_deletion_color, Color.parseColor("#cf2929"))
            }
            Configuration.UI_MODE_NIGHT_NO -> {
                additionColor = array.getColor(R.styleable.DiffTextViewStyle_diff_addition_color, Color.parseColor("#CCFFCC"))
                deletionColor = array.getColor(R.styleable.DiffTextViewStyle_diff_deletion_color, Color.parseColor("#FFDDDD"))
            }
        }
        showInfo = array.getBoolean(R.styleable.DiffTextViewStyle_diff_show_diff_info, false)
        array.recycle()
    }

    override fun setText(text: CharSequence, type: BufferType) {
        if (!TextUtils.isEmpty(text)) {
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
                    if (!token.startsWith("@@") || showInfo) {
                        if (i < lines - 1) {
                            token = """
                                $token
                                
                                """.trimIndent()
                        }
                        val firstChar = token[0]
                        var color = 0
                        if (firstChar == '+') {
                            color = additionColor
                        } else if (firstChar == '-') {
                            color = deletionColor
                        }
                        val spannableDiff = SpannableString(token)
                        if (color == additionColor || color == deletionColor) {
                            val span = BackgroundColorSpan(color)
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