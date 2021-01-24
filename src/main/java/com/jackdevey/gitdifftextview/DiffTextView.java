package com.jackdevey.gitdifftextview;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.BackgroundColorSpan;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Bernat on 22/12/2014.
 */
public class DiffTextView extends TextView {

    private int additionColor;
    private int deletionColor;
    private boolean showInfo;
    private int maxLines = -1;

    public DiffTextView(Context context) {
        this(context, null);
    }

    public DiffTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, R.attr.diff_theme);
    }

    public DiffTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public DiffTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        isInEditMode();

        final TypedArray array = context.obtainStyledAttributes(attrs,
                R.styleable.DiffTextViewStyle, defStyleAttr, 0);
        this.additionColor = array.getColor(R.styleable.DiffTextViewStyle_diff_addition_color, Color.parseColor("#CCFFCC"));
        this.deletionColor = array.getColor(R.styleable.DiffTextViewStyle_diff_deletion_color, Color.parseColor("#FFDDDD"));
        this.showInfo = array.getBoolean(R.styleable.DiffTextViewStyle_diff_show_diff_info, false);
        array.recycle();
    }

    public void setColor(int addColor, int delColor) {
        additionColor = addColor;
        deletionColor = delColor;
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        if (!TextUtils.isEmpty(text)) {
            String diff = text.toString();
            String[] split = diff.split("\\r?\\n|\\r");

            if (split.length > 0) {
                SpannableStringBuilder builder = new SpannableStringBuilder();

                int lines;
                if (maxLines > 0) {
                    lines = Math.min(maxLines, split.length);
                } else {
                    lines = split.length;
                }

                for (int i = 0; i < lines; i++) {
                    String token = split[i];
                    if (!token.startsWith("@@") || showInfo) {
                        if (i < (lines - 1)) {
                            token = token.concat("\n");
                        }

                        char firstChar = token.charAt(0);

                        int color = 0;
                        if (firstChar == '+') {
                            color = additionColor;
                        } else if (firstChar == '-') {
                            color = deletionColor;
                        }

                        SpannableString spannableDiff = new SpannableString(token);
                        if (color == additionColor || color == deletionColor) {
                            BackgroundColorSpan span = new BackgroundColorSpan(color);
                            spannableDiff.setSpan(span, 0, token.length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
                        }

                        builder.append(spannableDiff);
                    }
                }

                super.setText(builder, type);
            }
        } else {
            super.setText(text, type);
        }
    }

    @Override
    public int getMaxLines() {
        return maxLines;
    }

    @Override
    public void setMaxLines(int maxlines) {
        this.maxLines = maxlines;
    }
}