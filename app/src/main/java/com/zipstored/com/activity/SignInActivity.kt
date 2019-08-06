package com.zipstored.com.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import com.google.firebase.FirebaseApp
import com.google.firebase.iid.FirebaseInstanceId
import com.liquorworldkotlin.RetrofitServiceClass.RetrofitServiceGenerator
import com.liquorworldkotlin.RetrofitServiceClass.ServiceClient
import com.liquorworldkotlin.dialog.MessageDialog
import com.zipstored.com.Print
import com.zipstored.com.R
import com.zipstored.com.RetrofitResponse
import com.zipstored.com.dialog_fragment.ForgotPassDialogFragment
import com.zipstored.com.dialog_fragment.SignupDialogFragment
import com.zipstored.com.utils.MySharedPreferance
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONException
import org.json.JSONObject
import java.util.regex.Matcher
import java.util.regex.Pattern

class SignInActivity : AppCompatActivity() {

    lateinit var messageDialogPopup: MessageDialog
    lateinit var mySharedPreferance: MySharedPreferance
    internal lateinit var retrofitResponse: RetrofitResponse
    internal lateinit var serviceClient: ServiceClient
    internal lateinit var retrofitServiceGenerator: RetrofitServiceGenerator
    var android_id = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mySharedPreferance = MySharedPreferance(this)
        retrofitServiceGenerator = RetrofitServiceGenerator()
        serviceClient = retrofitServiceGenerator.createService(ServiceClient::class.java)
        retrofitResponse = RetrofitResponse(this, supportFragmentManager)

        //FirebaseApp.initializeApp(this);

        bt_login.setOnClickListener {
            if (checkValidation()) {
                signin()
            }
        }

        tv_sign_up.setOnClickListener {
            val signupDialogFragment = SignupDialogFragment()
            signupDialogFragment.show(supportFragmentManager, "SignupDialogFragment")
        }

        tv_pwd.setOnClickListener {
            val forgotPassDialogFragment = ForgotPassDialogFragment()
            forgotPassDialogFragment.show(supportFragmentManager, "ForgotPassDialogFragment")
        }

        edt_email_address.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (emailValidator(s.toString())) {
                    iv_email_valid.visibility = View.VISIBLE
                } else {
                    iv_email_valid.visibility = View.GONE
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })

        tb_pass_eye.setOnClickListener {
            if (tb_pass_eye.isChecked == true) {
                edt_password.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                edt_password.setSelection(edt_password.getText()!!.length)
            } else {
                edt_password.inputType = InputType.TYPE_CLASS_TEXT
                edt_password.setSelection(edt_password.getText()!!.length)
            }
        }

        //FirebaseApp.initializeApp(this);
        println("FirebaseInstanceId token======" + FirebaseInstanceId.getInstance().getToken())
        /*mySharedPreferance.savePreferancce(
            mySharedPreferance.token,
            FirebaseInstanceId.getInstance().getToken().toString()
        )*/

        android_id = Settings.Secure.getString(
            this.contentResolver,
            Settings.Secure.ANDROID_ID
        )

        mySharedPreferance.savePreferancce(
            mySharedPreferance.device_id,
           android_id
        )
        if (!mySharedPreferance.getPreferancceString(mySharedPreferance.user_email).equals(""))
        {
            edt_email_address.setText(mySharedPreferance.getPreferancceString(mySharedPreferance.user_email).toString())
        }
        if (!mySharedPreferance.getPreferancceString(mySharedPreferance.user_password).equals(""))
        {
            edt_password.setText(mySharedPreferance.getPreferancceString(mySharedPreferance.user_password).toString())
        }

        println("SigninActivity jsonobject======" + mySharedPreferance.getPreferancceString(mySharedPreferance.all_amenities_master_data))

    }

    fun checkValidation(): Boolean {
        if (edt_email_address.text.toString().trim().length < 1) {
            //edt_email_address.error = "Enter your Email."
            showMessagePopup("Enter your Email")
            edt_email_address.requestFocus()
            return false
        } else if (!emailValidator(edt_email_address.text.toString())) {
            //edt_email_address.error = "This email is not valid."
            showMessagePopup("This email is not valid.")
            edt_email_address.requestFocus()
            return false
        } else if (edt_password.text.toString().length < 1) {
            //edt_password.error = "Enter your Password."
            showMessagePopup("Enter your Password.")
            edt_password.requestFocus()
            return false
        } else {
            return true
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
        messageDialogPopup = MessageDialog(this@SignInActivity)
        messageDialogPopup.setTitle(msg)
        messageDialogPopup.show()
    }

    fun signin() {
        val hashMap = HashMap<String, String>()
        hashMap["email"] = edt_email_address.text.toString().trim()
        hashMap["password"] = edt_password.text.toString().trim()
        hashMap["device_type"] = "4"
        hashMap["device_unique_key"] = android_id
        hashMap["device_push_key"] = mySharedPreferance.getPreferancceString(mySharedPreferance.token).toString()
        hashMap["latitude"] = "22.5726"
        hashMap["longitude"] = "88.3639"


        Print.makePrint(hashMap)

        retrofitResponse.getWebServiceResponse(
            serviceClient.getLogInResponse(hashMap),
            object : RetrofitResponse.DataFetchResult {
                override fun onDataFetchComplete(jsonObject: JSONObject) {
                    try {

                        println("sigin | RetrofitResponse ========>>>>>>" + jsonObject)
                        if (jsonObject.getInt("response_status").toString().equals("1")) {
                            mySharedPreferance.savePreferancce(mySharedPreferance.all_login_master_data, jsonObject.toString())
                            Toast.makeText(
                                this@SignInActivity,
                                jsonObject.getString("response_message"),
                                Toast.LENGTH_LONG
                            ).show()
                            mySharedPreferance.savePreferancce(
                                mySharedPreferance.access_token,
                                jsonObject.getString("access_token")
                            )
                            mySharedPreferance.savePreferancce(
                                mySharedPreferance.user_email,
                                jsonObject.getString("email")
                            )
                            mySharedPreferance.savePreferancce(
                                mySharedPreferance.user_password,
                                edt_password.text.toString().trim()
                            )
                            val i = Intent(this@SignInActivity, DashActivity::class.java)
                            startActivity(i)
                            finish()

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
