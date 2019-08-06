package com.zipstored.com.utils

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

import java.lang.ref.WeakReference

/**
 * Created by himadri on 26/4/17.
 */

class UsPhoneNumberFormatter(private val mWeakEditText: WeakReference<EditText>) : TextWatcher {
    //This TextWatcher sub-class formats entered numbers as 1 (123) 456-7890
    private var mFormatting: Boolean = false // this is a flag which prevents the
    // stack(onTextChanged)
    private var clearFlag: Boolean = false
    private var mLastStartLocation: Int = 0
    private var mLastBeforeText: String? = null

    private var afterChangeText: AfterChangeText? = null

    fun setOnAfterChangeText(afterChangeText: AfterChangeText) {
        this.afterChangeText = afterChangeText
    }

    override fun beforeTextChanged(
        s: CharSequence, start: Int, count: Int,
        after: Int
    ) {
        if (after == 0 && s.toString() == "1 ") {
            clearFlag = true
        }
        mLastStartLocation = start
        mLastBeforeText = s.toString()
    }

    override fun onTextChanged(
        s: CharSequence, start: Int, before: Int,
        count: Int
    ) {
        // TODO: Do nothing
    }

    override fun afterTextChanged(s: Editable) {
        // Make sure to ignore calls to afterTextChanged caused by the work
        // done below
        if (!mFormatting) {
            mFormatting = true
            val curPos = mLastStartLocation
            val beforeValue = mLastBeforeText
            val currentValue = s.toString()
            val formattedValue = formatUsNumber(s)
            if (currentValue.length > beforeValue!!.length) {
                val setCusorPos = formattedValue.length - (beforeValue.length - curPos)
                mWeakEditText.get()?.setSelection(if (setCusorPos < 0) 0 else setCusorPos)
            } else {
                var setCusorPos = formattedValue.length - (currentValue.length - curPos)
                if (setCusorPos > 0 && !Character.isDigit(formattedValue[setCusorPos - 1])) {
                    setCusorPos--
                }
                mWeakEditText.get()?.setSelection(if (setCusorPos < 0) 0 else setCusorPos)
            }
            mFormatting = false
        }
        if (afterChangeText != null) {
            afterChangeText!!.OnAfterChangeText(s.toString())
        } else {
            println("Invalid Interface Callback....")
        }

    }

    private fun formatUsNumber(text: Editable): String {
        val formattedString = StringBuilder()
        // Remove everything except digits
        var p = 0
        while (p < text.length) {
            val ch = text[p]
            if (!Character.isDigit(ch)) {
                text.delete(p, p + 1)
            } else {
                p++
            }
        }
        // Now only digits are remaining
        val allDigitString = text.toString()

        val totalDigitCount = allDigitString.length

        if (totalDigitCount == 0
            || totalDigitCount > 13 && !allDigitString.startsWith("1")
            || totalDigitCount > 14
        ) {
            // May be the total length of input length is greater than the
            // expected value so we'll remove all formatting
            text.clear()
            text.append(allDigitString)
            return allDigitString
        }
        var alreadyPlacedDigitCount = 0
        // Only '1' is remaining and user pressed backspace and so we clear
        // the edit text.
        if (allDigitString == "1" && clearFlag) {
            text.clear()
            clearFlag = false
            return ""
        }
        /*if (allDigitString.startsWith("1")) {
            formattedString.append("+1")
            alreadyPlacedDigitCount++
        } else if (allDigitString.startsWith("0") || allDigitString.startsWith("2") || allDigitString.startsWith("3") || allDigitString.startsWith(
                "4"
            ) || allDigitString.startsWith("5") || allDigitString.startsWith("6") || allDigitString.startsWith("7") || allDigitString.startsWith(
                "8"
            ) || allDigitString.startsWith("9")
        ) {
            formattedString.append("+1")
        }*/
        // The first 3 numbers beyond '1' must be enclosed in brackets "()"
        if (totalDigitCount - alreadyPlacedDigitCount > 3) {
            formattedString.append(
                ""
                        + allDigitString.substring(
                    alreadyPlacedDigitCount,
                    alreadyPlacedDigitCount + 4
                ) + " "
            )
            alreadyPlacedDigitCount += 4
        }
        // There must be a '-' inserted after the next 3 numbers
        if (totalDigitCount - alreadyPlacedDigitCount > 4) {
            formattedString.append(
                allDigitString.substring(
                    alreadyPlacedDigitCount, alreadyPlacedDigitCount + 4
                ) + " "
            )
            alreadyPlacedDigitCount += 4
        }
        // All the required formatting is done so we'll just copy the
        // remaining digits.
        if (totalDigitCount > alreadyPlacedDigitCount) {
            formattedString.append(
                allDigitString
                    .substring(alreadyPlacedDigitCount)
            )
        }

        text.clear()
        text.append(formattedString.toString())

        mWeakEditText.get()?.post {
            try {
                mWeakEditText.get()!!.setSelection(text.length)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return formattedString.toString()
    }

    interface AfterChangeText {
        fun OnAfterChangeText(text: String)
    }

}