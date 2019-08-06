package com.zipstored.com

import android.content.Context
import com.google.android.material.snackbar.Snackbar
import androidx.fragment.app.FragmentManager
import android.view.View
import android.widget.Toast
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


import java.io.IOException

class RetrofitResponse {

    private var context: Context? = null
    private var progressBarDialog: ProgressBarDialog? = null
    private var view: View? = null
    private var fragmentManager: androidx.fragment.app.FragmentManager? = null


    constructor(context: Context) {
        this.context = context
        progressBarDialog = ProgressBarDialog()

    }

    constructor(context: Context, fragmentManager: androidx.fragment.app.FragmentManager) {
        this.context = context
        this.fragmentManager = fragmentManager
        progressBarDialog = ProgressBarDialog()


    }


    fun showProgressDialog() {
        if (!progressBarDialog!!.isShowing && fragmentManager != null) {
            progressBarDialog = ProgressBarDialog()
            progressBarDialog!!.showDialog(fragmentManager!!)
        }
    }

    fun hideProgressDialog() {
        if (progressBarDialog != null && progressBarDialog!!.isShowing)
            progressBarDialog!!.dismiss()
    }

    /*public void setUpProgressDialog(FragmentManager fragmentManager) {
            this.fragmentManager = fragmentManager;
        }*/

    fun setDialogMessage(message: String) {
        if (progressBarDialog != null)
            progressBarDialog!!.setMessage(message)
    }


    fun setUpSnackBar(view: View) {
        this.view = view
    }

    fun getWebServiceResponse(callBackResponse: Call<ResponseBody>, mDataFetchResult: DataFetchResult) {
        if (InternetConnectionCheck.isInternetAvailable(context!!)) {
            if (progressBarDialog != null)
                showProgressDialog()
            callBackResponse.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    Print.makePrint("Service URL==>" + response.raw().request().url())
                    try {
                        if (response.body() != null) {
                            val serviceResponse = response.body()!!.string()
                            Print.makePrint("Service Response ==>$serviceResponse")
                            hideProgressDialog()
                            val jsonResponse = JSONObject(serviceResponse)
                            mDataFetchResult.onDataFetchComplete(jsonResponse)
                        } else {
                            showMessage("Server Error!!")
                            hideProgressDialog()
                        }
                    } catch (e: IOException) {
                        showMessage("Something Went Wrong!!")
                        e.printStackTrace()
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }

                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    hideProgressDialog()
                    showMessage("Server Error!!")
                }
            })
        } else {
            showMessage("No Internet Connection!!")
        }

    }

    private fun showMessage(message: String) {
        if (view != null)
            showSnackMessage(view!!, message)
        else
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    private fun showSnackMessage(view: View, message: String) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show()
    }

    interface DataFetchResult {
        fun onDataFetchComplete(jsonResponse: JSONObject)
    }

    //need to create another for splash dialog
    fun getWebServiceResponseForSplash(callBackResponse: Call<ResponseBody>, mDataFetchResult: DataFetchResult) {
        if (InternetConnectionCheck.isInternetAvailable(context!!)) {
            if (progressBarDialog != null)
                //showProgressDialog()
            callBackResponse.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    Print.makePrint("Service URL==>" + response.raw().request().url())
                    try {
                        if (response.body() != null) {
                            val serviceResponse = response.body()!!.string()
                            Print.makePrint("Service Response ==>$serviceResponse")
                            //hideProgressDialog()
                            val jsonResponse = JSONObject(serviceResponse)
                            mDataFetchResult.onDataFetchComplete(jsonResponse)
                        } else {
                            showMessage("Server Error!!")
                            //hideProgressDialog()
                        }
                    } catch (e: IOException) {
                        showMessage("Something Went Wrong!!")
                        e.printStackTrace()
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }

                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    hideProgressDialog()
                    showMessage("Server Error!!")
                }
            })
        } else {
            showMessage("No Internet Connection!!")
        }

    }
}
