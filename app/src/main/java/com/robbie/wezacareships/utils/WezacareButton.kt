package com.robbie.wezacareships.utils

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton


class WezacareButton(context: Context, attrs: AttributeSet) : AppCompatButton(context, attrs) {
    init {
        //call the functions  to apply the font components
        applyFont()
    }
    private fun applyFont(){
        //This is used to get the file  from the assets folder and set it to the text view
        val typeface: Typeface =
            Typeface.createFromAsset(context.assets,"Montserrat-Bold.ttf")
        setTypeface(typeface)
    }
}