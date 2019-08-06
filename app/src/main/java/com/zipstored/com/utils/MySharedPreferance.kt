package com.zipstored.com.utils

import android.content.Context


class MySharedPreferance {

    var user_name = "user_name"
    var user_email = "user_email"
    var user_password = "user_password"
    var token = "token"
    var access_token = "access_token"
    var device_id = "device_id"
    var all_slot_master_data = "all_slot_master_data"
    var all_amenities_master_data = "all_amenities_master_data"
    var all_login_master_data = "all_login_master_data"

    var context: Context


    constructor(context: Context) {
        this.context = context
    }


    fun savePreferancce(key: String, value: String) {
        println("Preference KEY===>>>    $key Preference VALUE===>>>   $value")
        val sharedPreferences = context.getSharedPreferences("Preferance", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.commit()
    }

    fun savePreferancce(key: String, value: Boolean) {
        println("Preference KEY===>>>    $key Preference VALUE===>>>   $value")
        val sharedPreferences = context.getSharedPreferences("Preferance", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean(key, value)
        editor.commit()
    }


    fun getPreferancceString(key: String): String? {
        val sharedPreferences = context.getSharedPreferences("Preferance", Context.MODE_PRIVATE)
        return sharedPreferences.getString(key, "")
    }

    fun getPreferancceBoolean(key: String): Boolean {
        val sharedPreferences = context.getSharedPreferences("Preferance", Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(key, false)
    }


    fun deletePreferenceValue(key: String) {
        val sharedPreferences = context.getSharedPreferences("Preferance", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove(key)
        editor.apply()
    }
}