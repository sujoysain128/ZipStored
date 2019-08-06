package com.zipstored.com

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.zipstored.com.R


/**
 * Created by ncrts on 18/1/18.
 */

class ProgressBarDialog : androidx.fragment.app.DialogFragment() {

    private var dialogMessage: String? = "Please Wait....."
    private var tv_pbd_message: TextView? = null

    val isShowing: Boolean
        get() = if (dialog != null) dialog.isShowing else false

    fun setMessage(message: String) {
        dialogMessage = message

    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        return object : Dialog(activity!!) {
            override fun onBackPressed() {

            }
        }
    }


    override fun onResume() {
        super.onResume()
        if (dialogMessage != null) {
            tv_pbd_message!!.text = dialogMessage
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCanceledOnTouchOutside(false)
        return inflater.inflate(R.layout.layout_progress_bar_dialog, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tv_pbd_message = view.findViewById(R.id.tv_pbd_message)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        tv_pbd_message!!.text = dialogMessage
    }

    override fun dismiss() {
        dialog.dismiss()
    }

    fun showDialog(fragmentManager: androidx.fragment.app.FragmentManager) {
        show(fragmentManager, "ProgressBarDialog")
    }

}
