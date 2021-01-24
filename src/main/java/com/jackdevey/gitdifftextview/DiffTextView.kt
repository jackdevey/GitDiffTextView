package com.jackdevey.gitdifftextview

import android.annotation.TargetApi
import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.os.Build
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import android.util.AttributeSet
import android.widget.TextView

class DiffTextView : TextView {
    private var additionColor = 0
    private var deletionColor = 0
    private var infoColor = 0
    private var additionTextColor = 0
    private var deletionTextColor = 0
    private var infoTextColor = 0
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
        infoColor = array.getColor(R.styleable.DiffTextViewStyle_diff_info_color, Color.parseColor("#EEEEEE"))
        additionTextColor = array.getColor(R.styleable.DiffTextViewStyle_diff_addition_text_color, Color.TRANSPARENT)
        deletionTextColor = array.getColor(R.styleable.DiffTextViewStyle_diff_deletion_text_color, Color.TRANSPARENT)
        infoTextColor = array.getColor(R.styleable.DiffTextViewStyle_diff_info_text_color, Color.TRANSPARENT)
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
                        var color = Color.TRANSPARENT
                        var textColor = Color.TRANSPARENT
                        if (firstChar == '+') {
                            color = additionColor
                            textColor = additionTextColor
                        } else if (firstChar == '-') {
                            color = deletionColor
                            textColor = deletionTextColor
                        } else if (token.startsWith("@@")) {
                            color = infoColor
                            textColor = infoTextColor
                        }
                        val spannableDiff = SpannableString(token)
                        // Span for line color (where transparent is considered as default)
                        if (color != Color.TRANSPARENT) {
                            val span = DiffLineSpan(color, paddingLeft)
                            spannableDiff.setSpan(span, 0, token.length, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
                        }
                        // Span for text color (where transparent is considered as default)
                        if (textColor != Color.TRANSPARENT) {
                            val span = ForegroundColorSpan(textColor)
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