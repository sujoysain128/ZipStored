package com.liquorworldkotlin.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.Window
import com.zipstored.com.R
import kotlinx.android.synthetic.main.dialog_message.*


/**
 * Created by ncrts on 9/9/17.
 */

class MessageDialog(context: Context) : Dialog(context), View.OnClickListener {


    var d: Dialog? = null
    internal var message = ""
    internal var bt_text = ""

    fun setTitle(message: String): MessageDialog {
        this.message = message
        return this
    }

    fun setSubmitMsg(button: String) {
        this.bt_text = button
    }




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setContentView(R.layout.dialog_message)

        bt_dialogOK.setOnClickListener(this)

        if (bt_text.length > 0) {
            bt_dialogOK.text = bt_text
        }
        if (message.length > 0) {
            tv_dialogMsg.text = message
        }
    }


    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.bt_dialogOK -> dismiss()
            else -> {
            }
        }
        dismiss()
    }
}