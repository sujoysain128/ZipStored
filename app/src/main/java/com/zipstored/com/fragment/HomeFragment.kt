package com.zipstored.com.fragment


import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.zipstored.com.R
import android.view.WindowManager
import androidx.recyclerview.widget.RecyclerView
import com.liquorworldkotlin.dialog.MessageDialog
import com.zipstored.com.activity.DashActivity
import com.zipstored.com.adapter.AmminitesListAdapter
import com.zipstored.com.adapter.SpaceListAdapter
import com.zipstored.com.adapter.SpaceTypeListAdapter
import com.zipstored.com.dialog_fragment.LocationListDialogFragment
import com.zipstored.com.utils.MySharedPreferance
import kotlinx.android.synthetic.main.fragment_home.*
import org.json.JSONArray
import org.json.JSONObject


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class HomeFragment : androidx.fragment.app.Fragment(), View.OnClickListener, SpaceListAdapter.OnItemClickListener,
    SpaceTypeListAdapter.OnItemClickListener {


    internal lateinit var arrayList: ArrayList<JSONObject>
    internal lateinit var arrayListSlot: ArrayList<JSONObject>
    internal lateinit var arrayListSlotType: ArrayList<JSONObject>
    lateinit var amminitesListAdapter: AmminitesListAdapter
    lateinit var spaceListAdapter: SpaceListAdapter
    lateinit var spaceTypeListAdapter: SpaceTypeListAdapter
    lateinit var mySharedPreferance: MySharedPreferance
    lateinit var messageDialogPopup: MessageDialog
    var jsonObject: JSONObject? = null
    var jsonArray: JSONArray? = null

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
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //(activity as DashActivity).drawerimageset("1")
        (activity as DashActivity).titletext("What are you looking for", "HomeFrag")
        /*val params = activity!!.getWindow().getAttributes()
        params.flags =
            params.flags or (WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)*/

        mySharedPreferance = MySharedPreferance(activity!!)

        arrayList = ArrayList()
        arrayListSlot = ArrayList()
        arrayListSlotType = ArrayList()

        amminitesListAdapter = AmminitesListAdapter(activity!!, arrayList)
        spaceListAdapter = SpaceListAdapter(activity!!, arrayListSlot)
        spaceTypeListAdapter = SpaceTypeListAdapter(activity!!, arrayListSlotType)
        //amminitesListAdapter.setOnItemClickListener(this)
        val mLinearLayoutManager = androidx.recyclerview.widget.LinearLayoutManager(
            activity!!,
            RecyclerView.VERTICAL,
            false
        )
        rv_amminites.layoutManager = mLinearLayoutManager
        rv_amminites.itemAnimator = androidx.recyclerview.widget.DefaultItemAnimator()
        rv_amminites.setHasFixedSize(true)
        rv_amminites.adapter = amminitesListAdapter

        val mLinearLayoutManagerforspace = androidx.recyclerview.widget.LinearLayoutManager(
            activity!!,
            RecyclerView.HORIZONTAL,
            false
        )
        spaceListAdapter.setOnItemClickListener(this)
        val mLinearLayoutManagerforspacetype = androidx.recyclerview.widget.LinearLayoutManager(
            activity!!,
            RecyclerView.HORIZONTAL,
            false
        )
        rv_area.layoutManager = mLinearLayoutManagerforspace
        rv_area.itemAnimator = DefaultItemAnimator()
        rv_area.setHasFixedSize(true)
        rv_area.adapter = spaceListAdapter

        spaceTypeListAdapter.setOnItemClickListener(this)
        rv_space_type.layoutManager = mLinearLayoutManagerforspacetype
        rv_space_type.itemAnimator = DefaultItemAnimator()
        rv_space_type.setHasFixedSize(true)
        rv_space_type.adapter = spaceTypeListAdapter


        try {
            loadCatData()
            load_slot_data()
            load_slot_type_data()

        } catch (e: Exception) {
            e.printStackTrace()
        }


        tv_show_all.setOnClickListener {
            //tv_show_all.text = "SHOW LESS AMMENITIES"
            if (tv_show_all.text.equals("SHOW LESS AMMENITIES")) {
                amminitesListAdapter.showlessItem()
                tv_show_all.text = "SHOW ALL AMMENITIES"
            } else {
                amminitesListAdapter.showallItem()
                tv_show_all.text = "SHOW LESS AMMENITIES"
            }

        }

        /*tv_show_less.setOnClickListener {
            amminitesListAdapter.showlessItem()
            tv_show_all.visibility = View.VISIBLE
            tv_show_less.visibility = View.GONE
        }*/

        var providerListing = ProviderListing()

        bt_apply.setOnClickListener {


            var selectedIDs = StringBuilder()
            for (i in 0 until arrayList.size) {
                if (arrayList[i].has("isSelected")) {
                    if (arrayList[i].getBoolean("isSelected")) {
                        if (i == 0) {
                            selectedIDs.append(arrayList[i].optString("id"))
                        } else {
                            if (selectedIDs.length > 0) {
                                selectedIDs.append(",")
                                selectedIDs.append(arrayList[i].optString("id"))
                            } else {
                                selectedIDs.append(arrayList[i].optString("id"))
                            }
                        }
                    }
                }

                println("get all amenities selectedIds =========>>>> " + selectedIDs.toString())
                //dismiss()
            }

            var selectedID1s = StringBuilder()
            for (i in 0 until arrayListSlot.size) {
                if (arrayListSlot[i].has("isSelected")) {
                    if (arrayListSlot[i].getBoolean("isSelected")) {
                        if (i == 0) {
                            selectedID1s.append(arrayListSlot[i].optString("id"))
                        } else {
                            if (selectedID1s.length > 0) {
                                selectedID1s.append(",")
                                selectedID1s.append(arrayListSlot[i].optString("id"))
                            } else {
                                selectedID1s.append(arrayListSlot[i].optString("id"))
                            }
                        }
                    }
                }
            }

            var selectedID2s = StringBuilder()
            for (i in 0 until arrayListSlotType.size) {
                if (arrayListSlotType[i].has("isSelected")) {
                    if (arrayListSlotType[i].getBoolean("isSelected")) {
                        if (i == 0) {
                            selectedID2s.append(arrayListSlotType[i].optString("id"))
                        } else {
                            if (selectedID2s.length > 0) {
                                selectedID2s.append(",")
                                selectedID2s.append(arrayListSlotType[i].optString("id"))
                            } else {
                                selectedID2s.append(arrayListSlotType[i].optString("id"))
                            }
                        }
                    }
                }
            }

            slot = selectedID1s.toString()
            aminites = selectedIDs.toString()
            space_type = selectedID2s.toString()

            println("get all slot selectedIds =========>>>> " + slot)
            println("get all space type selectedIds =========>>>> " + space_type)

            val bundle = Bundle()
            bundle.putString("min_price", min_price)
            bundle.putString("max_price", max_price)
            bundle.putString("space_type", space_type)
            bundle.putString("aminites", aminites)
            bundle.putString("slot", slot)
            bundle.putString("rating", rating)
            bundle.putString("locations", locations)

            if (!slot.equals("") || !space_type.equals("") ) {
                if (!space_type.equals("") && !slot.equals("") ) {
                    (activity!! as DashActivity).callFragment(providerListing, bundle, false)
                }
                else {
                    showMessagePopup("Please select both space type and slot")
                }
            }
            else{
                (activity!! as DashActivity).callFragment(providerListing, bundle, false)
            }

        }

        tv_location.setOnClickListener {
            val locationListDialogFragment = LocationListDialogFragment()
            locationListDialogFragment.setOnItemClickListener(object :
                LocationListDialogFragment.OnItemClickListener {
                override fun OnItemClick(position: String, id: String) {
                    println("HomeFragment | LocationDialog=====>>>>" + position.length)
                    locations = position
                    iv_location.visibility = View.GONE
                    var char: Char = ','
                    tv_location_count.text = countChar(position, char).toString()
                    tv_location_count.visibility = View.VISIBLE

                    if (id.equals("dismiss")) {
                        tv_location_count.visibility = View.GONE
                        iv_location.visibility = View.VISIBLE
                        locations = ""
                    }

                }
            })
            locationListDialogFragment.show(childFragmentManager, "LocationListDialogFragment")
        }

        rangeSeekbar1.setMinValue(0f).apply()

        rangeSeekbar1.setMaxValue(20f).apply()

        rangeSeekbar2.setMinValue(0f).apply()

        rangeSeekbar2.setMaxValue(100f).apply()

        rangeSeekbar2.setOnRangeSeekbarChangeListener { minValue, maxValue ->
            min_price = minValue.toString()
            max_price = maxValue.toString()

            tv_pricerange.text = "$ " + minValue.toString() + " - " + " $ " + maxValue.toString()
        }

        rangeSeekbar1.setOnRangeSeekbarChangeListener { minValue, maxValue ->

            tv_distancerange.text = minValue.toString() + " - " + maxValue.toString() + " Kilometeres"
        }

        /*tv_regular.setOnClickListener {
            //tv_regular.setBackgroundDrawable(R.drawable.curve_rectangle_gray)
            tv_regular.setTextColor(ContextCompat.getColor(activity!!, R.color.colorWhite))
            tv_regular.setBackgroundDrawable(ContextCompat.getDrawable(activity!!,R.drawable.curve_rectangle_gray))
            tv_climate_control.setTextColor(ContextCompat.getColor(activity!!, R.color.colorLitePinkApp))
            tv_climate_control.setBackgroundDrawable(ContextCompat.getDrawable(activity!!,R.drawable.curve_rectangle_filter_pink))
        }

        tv_climate_control.setOnClickListener {
            tv_climate_control.setTextColor(ContextCompat.getColor(activity!!, R.color.colorWhite))
            tv_climate_control.setBackgroundDrawable(ContextCompat.getDrawable(activity!!,R.drawable.curve_rectangle_gray))
            tv_regular.setTextColor(ContextCompat.getColor(activity!!, R.color.colorLitePinkApp))
            tv_regular.setBackgroundDrawable(ContextCompat.getDrawable(activity!!,R.drawable.curve_rectangle_filter_pink))
        }*/

        /* tv_5_5.setOnClickListener {
             tv_5_5.setTextColor(ContextCompat.getColor(activity!!, R.color.colorWhite))
             tv_5_5.setBackgroundDrawable(ContextCompat.getDrawable(activity!!,R.drawable.curve_rectangle_gray))
             tv_10_10.setTextColor(ContextCompat.getColor(activity!!, R.color.gray))
             tv_10_10.setBackgroundDrawable(ContextCompat.getDrawable(activity!!,R.drawable.curve_rectangle_filter))
             tv_20_20.setTextColor(ContextCompat.getColor(activity!!, R.color.gray))
             tv_20_20.setBackgroundDrawable(ContextCompat.getDrawable(activity!!,R.drawable.curve_rectangle_filter))
         }

         tv_10_10.setOnClickListener {
             tv_10_10.setTextColor(ContextCompat.getColor(activity!!, R.color.colorWhite))
             tv_10_10.setBackgroundDrawable(ContextCompat.getDrawable(activity!!,R.drawable.curve_rectangle_gray))
             tv_5_5.setTextColor(ContextCompat.getColor(activity!!, R.color.gray))
             tv_5_5.setBackgroundDrawable(ContextCompat.getDrawable(activity!!,R.drawable.curve_rectangle_filter))
             tv_20_20.setTextColor(ContextCompat.getColor(activity!!, R.color.gray))
             tv_20_20.setBackgroundDrawable(ContextCompat.getDrawable(activity!!,R.drawable.curve_rectangle_filter))
         }

         tv_20_20.setOnClickListener {
             tv_20_20.setTextColor(ContextCompat.getColor(activity!!, R.color.colorWhite))
             tv_20_20.setBackgroundDrawable(ContextCompat.getDrawable(activity!!,R.drawable.curve_rectangle_gray))
             tv_5_5.setTextColor(ContextCompat.getColor(activity!!, R.color.gray))
             tv_5_5.setBackgroundDrawable(ContextCompat.getDrawable(activity!!,R.drawable.curve_rectangle_filter))
             tv_10_10.setTextColor(ContextCompat.getColor(activity!!, R.color.gray))
             tv_10_10.setBackgroundDrawable(ContextCompat.getDrawable(activity!!,R.drawable.curve_rectangle_filter))
         }*/

        tv_1_star.setOnClickListener(this)
        tv_2_star.setOnClickListener(this)
        tv_3_star.setOnClickListener(this)
        tv_4_star.setOnClickListener(this)
        tv_5_star.setOnClickListener(this)
        tv_unrated.setOnClickListener(this)
    }

    private fun load_slot_type_data() {
        arrayListSlotType.clear()
        jsonObject = JSONObject(mySharedPreferance.getPreferancceString(mySharedPreferance.all_login_master_data))
        val data = jsonObject!!.getJSONArray("space_types")

        for (i in 0 until data.length()) {
            //data.getJSONObject(i).put("isSelected", false)
            arrayListSlotType.add(data.getJSONObject(i))
        }
        spaceTypeListAdapter.notifyDataSetChanged()
    }

    private fun load_slot_data() {
        arrayListSlot.clear()
        jsonObject = JSONObject(mySharedPreferance.getPreferancceString(mySharedPreferance.all_slot_master_data))
        val data = jsonObject!!.getJSONArray("master-slots")

        for (i in 0 until data.length()) {
            //data.getJSONObject(i).put("isSelected", false)
            arrayListSlot.add(data.getJSONObject(i))
        }

        spaceListAdapter.notifyDataSetChanged()
    }

    fun loadCatData() {
        arrayList.clear()
        jsonObject = JSONObject(mySharedPreferance.getPreferancceString(mySharedPreferance.all_amenities_master_data))
        val data = jsonObject!!.getJSONArray("amenities")

        for (i in 0 until data.length()) {
            //data.getJSONObject(i).put("isSelected", false)
            arrayList.add(data.getJSONObject(i))
        }
        amminitesListAdapter.notifyDataSetChanged()
    }

    fun countChar(str: String, c: Char): Int {
        var count = 0

        for (i in 0 until str.length) {
            if (str[i] == c)
                count++
        }
        return count + 1
    }

    override fun onClick(v: View?) {
        when (v!!.id) {

            R.id.tv_1_star -> {
                rating = "1"
                tv_1_star.setTextColor(ContextCompat.getColor(activity!!, R.color.colorWhite))
                tv_1_star.setBackgroundDrawable(ContextCompat.getDrawable(activity!!, R.drawable.curve_rectangle_gray))

                tv_2_star.setTextColor(ContextCompat.getColor(activity!!, R.color.gray))
                tv_2_star.setBackgroundDrawable(
                    ContextCompat.getDrawable(
                        activity!!,
                        R.drawable.curve_rectangle_filter
                    )
                )
                tv_3_star.setTextColor(ContextCompat.getColor(activity!!, R.color.gray))
                tv_3_star.setBackgroundDrawable(
                    ContextCompat.getDrawable(
                        activity!!,
                        R.drawable.curve_rectangle_filter
                    )
                )
                tv_4_star.setTextColor(ContextCompat.getColor(activity!!, R.color.gray))
                tv_4_star.setBackgroundDrawable(
                    ContextCompat.getDrawable(
                        activity!!,
                        R.drawable.curve_rectangle_filter
                    )
                )
                tv_5_star.setTextColor(ContextCompat.getColor(activity!!, R.color.gray))
                tv_5_star.setBackgroundDrawable(
                    ContextCompat.getDrawable(
                        activity!!,
                        R.drawable.curve_rectangle_filter
                    )
                )
                tv_unrated.setTextColor(ContextCompat.getColor(activity!!, R.color.gray))
                tv_unrated.setBackgroundDrawable(
                    ContextCompat.getDrawable(
                        activity!!,
                        R.drawable.curve_rectangle_filter
                    )
                )

            }
            R.id.tv_2_star -> {
                rating = "2"
                tv_2_star.setTextColor(ContextCompat.getColor(activity!!, R.color.colorWhite))
                tv_2_star.setBackgroundDrawable(ContextCompat.getDrawable(activity!!, R.drawable.curve_rectangle_gray))

                tv_1_star.setTextColor(ContextCompat.getColor(activity!!, R.color.gray))
                tv_1_star.setBackgroundDrawable(
                    ContextCompat.getDrawable(
                        activity!!,
                        R.drawable.curve_rectangle_filter
                    )
                )
                tv_3_star.setTextColor(ContextCompat.getColor(activity!!, R.color.gray))
                tv_3_star.setBackgroundDrawable(
                    ContextCompat.getDrawable(
                        activity!!,
                        R.drawable.curve_rectangle_filter
                    )
                )
                tv_4_star.setTextColor(ContextCompat.getColor(activity!!, R.color.gray))
                tv_4_star.setBackgroundDrawable(
                    ContextCompat.getDrawable(
                        activity!!,
                        R.drawable.curve_rectangle_filter
                    )
                )
                tv_5_star.setTextColor(ContextCompat.getColor(activity!!, R.color.gray))
                tv_5_star.setBackgroundDrawable(
                    ContextCompat.getDrawable(
                        activity!!,
                        R.drawable.curve_rectangle_filter
                    )
                )
                tv_unrated.setTextColor(ContextCompat.getColor(activity!!, R.color.gray))
                tv_unrated.setBackgroundDrawable(
                    ContextCompat.getDrawable(
                        activity!!,
                        R.drawable.curve_rectangle_filter
                    )
                )
            }
            R.id.tv_3_star -> {
                rating = "3"
                tv_3_star.setTextColor(ContextCompat.getColor(activity!!, R.color.colorWhite))
                tv_3_star.setBackgroundDrawable(ContextCompat.getDrawable(activity!!, R.drawable.curve_rectangle_gray))

                tv_2_star.setTextColor(ContextCompat.getColor(activity!!, R.color.gray))
                tv_2_star.setBackgroundDrawable(
                    ContextCompat.getDrawable(
                        activity!!,
                        R.drawable.curve_rectangle_filter
                    )
                )
                tv_1_star.setTextColor(ContextCompat.getColor(activity!!, R.color.gray))
                tv_1_star.setBackgroundDrawable(
                    ContextCompat.getDrawable(
                        activity!!,
                        R.drawable.curve_rectangle_filter
                    )
                )
                tv_4_star.setTextColor(ContextCompat.getColor(activity!!, R.color.gray))
                tv_4_star.setBackgroundDrawable(
                    ContextCompat.getDrawable(
                        activity!!,
                        R.drawable.curve_rectangle_filter
                    )
                )
                tv_5_star.setTextColor(ContextCompat.getColor(activity!!, R.color.gray))
                tv_5_star.setBackgroundDrawable(
                    ContextCompat.getDrawable(
                        activity!!,
                        R.drawable.curve_rectangle_filter
                    )
                )
                tv_unrated.setTextColor(ContextCompat.getColor(activity!!, R.color.gray))
                tv_unrated.setBackgroundDrawable(
                    ContextCompat.getDrawable(
                        activity!!,
                        R.drawable.curve_rectangle_filter
                    )
                )
            }
            R.id.tv_4_star -> {
                rating = "4"
                tv_4_star.setTextColor(ContextCompat.getColor(activity!!, R.color.colorWhite))
                tv_4_star.setBackgroundDrawable(ContextCompat.getDrawable(activity!!, R.drawable.curve_rectangle_gray))

                tv_2_star.setTextColor(ContextCompat.getColor(activity!!, R.color.gray))
                tv_2_star.setBackgroundDrawable(
                    ContextCompat.getDrawable(
                        activity!!,
                        R.drawable.curve_rectangle_filter
                    )
                )
                tv_3_star.setTextColor(ContextCompat.getColor(activity!!, R.color.gray))
                tv_3_star.setBackgroundDrawable(
                    ContextCompat.getDrawable(
                        activity!!,
                        R.drawable.curve_rectangle_filter
                    )
                )
                tv_1_star.setTextColor(ContextCompat.getColor(activity!!, R.color.gray))
                tv_1_star.setBackgroundDrawable(
                    ContextCompat.getDrawable(
                        activity!!,
                        R.drawable.curve_rectangle_filter
                    )
                )
                tv_5_star.setTextColor(ContextCompat.getColor(activity!!, R.color.gray))
                tv_5_star.setBackgroundDrawable(
                    ContextCompat.getDrawable(
                        activity!!,
                        R.drawable.curve_rectangle_filter
                    )
                )
                tv_unrated.setTextColor(ContextCompat.getColor(activity!!, R.color.gray))
                tv_unrated.setBackgroundDrawable(
                    ContextCompat.getDrawable(
                        activity!!,
                        R.drawable.curve_rectangle_filter
                    )
                )
            }
            R.id.tv_5_star -> {
                rating = "5"
                tv_5_star.setTextColor(ContextCompat.getColor(activity!!, R.color.colorWhite))
                tv_5_star.setBackgroundDrawable(ContextCompat.getDrawable(activity!!, R.drawable.curve_rectangle_gray))

                tv_2_star.setTextColor(ContextCompat.getColor(activity!!, R.color.gray))
                tv_2_star.setBackgroundDrawable(
                    ContextCompat.getDrawable(
                        activity!!,
                        R.drawable.curve_rectangle_filter
                    )
                )
                tv_3_star.setTextColor(ContextCompat.getColor(activity!!, R.color.gray))
                tv_3_star.setBackgroundDrawable(
                    ContextCompat.getDrawable(
                        activity!!,
                        R.drawable.curve_rectangle_filter
                    )
                )
                tv_4_star.setTextColor(ContextCompat.getColor(activity!!, R.color.gray))
                tv_4_star.setBackgroundDrawable(
                    ContextCompat.getDrawable(
                        activity!!,
                        R.drawable.curve_rectangle_filter
                    )
                )
                tv_1_star.setTextColor(ContextCompat.getColor(activity!!, R.color.gray))
                tv_1_star.setBackgroundDrawable(
                    ContextCompat.getDrawable(
                        activity!!,
                        R.drawable.curve_rectangle_filter
                    )
                )
                tv_unrated.setTextColor(ContextCompat.getColor(activity!!, R.color.gray))
                tv_unrated.setBackgroundDrawable(
                    ContextCompat.getDrawable(
                        activity!!,
                        R.drawable.curve_rectangle_filter
                    )
                )
            }
            R.id.tv_unrated -> {
                rating = "-1"
                tv_unrated.setTextColor(ContextCompat.getColor(activity!!, R.color.colorWhite))
                tv_unrated.setBackgroundDrawable(ContextCompat.getDrawable(activity!!, R.drawable.curve_rectangle_gray))

                tv_2_star.setTextColor(ContextCompat.getColor(activity!!, R.color.gray))
                tv_2_star.setBackgroundDrawable(
                    ContextCompat.getDrawable(
                        activity!!,
                        R.drawable.curve_rectangle_filter
                    )
                )
                tv_3_star.setTextColor(ContextCompat.getColor(activity!!, R.color.gray))
                tv_3_star.setBackgroundDrawable(
                    ContextCompat.getDrawable(
                        activity!!,
                        R.drawable.curve_rectangle_filter
                    )
                )
                tv_4_star.setTextColor(ContextCompat.getColor(activity!!, R.color.gray))
                tv_4_star.setBackgroundDrawable(
                    ContextCompat.getDrawable(
                        activity!!,
                        R.drawable.curve_rectangle_filter
                    )
                )
                tv_5_star.setTextColor(ContextCompat.getColor(activity!!, R.color.gray))
                tv_5_star.setBackgroundDrawable(
                    ContextCompat.getDrawable(
                        activity!!,
                        R.drawable.curve_rectangle_filter
                    )
                )
                tv_1_star.setTextColor(ContextCompat.getColor(activity!!, R.color.gray))
                tv_1_star.setBackgroundDrawable(
                    ContextCompat.getDrawable(
                        activity!!,
                        R.drawable.curve_rectangle_filter
                    )
                )
            }
        }

    }

    //area list
    override fun OnItemClick(position: Int, id: String) {
        println("HomeFragment | spaceListAdapter =======>>>> " + id)
        slot = id
    }

    //space type
    override fun OnItemClick(position: String, id: String) {
        println("HomeFragment | spaceTypeListAdapter =======>>>> " + id)
        space_type = id
    }

    override fun onPause() {
        super.onPause()
        println("On pause state in Home fragment")
        min_price = ""
        max_price = ""
        space_type = ""
        aminites = ""
        slot = ""
        rating = "-1"
        locations = ""

    }

    fun showMessagePopup(msg: String) {
        messageDialogPopup = MessageDialog(activity!!)
        messageDialogPopup.setTitle(msg)
        messageDialogPopup.show()
    }

}
