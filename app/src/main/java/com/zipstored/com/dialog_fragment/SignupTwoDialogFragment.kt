package com.zipstored.com.dialog_fragment


import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import android.telephony.PhoneNumberUtils
import android.text.InputFilter
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.EditText
import com.liquorworldkotlin.dialog.MessageDialog
import com.zipstored.com.R
import com.zipstored.com.utils.UsPhoneNumberFormatter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.edt_password
import kotlinx.android.synthetic.main.signup_dialog_fragment.*
import kotlinx.android.synthetic.main.signup_dialog_fragment.edt_email_address
import kotlinx.android.synthetic.main.signup_dialog_fragment.iv_back
import kotlinx.android.synthetic.main.signup_dialog_fragment.tv_log_in
import kotlinx.android.synthetic.main.signup_two_dialog_fragment.*
import java.lang.ref.WeakReference
import android.widget.Toast
import com.liquorworldkotlin.RetrofitServiceClass.RetrofitServiceGenerator
import com.liquorworldkotlin.RetrofitServiceClass.ServiceClient
import com.rilixtech.Country
import com.rilixtech.CountryCodePicker
import com.zipstored.com.Print
import com.zipstored.com.RetrofitResponse
import org.json.JSONException
import org.json.JSONObject
import android.provider.Settings.Secure
import com.zipstored.com.utils.MySharedPreferance


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class SignupTwoDialogFragment : androidx.fragment.app.DialogFragment() {

    internal lateinit var dialog: Dialog
    internal lateinit var v: View
    internal var itemClickListener: OnItemClickListener? = null
    lateinit var messageDialogPopup: MessageDialog
    internal lateinit var usPhoneNumberFormatter: UsPhoneNumberFormatter
    internal lateinit var retrofitResponse: RetrofitResponse
    internal lateinit var serviceClient: ServiceClient
    internal lateinit var retrofitServiceGenerator: RetrofitServiceGenerator
    var android_id = ""
    lateinit var mySharedPreferance: MySharedPreferance
    var country_code = "+1"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v = inflater.inflate(R.layout.signup_two_dialog_fragment, container, false)
        return v
    }


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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        usPhoneNumberFormatter = UsPhoneNumberFormatter(WeakReference<EditText>(edt_phone_number))
        edt_phone_number.addTextChangedListener(usPhoneNumberFormatter)//16
        edt_phone_number.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(12))

        mySharedPreferance = MySharedPreferance(activity!!)
        retrofitServiceGenerator = RetrofitServiceGenerator()
        serviceClient = retrofitServiceGenerator.createService(ServiceClient::class.java)
        retrofitResponse = RetrofitResponse(activity!!, activity!!.supportFragmentManager)


        iv_back.setOnClickListener {
            dismiss()
        }
        tv_log_in.setOnClickListener {
            if (itemClickListener != null) {
                itemClickListener!!.OnItemClick("", "")
                dismiss()
            }
        }

        bt_sign_up.setOnClickListener {
            if (checkValidation()) {
                /*if (itemClickListener != null) {
                    itemClickListener!!.OnItemClick("", "")
                    dismiss()
                }*/
                signup()
            }
        }

        tb_pass_eye_password.setOnClickListener {
            if (tb_pass_eye_password.isChecked == true) {
                edt_password.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                edt_password.setSelection(edt_password.getText()!!.length)
            } else {
                edt_password.inputType = InputType.TYPE_CLASS_TEXT
                edt_password.setSelection(edt_password.getText()!!.length)
            }
        }
        tb_confirm_pass_eye.setOnClickListener {
            if (tb_confirm_pass_eye.isChecked == true) {
                edt_confirm_password.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                edt_confirm_password.setSelection(edt_confirm_password.getText()!!.length)
            } else {
                edt_confirm_password.inputType = InputType.TYPE_CLASS_TEXT
                edt_confirm_password.setSelection(edt_confirm_password.getText()!!.length)
            }
        }

        ccp.setOnCountryChangeListener { selectedCountry ->

           /* Toast.makeText(
                context,
                "Updated " + selectedCountry.phoneCode,
                Toast.LENGTH_SHORT
            ).show()*/
            country_code = selectedCountry.phoneCode
        }

        android_id = Secure.getString(
            context!!.contentResolver,
            Secure.ANDROID_ID
        )

        println("android id====>>> "+android_id +mySharedPreferance.getPreferancceString(mySharedPreferance.user_email))
    }

    fun setOnItemClickListener(itemClickListener: OnItemClickListener) {
        this.itemClickListener = itemClickListener
    }

    interface OnItemClickListener {
        fun OnItemClick(position: String, id: String)
    }

    fun checkValidation(): Boolean {
        if (edt_phone_number.text.toString().trim().length < 1) {
            //edt_email_address.error = "Enter your Email."
            showMessagePopup("Enter your phone no.")
            edt_phone_number.requestFocus()
            return false
        } else if (edt_password.text.toString().trim().length < 3) {
            //edt_email_address.error = "This email is not valid."
            showMessagePopup("Password is not valid.It Must be greater than 3.")
            edt_password.requestFocus()
            return false
        } else if (edt_confirm_password.text.toString().length < 1) {
            //edt_password.error = "Enter your Password."
            showMessagePopup("Confirm Password is not valid.It Must be greater than 3.")
            edt_confirm_password.requestFocus()
            return false
        } else if (!edt_confirm_password.text.toString().trim().equals(edt_password.text.toString().trim())) {
            //edt_password.error = "Enter your Password."
            showMessagePopup("Password donot match")
            edt_confirm_password.requestFocus()
            return false
        } else {
            return true
        }
    }

    fun showMessagePopup(msg: String) {
        messageDialogPopup = MessageDialog(activity!!)
        messageDialogPopup.setTitle(msg)
        messageDialogPopup.show()
    }

    fun signup()
    {
        val hashMap = HashMap<String, String>()
        hashMap["email"] = mySharedPreferance.getPreferancceString(mySharedPreferance.user_email).toString()
        hashMap["password"] = edt_password.text.toString().trim()
        hashMap["device_type"] = "2"
        hashMap["device_unique_key"] = android_id
        hashMap["device_push_key"] = mySharedPreferance.getPreferancceString(mySharedPreferance.token).toString()
        hashMap["name"] = mySharedPreferance.getPreferancceString(mySharedPreferance.user_name).toString()
        hashMap["latitude"] = "22.5726"
        hashMap["longitude"] = "88.3639"
        hashMap["mobile_no"] = country_code + edt_phone_number.text.toString().trim()

        Print.makePrint(hashMap)

        retrofitResponse.getWebServiceResponse(
            serviceClient.getSignUpResponse(hashMap),
            object : RetrofitResponse.DataFetchResult {
                override fun onDataFetchComplete(jsonObject: JSONObject) {
                    try {

                        println("signup | RetrofitResponse ========>>>>>>"+jsonObject)
                        if (jsonObject.getInt("response_status").toString().equals("1"))
                        {
                            mySharedPreferance.savePreferancce(mySharedPreferance.user_password,edt_password.text.toString().trim())
                            Toast.makeText(activity!!, jsonObject.getString("response_message"), Toast.LENGTH_LONG).show()
                            if (itemClickListener != null) {
                                itemClickListener!!.OnItemClick("", "")
                                dismiss()
                            }
                        } else {
                            showMessagePopup(jsonObject.getString("response_message"))
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }

                }
            })
    }
}
