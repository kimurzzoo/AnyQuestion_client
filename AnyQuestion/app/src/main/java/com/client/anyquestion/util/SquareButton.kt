package com.client.anyquestion.util

import android.content.Context
import android.util.AttributeSet

class SquareButton : androidx.appcompat.widget.AppCompatButton{

    constructor(context : Context, attrs : AttributeSet) : super(context,attrs)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = MeasureSpec.getSize(widthMeasureSpec)
        setMeasuredDimension(width, width)
    }
}