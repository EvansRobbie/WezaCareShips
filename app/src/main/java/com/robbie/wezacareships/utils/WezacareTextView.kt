package com.robbie.wezacareships.utils

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class WezacareTextView(context: Context, attrs: AttributeSet) : AppCompatTextView(context, attrs) {
    init {
        //call the funcyions  to apply the font components
        applyFont()
    }
    private fun applyFont(){
        //This is used to get the file  from the assets folder and set it to the text view
        val typeface: Typeface =
            Typeface.createFromAsset(context.assets,"Montserrat-Regular.ttf")
        setTypeface(typeface)
    }
}