
package com.zipstored.com
import android.content.Context
import android.graphics.Typeface
import androidx.appcompat.widget.AppCompatTextView
import android.util.AttributeSet

/**
 * Created by ncrts on 13/9/17.
 */

class ArialRegularTextView : AppCompatTextView {

    constructor(context: Context) : super(context) {
        this.setTypeface(typeface, typeface.style)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        this.setTypeface(typeface, typeface.style)

    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {

        val tf = typeface
        this.setTypeface(typeface, typeface.style)
    }


    override fun setTypeface(tf: Typeface?, style: Int) {
        var tf = tf
        println()
        if (style == 1) {
            //replace "HelveticaBOLD.otf" with the name of your bold font
            tf = Typeface.createFromAsset(context.applicationContext.assets, "arial_regular.ttf")
        } else {
            //replace "HelveticaNORMAL.otf" with the name of your normal font
            tf = Typeface.createFromAsset(context.applicationContext.assets, "arial_regular.ttf")
        }
        super.setTypeface(tf, 0)
    }
    //avenirltstd-black
}
