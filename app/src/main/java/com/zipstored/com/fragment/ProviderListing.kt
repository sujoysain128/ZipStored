package com.zipstored.com.fragment


import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.liquorworldkotlin.adapter.ProviderStorageListAdapter

import com.zipstored.com.R
import com.zipstored.com.activity.DashActivity
import com.zipstored.com.iinterface.FragmentCommunicator
import kotlinx.android.synthetic.main.fragment_provider_listing.*
import org.json.JSONObject
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.liquorworldkotlin.RetrofitServiceClass.RetrofitServiceGenerator
import com.liquorworldkotlin.RetrofitServiceClass.ServiceClient
import com.liquorworldkotlin.dialog.MessageDialog
import com.zipstored.com.Print
import com.zipstored.com.RetrofitResponse
import com.zipstored.com.utils.MySharedPreferance
import org.json.JSONException





// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class ProviderListing : androidx.fragment.app.Fragment(), FragmentCommunicator,
    ProviderStorageListAdapter.OnItemClickListener {


    override fun passData(name: String?) {
        Toast.makeText(activity!!, "Passdata invoked", Toast.LENGTH_SHORT).show()
    }

    internal lateinit var arrayList: ArrayList<JSONObject>
    lateinit var providerStorageListAdapter: ProviderStorageListAdapter
    lateinit var messageDialogPopup: MessageDialog
    lateinit var mySharedPreferance: MySharedPreferance
    internal lateinit var retrofitResponse: RetrofitResponse
    internal lateinit var serviceClient: ServiceClient
    internal lateinit var retrofitServiceGenerator: RetrofitServiceGenerator

    var page = 1
    var min_price = ""
    var max_price = ""
    var space_type = ""
    var aminites = ""
    var slot = ""
    var rating = "-1"
    var locations = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_provider_listing, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as DashActivity).chnageicon("1")
        (activity as DashActivity).titletext("", "ProviderListingFrag")

       /* val fm = activity!!.supportFragmentManager
        for (i in 0 until fm.backStackEntryCount) {
            fm.popBackStack()
        }*/

        val bundle = this.arguments
        if (bundle != null) {
            min_price = bundle.getString("min_price")
            max_price = bundle.getString("max_price")
            space_type = bundle.getString("space_type")
            aminites = bundle.getString("aminites")
            slot = bundle.getString("slot")
            rating = bundle.getString("rating")
            locations = bundle.getString("locations")
            println("ProviderListing bundle======>>>>>>" + space_type)
        }

        //http:\/\/res.cloudinary.com\/zipstored\/image\/upload\//h_80,w_256/r6zhddob2u0nirmlptmd.jpg

        mySharedPreferance = MySharedPreferance(activity!!)
        retrofitServiceGenerator = RetrofitServiceGenerator()
        serviceClient = retrofitServiceGenerator.createService(ServiceClient::class.java)
        retrofitResponse = RetrofitResponse(activity!!, childFragmentManager)

        (activity as DashActivity).passVal(FragmentCommunicator { name ->
            Toast.makeText(activity!!, name, Toast.LENGTH_SHORT).show()
        })
        arrayList = ArrayList()
        providerStorageListAdapter = ProviderStorageListAdapter(activity!!, arrayList)

        providerStorageListAdapter.paginate(object : ProviderStorageListAdapter.UpdateData {
            override fun get(position: Int) {
                page = page + 1
                println("paginate page:- " + page)

            }
        })
        providerStorageListAdapter.setOnItemClickListener(this)
        val mLinearLayoutManager = androidx.recyclerview.widget.LinearLayoutManager(
            activity!!,
            RecyclerView.VERTICAL,
            false
        )
        rv_storage_list.layoutManager = mLinearLayoutManager
        rv_storage_list.itemAnimator = androidx.recyclerview.widget.DefaultItemAnimator()
        rv_storage_list.setHasFixedSize(true)
        rv_storage_list.adapter = providerStorageListAdapter

        loadCatData()

        ll_filter.setOnClickListener {
            (activity as DashActivity).chnageicon("1")
            activity!!.supportFragmentManager.popBackStackImmediate()
        }

        rv_storage_list.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (arrayList.size == 0) {
                    println("size of arraylist")
                    providerStorageListAdapter.updateAgain(false)
                } else {
                    println("size of arraylist check")
                    providerStorageListAdapter.updateAgain(true)
                }
            }
        })

        storage_list()
    }

    fun loadCatData() {
        arrayList.clear()
    }

    override fun onResume() {
        super.onResume()
        println("onResume state in providerListing")
    }

    fun showMessagePopup(msg: String) {
        messageDialogPopup = MessageDialog(activity!!)
        messageDialogPopup.setTitle(msg)
        messageDialogPopup.show()
    }

    fun storage_test_list() {
        val hashMap = HashMap<String, String>()
        hashMap["device_type"] = "4"
        hashMap["device_unique_key"] = mySharedPreferance.getPreferancceString(mySharedPreferance.device_id).toString()
        hashMap["device_push_key"] = mySharedPreferance.getPreferancceString(mySharedPreferance.token).toString()
        hashMap["request_access_token"] =
            mySharedPreferance.getPreferancceString(mySharedPreferance.access_token).toString()
        hashMap["latitude"] = "22.5726"
        hashMap["longitude"] = "88.3639"
        hashMap["rating"] = rating
        hashMap["slot"] = slot
        hashMap["space_type"] = space_type
        hashMap["amenities"] = aminites
        hashMap["lower_price"] = min_price
        hashMap["higher_price"] = max_price
        hashMap["locations"] = locations
        hashMap["page_no"] = max_price


        Print.makePrint("storage_list hashMap ========>>>>>>>" + hashMap)

        retrofitResponse.getWebServiceResponse(
            serviceClient.getStorageListResponse(hashMap),
            object : RetrofitResponse.DataFetchResult {
                override fun onDataFetchComplete(jsonObject: JSONObject) {
                    try {

                        println("ProviderListing | RetrofitResponse ========>>>>>>" + jsonObject)
                        if (jsonObject.getInt("response_status").toString().equals("1")) {
                            //mySharedPreferance.savePreferancce(mySharedPreferance.intro_page_show, jsonObject.getJSONObject("user_details").getString("login_counter"))

                            val data = jsonObject!!.getJSONArray("storage_list")
                            for (i in 0 until data.length()) {
                                arrayList.add(data.getJSONObject(i))
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

    fun storage_list() {
        providerStorageListAdapter.loader(true)

        val hashMap = HashMap<String, String>()
        hashMap["device_type"] = "4"
        hashMap["device_unique_key"] = mySharedPreferance.getPreferancceString(mySharedPreferance.device_id).toString()
        hashMap["device_push_key"] = mySharedPreferance.getPreferancceString(mySharedPreferance.token).toString()
        hashMap["request_access_token"] =
            mySharedPreferance.getPreferancceString(mySharedPreferance.access_token).toString()
        hashMap["latitude"] = "22.5726"
        hashMap["longitude"] = "88.3639"
        hashMap["rating"] = rating
        hashMap["slot"] = slot
        hashMap["space_type"] = space_type
        hashMap["amenities"] = aminites
        hashMap["lower_price"] = min_price
        hashMap["higher_price"] = max_price
        hashMap["locations"] = locations
        hashMap["page_no"] = "" + page

        Print.makePrint("storage_list hashMap ========>>>>>>>" + hashMap)

        retrofitResponse.getWebServiceResponse(
            serviceClient.getStorageListResponse(hashMap),
            object : RetrofitResponse.DataFetchResult {
                override fun onDataFetchComplete(jsonObject: JSONObject) {
                    println("storage_list  response ==========>>>>> " + jsonObject)
                    providerStorageListAdapter.loader(false)
                    providerStorageListAdapter.updateAgain(false)
                    if (jsonObject != null) {
                        if (page == 1) {
                            arrayList.clear()
                        }
                        try {
                            if (jsonObject.getInt("response_status").toString().equals("1"))
                            {
                                val data = jsonObject!!.getJSONArray("storage_list")
                                //arrayList.clear()
                                for (i in 0 until data.length()) {
                                    arrayList.add(data.getJSONObject(i))
                                }
                                //Toast.makeText(activity!!, jsonObject.getString("message"), Toast.LENGTH_LONG).show()

                            } else {
                                providerStorageListAdapter.loader(false)
                                providerStorageListAdapter.updateAgain(false)
                                showMessagePopup(jsonObject.getString("response_message"))
                            }
                            providerStorageListAdapter.notifyDataSetChanged()
                        } catch (e: JSONException) {
                            e.printStackTrace()
                            providerStorageListAdapter.loader(false)
                            providerStorageListAdapter.updateAgain(false)
                        }

                    }
                }
            })
    }

    override fun OnItemClick(position: String, order_id: String) {
        var storageProviderDetailsFragment = StorageProviderDetailsFragment()
        var bundle = Bundle()
        bundle.putString("location_id",order_id)
        (activity!! as DashActivity).callFragment(storageProviderDetailsFragment, bundle, false)
    }

}
