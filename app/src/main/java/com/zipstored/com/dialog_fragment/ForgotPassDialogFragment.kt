package com.zipstored.com.dialog_fragment

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import com.liquorworldkotlin.RetrofitServiceClass.RetrofitServiceGenerator
import com.liquorworldkotlin.RetrofitServiceClass.ServiceClient
import com.liquorworldkotlin.dialog.MessageDialog
import com.zipstored.com.Print
import com.zipstored.com.R
import com.zipstored.com.RetrofitResponse
import com.zipstored.com.utils.MySharedPreferance
import kotlinx.android.synthetic.main.dialog_fragment_forgot_pass.*
import org.json.JSONException
import org.json.JSONObject
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern


class ForgotPassDialogFragment : androidx.fragment.app.DialogFragment() {

    internal lateinit var dialog: Dialog
    internal lateinit var v: View
    internal lateinit var animation_zoom_in: Animation
    internal lateinit var slide_out_buttom: Animation

    lateinit var mySharedPreferance: MySharedPreferance
    internal lateinit var retrofitResponse: RetrofitResponse
    internal lateinit var serviceClient: ServiceClient
    internal lateinit var retrofitServiceGenerator: RetrofitServiceGenerator
    lateinit var messageDialogPopup: MessageDialog


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dialog = object : Dialog(activity!!, R.style.DialogFragmentCustomTheme) {
            override fun onBackPressed() {
                //super.onBackPressed();
                dismiss()
            }
        }
        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)
        dialog.window!!.attributes.windowAnimations = R.style.DialogFragmentCustomTheme
        println("Dialog ONcreate============>")
        return dialog
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v = inflater.inflate(R.layout.dialog_fragment_forgot_pass, container, false)
        animation_zoom_in = AnimationUtils.loadAnimation(activity!!.applicationContext, R.anim.slide_out_up)
        slide_out_buttom = AnimationUtils.loadAnimation(activity!!.applicationContext, R.anim.slide_out_bottom)
        println("Current CLASS===>>>" + javaClass.simpleName)
        return v
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mySharedPreferance = MySharedPreferance(activity!!)
        retrofitServiceGenerator = RetrofitServiceGenerator()
        serviceClient = retrofitServiceGenerator.createService(ServiceClient::class.java)
        retrofitResponse = RetrofitResponse(activity!!, childFragmentManager)

        tv_apply.setOnClickListener {
            forgot_password()
        }
        tv_dismiss.setOnClickListener {
            dismiss()
        }
    }


    fun forgot_password() {
        if (checkValidation()) {
            retrofitResponse.setDialogMessage("Please Wait...")

            val hashMap = HashMap<String, String>()
            hashMap["email"] = edt_forgot_email.text.toString().trim()
            hashMap["device_type"] = "4"
            hashMap["device_unique_key"] = mySharedPreferance.getPreferancceString(mySharedPreferance.device_id).toString()
            hashMap["device_push_key"] = mySharedPreferance.getPreferancceString(mySharedPreferance.token).toString()

            Print.makePrint(hashMap)

            retrofitResponse.getWebServiceResponse(
                serviceClient.getForgotPasswordResponse(hashMap),
                object : RetrofitResponse.DataFetchResult {
                    override fun onDataFetchComplete(jsonObject: JSONObject) {
                        try {
                            if (jsonObject.getInt("response_status").toString().equals("1"))
                            {
                                showMessagePopup(jsonObject.getString("response_message"))
                                dismiss()
                            }
                            else{
                                showMessagePopup(jsonObject.getString("response_message"))
                            }
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }

                    }
                })
        }
    }


    fun emailValidator(email: String): Boolean {
        val pattern: Pattern
        val matcher: Matcher
        val EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
        pattern = Pattern.compile(EMAIL_PATTERN)
        matcher = pattern.matcher(email)
        return matcher.matches()
    }


    fun showMessagePopup(msg: String) {
        messageDialogPopup = MessageDialog(activity!!)
        messageDialogPopup.setTitle(msg)
        messageDialogPopup.show()
    }


    fun checkValidation(): Boolean {
        if (edt_forgot_email.text.toString().trim().length < 1) {
            edt_forgot_email.error = "Enter your Email."
            edt_forgot_email.requestFocus()
            return false
        } else if (!emailValidator(edt_forgot_email.text.toString())) {
            edt_forgot_email.error = "This email is not valid."
            edt_forgot_email.requestFocus()
            return false
        } else {
            return true
        }
    }


}

