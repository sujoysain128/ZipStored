package com.zipstored.com.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import com.google.firebase.FirebaseApp
import com.google.firebase.iid.FirebaseInstanceId
import com.liquorworldkotlin.RetrofitServiceClass.RetrofitServiceGenerator
import com.liquorworldkotlin.RetrofitServiceClass.ServiceClient
import com.liquorworldkotlin.dialog.MessageDialog
import com.zipstored.com.Print
import com.zipstored.com.R
import com.zipstored.com.RetrofitResponse
import com.zipstored.com.utils.MySharedPreferance
import org.json.JSONException
import org.json.JSONObject

class SplashActivity : AppCompatActivity() {
    private val SPLASH_TIME_OUT = 1000
    lateinit var messageDialogPopup: MessageDialog
    lateinit var mySharedPreferance: MySharedPreferance
    internal lateinit var retrofitResponse: RetrofitResponse
    internal lateinit var serviceClient: ServiceClient
    internal lateinit var retrofitServiceGenerator: RetrofitServiceGenerator
    var android_id = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme)
        //startNextActivity()

        //FirebaseApp.initializeApp(this);

        mySharedPreferance = MySharedPreferance(this)
        retrofitServiceGenerator = RetrofitServiceGenerator()
        serviceClient = retrofitServiceGenerator.createService(ServiceClient::class.java)
        retrofitResponse = RetrofitResponse(this, supportFragmentManager)

        android_id = Settings.Secure.getString(
            this.contentResolver,
            Settings.Secure.ANDROID_ID
        )

        /*mySharedPreferance.savePreferancce(
            mySharedPreferance.token,
            FirebaseInstanceId.getInstance().getToken().toString()
        )*/

        mySharedPreferance.savePreferancce(
            mySharedPreferance.device_id,
            android_id
        )

        slots()

        amminites()
    }

    fun startNextActivity()
    {
        val i = Intent(this, SignInActivity::class.java)
        startActivity(i)
        finish()

       /* Handler().postDelayed({
            val i = Intent(this, SignInActivity::class.java)
            startActivity(i)
            finish()
        }, SPLASH_TIME_OUT.toLong())*/
    }

    fun slots() {

        val hashMap = HashMap<String, String>()
        hashMap["device_unique_key"] = android_id
        hashMap["device_push_key"] = mySharedPreferance.getPreferancceString(mySharedPreferance.token).toString()
        hashMap["device_type"] = "4"
        Print.makePrint(hashMap)

        retrofitResponse.getWebServiceResponseForSplash(
            serviceClient.getSlotResponse(hashMap),
            object : RetrofitResponse.DataFetchResult {
                override fun onDataFetchComplete(jsonObject: JSONObject) {
                    try {

                        println("splash |slots| RetrofitResponse ========>>>>>>" + jsonObject)
                        if (jsonObject.getInt("response_status").toString().equals("1")) {
                            mySharedPreferance.savePreferancce(mySharedPreferance.all_slot_master_data,jsonObject.toString())


                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }

                }
            })
    }

    fun amminites() {

        val hashMap = HashMap<String, String>()
        hashMap["device_unique_key"] = android_id
        hashMap["device_push_key"] = mySharedPreferance.getPreferancceString(mySharedPreferance.token).toString()
        hashMap["device_type"] = "4"
        Print.makePrint(hashMap)

        retrofitResponse.getWebServiceResponseForSplash(
            serviceClient.getAmenitiesResponse(hashMap),
            object : RetrofitResponse.DataFetchResult {
                override fun onDataFetchComplete(jsonObject: JSONObject) {
                    try {

                        println("splash|amminites | RetrofitResponse ========>>>>>>" + jsonObject)
                        if (jsonObject.getInt("response_status").toString().equals("1")) {
                            mySharedPreferance.savePreferancce(mySharedPreferance.all_amenities_master_data, jsonObject.toString())

                            startNextActivity()

                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }

                }
            })
    }
}
