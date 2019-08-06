package com.zipstored.com.dialog_fragment


import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.liquorworldkotlin.dialog.MessageDialog
import com.zipstored.com.R
import com.zipstored.com.utils.MySharedPreferance
import kotlinx.android.synthetic.main.signup_dialog_fragment.*
import java.util.regex.Matcher
import java.util.regex.Pattern

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class SignupDialogFragment : androidx.fragment.app.DialogFragment() {

    internal lateinit var dialog: Dialog
    internal lateinit var v: View
    lateinit var messageDialogPopup: MessageDialog
    lateinit var mySharedPreferance: MySharedPreferance

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v = inflater.inflate(R.layout.signup_dialog_fragment, container, false)
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

        mySharedPreferance = MySharedPreferance(activity!!)

        iv_back.setOnClickListener {
            dismiss()
        }
        tv_log_in.setOnClickListener {
            dismiss()
        }

        bt_next.setOnClickListener {

            if (checkValidation()) {
                mySharedPreferance.savePreferancce(mySharedPreferance.user_name, edt_name.text.toString().trim())
                mySharedPreferance.savePreferancce(mySharedPreferance.user_email, edt_email_address.text.toString().trim())
                val signupTwoDialogFragment = SignupTwoDialogFragment()
                signupTwoDialogFragment.setOnItemClickListener(object : SignupTwoDialogFragment.OnItemClickListener {
                    override fun OnItemClick(position: String, id: String) {
                        dismiss()
                    }
                })
                signupTwoDialogFragment.show(childFragmentManager, "SignupTwoDialogFragment")
            }
        }


        edt_email_address.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (emailValidator(s.toString()))
                {
                    iv_tick_email.visibility = View.VISIBLE
                }
                else{
                    iv_tick_email.visibility = View.GONE
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })

        edt_name.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.toString().length>2)
                {
                    iv_tick_name.visibility = View.VISIBLE
                }
                else{
                    iv_tick_name.visibility = View.GONE
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })
    }

    fun checkValidation(): Boolean {
        if (edt_name.text.toString().length < 1) {
            //edt_password.error = "Enter your Password."
            showMessagePopup("Enter your Name.")
            edt_name.requestFocus()
            return false
        }
        else if (edt_email_address.text.toString().trim().length < 1) {
            //edt_email_address.error = "Enter your Email."
            showMessagePopup("Enter your Email")
            edt_email_address.requestFocus()
            return false
        } else if (!emailValidator(edt_email_address.text.toString())) {
            //edt_email_address.error = "This email is not valid."
            showMessagePopup("This email is not valid.")
            edt_email_address.requestFocus()
            return false
        }  else {
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
        messageDialogPopup = MessageDialog(activity!!)
        messageDialogPopup.setTitle(msg)
        messageDialogPopup.show()
    }
}
