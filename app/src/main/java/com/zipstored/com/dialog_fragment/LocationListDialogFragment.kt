package com.zipstored.com.dialog_fragment

import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.liquorworldkotlin.RetrofitServiceClass.RetrofitServiceGenerator
import com.liquorworldkotlin.RetrofitServiceClass.ServiceClient
import com.liquorworldkotlin.dialog.MessageDialog
import com.zipstored.com.Print
import com.zipstored.com.R
import com.zipstored.com.RetrofitResponse
import com.zipstored.com.adapter.LocationListAdapter
import com.zipstored.com.utils.MySharedPreferance
import kotlinx.android.synthetic.main.location_dropdown_list.*
import kotlinx.android.synthetic.main.search_bar_layout.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList



class LocationListDialogFragment : androidx.fragment.app.DialogFragment() {


    internal lateinit var dialog: Dialog
    internal lateinit var v: View
    internal lateinit var animation_zoom_in: Animation
    internal lateinit var slide_out_buttom: Animation

    lateinit var mySharedPreferance: MySharedPreferance
    internal lateinit var retrofitResponse: RetrofitResponse
    internal lateinit var serviceClient: ServiceClient
    internal lateinit var retrofitServiceGenerator: RetrofitServiceGenerator
    lateinit var messageDialogPopup: MessageDialog

    internal var itemClickListener: OnItemClickListener? = null
    internal lateinit var arrayList: ArrayList<JSONObject>
    lateinit var locationListAdapter: LocationListAdapter
    var jsonObject: JSONObject? = null
    var jsonArray: JSONArray? = null

    var name: String? = null
    var searchObject: JSONObject? = null
    var curentObject: JSONObject? = null
    var totaljsonArray: JSONArray? = null

    var search_string = ""

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
        v = inflater.inflate(R.layout.location_dropdown_list, container, false)
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
        arrayList = ArrayList()

        locationListAdapter = LocationListAdapter(activity!!, arrayList)
        //amminitesListAdapter.setOnItemClickListener(this)
        val mLinearLayoutManager = androidx.recyclerview.widget.LinearLayoutManager(
            activity!!,
            RecyclerView.VERTICAL,
            false
        )
        rv_location.layoutManager = mLinearLayoutManager
        rv_location.itemAnimator = androidx.recyclerview.widget.DefaultItemAnimator()
        rv_location.setHasFixedSize(true)
        rv_location.adapter = locationListAdapter

        try {
            loadCatData()

        } catch (e: Exception) {
            e.printStackTrace()
        }


        tv_search.setOnClickListener {
            //et_search.text.toString()

            /*for (int i = 0; i < array.length(); i++) {
            JSONObject currObject = array.getJSONObject(i);
            name = currObject.getString("name");

            if(name == "hello")
            {
                searchObject = currObject
            }
        }

            return searchObject*/
            arrayList.clear()
            for (i in 0 until totaljsonArray!!.length()) {
                println("totaljsonArray ======>>> "+totaljsonArray)
                 curentObject = totaljsonArray!!.getJSONObject(i)
                println("curentObject ======>>> "+curentObject)
                name = curentObject!!.getString("city")
                println("name ======>>> "+name + "search string =====>>>> "+search_string)
                if (name!!.contains(search_string,true))
                {
                    println("loop ======>>> ")
                    searchObject = curentObject
                    arrayList.add(searchObject!!)

                }
            }
            locationListAdapter.notifyDataSetChanged()
            println("after search in location object =========>>>>>> "+arrayList)
        }

        tv_apply.setOnClickListener {
            println(" submit click:- " + arrayList.toString())
            var selectedIDs = StringBuilder()
            for (i in 0 until arrayList.size) {
                if (arrayList[i].has("isSelected")) {
                    if (arrayList[i].getBoolean("isSelected")) {
                        if (i == 0) {
                            selectedIDs.append(arrayList[i].optString("city"))
                        } else {
                            if (selectedIDs.length > 0) {
                                selectedIDs.append(",")
                                selectedIDs.append(arrayList[i].optString("city"))
                            } else {
                                selectedIDs.append(arrayList[i].optString("city"))
                            }
                        }
                    }
                }

                println("get all selectedIds =========>>>> " + selectedIDs.toString())
                //dismiss()
            }

            if (itemClickListener != null) {
                itemClickListener!!.OnItemClick(selectedIDs.toString(), "Test")
                dismiss()
            }
        }

        edt_search.addTextChangedListener(object :TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                println("addTextChangedListener =========>>>>>> "+s)
                search_string = s.toString()

                if (s!!.isEmpty())
                {
                    arrayList.clear()
                    location_list()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

        })


        tv_dismiss.setOnClickListener {
            if (itemClickListener != null) {
                itemClickListener!!.OnItemClick("ss", "dismiss")
                dismiss()
            }
        }

        location_list()
    }


    fun loadCatData() {
        arrayList.clear()
        //locationListAdapter.notifyDataSetChanged()
    }


    fun setOnItemClickListener(itemClickListener: OnItemClickListener) {
        this.itemClickListener = itemClickListener
    }

    interface OnItemClickListener {
        fun OnItemClick(position: String, id: String)
    }


    fun location_list() {

        retrofitResponse.setDialogMessage("Please Wait...")

        val hashMap = HashMap<String, String>()
        hashMap["device_type"] = "4"
        hashMap["device_unique_key"] = mySharedPreferance.getPreferancceString(mySharedPreferance.device_id).toString()
        hashMap["device_push_key"] = mySharedPreferance.getPreferancceString(mySharedPreferance.token).toString()

        Print.makePrint(hashMap)

        retrofitResponse.getWebServiceResponse(
            serviceClient.getLocationListResponse(hashMap),
            object : RetrofitResponse.DataFetchResult {
                override fun onDataFetchComplete(jsonObject: JSONObject) {
                    try {
                        if (jsonObject.getInt("response_status").toString().equals("1")) {
                            val data = jsonObject!!.getJSONArray("locations")
                            totaljsonArray = jsonObject!!.getJSONArray("locations")
                            for (i in 0 until data.length()) {
                                arrayList.add(data.getJSONObject(i))
                            }
                            locationListAdapter.notifyDataSetChanged()
                        } else {
                            showMessagePopup(jsonObject.getString("response_message"))
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }

                }
            })
    }

    fun showMessagePopup(msg: String) {
        messageDialogPopup = MessageDialog(activity!!)
        messageDialogPopup.setTitle(msg)
        messageDialogPopup.show()
    }


}

