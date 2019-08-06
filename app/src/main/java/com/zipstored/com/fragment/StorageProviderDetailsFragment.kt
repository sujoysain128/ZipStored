package com.zipstored.com.fragment


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.liquorworldkotlin.RetrofitServiceClass.RetrofitServiceGenerator
import com.liquorworldkotlin.RetrofitServiceClass.ServiceClient
import com.liquorworldkotlin.adapter.ProviderStorageListAdapter
import com.liquorworldkotlin.dialog.MessageDialog
import com.zipstored.com.Print

import com.zipstored.com.R
import com.zipstored.com.RetrofitResponse
import com.zipstored.com.activity.DashActivity
import com.zipstored.com.adapter.AmminitiesDetailsListAdapter
import com.zipstored.com.adapter.AvailablePriceListAdapter
import com.zipstored.com.adapter.SliderAdapter
import com.zipstored.com.adapter.WarehouseRulesListAdapter
import com.zipstored.com.utils.MySharedPreferance
import kotlinx.android.synthetic.main.fragment_storage_provider_details.*
import org.json.JSONException
import org.json.JSONObject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class StorageProviderDetailsFragment : Fragment() {

    internal lateinit var arrayList: ArrayList<JSONObject>
    internal lateinit var arrayAmminitiesList: ArrayList<JSONObject>
    internal lateinit var arrayWarehouserulesList: ArrayList<JSONObject>
    internal lateinit var arrayImagesList: ArrayList<JSONObject>
    lateinit var availablePriceListAdapter: AvailablePriceListAdapter
    lateinit var amminitiesDetailsListAdapter: AmminitiesDetailsListAdapter
    lateinit var warehouseRulesListAdapter: WarehouseRulesListAdapter
    lateinit var messageDialogPopup: MessageDialog
    lateinit var mySharedPreferance: MySharedPreferance
    internal lateinit var retrofitResponse: RetrofitResponse
    internal lateinit var serviceClient: ServiceClient
    internal lateinit var retrofitServiceGenerator: RetrofitServiceGenerator

    var location_id = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_storage_provider_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as DashActivity).chnageicon("0")
        (activity as DashActivity).titletext("Storage Details", "StorageDetails")

        mySharedPreferance = MySharedPreferance(activity!!)
        retrofitServiceGenerator = RetrofitServiceGenerator()
        serviceClient = retrofitServiceGenerator.createService(ServiceClient::class.java)
        retrofitResponse = RetrofitResponse(activity!!, childFragmentManager)


        val bundle = this.arguments
        if (bundle != null) {
            location_id = bundle.getString("location_id")
        }

        arrayList = ArrayList()
        arrayAmminitiesList = ArrayList()
        arrayWarehouserulesList = ArrayList()
        arrayImagesList = ArrayList()

        availablePriceListAdapter = AvailablePriceListAdapter(activity!!, arrayList)
        amminitiesDetailsListAdapter = AmminitiesDetailsListAdapter(activity!!, arrayAmminitiesList)
        warehouseRulesListAdapter = WarehouseRulesListAdapter(activity!!, arrayWarehouserulesList)

        val mLinearLayoutManager = androidx.recyclerview.widget.LinearLayoutManager(
            activity!!,
            RecyclerView.VERTICAL,
            false
        )
        rv_available_price.layoutManager = mLinearLayoutManager
        rv_available_price.itemAnimator = androidx.recyclerview.widget.DefaultItemAnimator()
        rv_available_price.setHasFixedSize(true)
        rv_available_price.adapter = availablePriceListAdapter


        val mLinearLayoutManager1 = androidx.recyclerview.widget.LinearLayoutManager(
            activity!!,
            RecyclerView.HORIZONTAL,
            false
        )
        rv_amminities_details.layoutManager = mLinearLayoutManager1
        rv_amminities_details.itemAnimator = androidx.recyclerview.widget.DefaultItemAnimator()
        rv_amminities_details.setHasFixedSize(true)
        rv_amminities_details.adapter = amminitiesDetailsListAdapter


        val mLinearLayoutManager2 = androidx.recyclerview.widget.LinearLayoutManager(
            activity!!,
            RecyclerView.VERTICAL,
            false
        )
        rv_warehouse_rule.layoutManager = mLinearLayoutManager2
        rv_warehouse_rule.itemAnimator = androidx.recyclerview.widget.DefaultItemAnimator()
        rv_warehouse_rule.setHasFixedSize(true)
        rv_warehouse_rule.adapter = warehouseRulesListAdapter

        load_data()
        load_amminities_data()
        load_rules_data()

        storage_details()
    }

    private fun load_rules_data() {
        arrayWarehouserulesList.clear()
        /* arrayWarehouserulesList.add(JSONObject().put("PIC", "sss"))
         arrayWarehouserulesList.add(JSONObject().put("PIC", "dd"))
         arrayWarehouserulesList.add(JSONObject().put("PIC", "sss"))
         warehouseRulesListAdapter.notifyDataSetChanged()*/
    }

    private fun load_amminities_data() {
        arrayAmminitiesList.clear()
        /*   arrayAmminitiesList.add(JSONObject().put("PIC", "sss"))
           arrayAmminitiesList.add(JSONObject().put("PIC", "dd"))
           arrayAmminitiesList.add(JSONObject().put("PIC", "sss"))
           amminitiesDetailsListAdapter.notifyDataSetChanged()*/
    }

    private fun load_data() {
        arrayList.clear()
        /* arrayList.add(JSONObject().put("PIC", "sss"))
         arrayList.add(JSONObject().put("PIC", "dd"))
         arrayList.add(JSONObject().put("PIC", "sss"))
         availablePriceListAdapter.notifyDataSetChanged()*/
        //arrayList.add(JSONObject().put("PIC", R.mipmap.housing_image_details))
    }


    fun storage_details() {

        val hashMap = HashMap<String, String>()
        hashMap["device_type"] = "4"
        hashMap["device_unique_key"] = mySharedPreferance.getPreferancceString(mySharedPreferance.device_id).toString()
        hashMap["device_push_key"] = mySharedPreferance.getPreferancceString(mySharedPreferance.token).toString()
        hashMap["request_access_token"] =
            mySharedPreferance.getPreferancceString(mySharedPreferance.access_token).toString()
        hashMap["location_id"] = location_id


        Print.makePrint("storage_details hashMap ========>>>>>>>" + hashMap)

        retrofitResponse.getWebServiceResponse(
            serviceClient.getStorageDetailsResponse(hashMap),
            object : RetrofitResponse.DataFetchResult {
                override fun onDataFetchComplete(jsonObject: JSONObject) {
                    println("storage_details  response ==========>>>>> " + jsonObject)

                    try {
                        if (jsonObject.getInt("response_status").toString().equals("0")) {
                            val data = jsonObject!!.getJSONObject("location_details").getJSONArray("amenity_list")
                            val data1 = jsonObject!!.getJSONObject("location_details").getJSONArray("slot_list")
                            val data2 = jsonObject!!.getJSONObject("location_details").getJSONArray("storage_rules")
                            //arrayList.clear()
                            for (i in 0 until data.length()) {
                                arrayAmminitiesList.add(data.getJSONObject(i))
                            }
                            amminitiesDetailsListAdapter.notifyDataSetChanged()

                            for (i in 0 until data1.length()) {
                                arrayList.add(data1.getJSONObject(i))
                            }
                            availablePriceListAdapter.notifyDataSetChanged()

                            for (i in 0 until data2.length()) {
                                arrayWarehouserulesList.add(data2.getJSONObject(i))
                            }
                            warehouseRulesListAdapter.notifyDataSetChanged()

                            //Toast.makeText(activity!!, jsonObject.getString("message"), Toast.LENGTH_LONG).show()

                            val img = jsonObject!!.getJSONObject("location_details").getJSONArray("store_images")
                            arrayImagesList.clear()
                            for (i in 0 until img.length()) {
                                arrayImagesList.add(img.getJSONObject(i))
                            }
                            viewPager.adapter = SliderAdapter(activity!!, arrayImagesList)
                            indicator.setupWithViewPager(viewPager, true)

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

