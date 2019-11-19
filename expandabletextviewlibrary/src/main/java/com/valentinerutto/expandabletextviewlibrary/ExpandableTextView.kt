package com.valentinerutto.expandabletextviewlibrary

import android.content.Context
import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.util.AttributeSet
import android.widget.TextView
import androidx.core.content.ContextCompat

class ExpandableTextView : TextView {
    private val DEFAULT_TRIM_LENGTH = 50
    private val ELLIPSIS = " ... MORE"
    private var originalText: CharSequence? = null
    private var trimmedText: CharSequence? = null
    private var bufferType: TextView.BufferType? = null
    private var trim = true
    private var trimLength: Int = 0


    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initViews(context, attrs)
    }

    private fun initViews(context: Context, attrs: AttributeSet) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ExpandableTextView)
        this.trimLength =
            typedArray.getInt(R.styleable.ExpandableTextView_trimLength, DEFAULT_TRIM_LENGTH)
        typedArray.recycle()

        setOnClickListener {
            trim = !trim
            setText()
            requestFocusFromTouch()
        }
    }

    private fun setText() {
        super.setText(getDisplayableText(), bufferType)
    }

    private fun getDisplayableText(): CharSequence? {
        return if (trim) trimmedText else originalText
    }

    override fun setText(text: CharSequence, type: TextView.BufferType) {
        originalText = text
        trimmedText = getTrimmedText(text)
        bufferType = type
        setText()
    }

    private fun getTrimmedText(text: CharSequence?): CharSequence? {
        return if (originalText != null && originalText!!.length > trimLength) {
            val spanString = SpannableString(ELLIPSIS)
            spanString.setSpan(
                ForegroundColorSpan(ContextCompat.getColor(context,R.color.colorAccent)),
                0, spanString.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            SpannableStringBuilder(originalText, 0, trimLength + 1).append(spanString)
        } else {
            originalText
        }
    }

    fun getOriginalText(): CharSequence? {
        return originalText
    }

    fun setTrimLength(trimLength: Int) {
        this.trimLength = trimLength
        trimmedText = getTrimmedText(originalText)
        setText()
    }

    fun getTrimLength(): Int {
        return trimLength
    }
}
