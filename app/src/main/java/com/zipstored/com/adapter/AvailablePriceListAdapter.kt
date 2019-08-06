package com.zipstored.com.adapter


import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.ToggleButton
import androidx.core.content.ContextCompat
import com.zipstored.com.R

import org.json.JSONObject
import java.util.*


class AvailablePriceListAdapter(private val context: Context, arrayList: ArrayList<JSONObject>) :
    androidx.recyclerview.widget.RecyclerView.Adapter<AvailablePriceListAdapter.DataObjectHolder>() {

    private var arrayList = ArrayList<JSONObject>()
    private var showitemcount = "3"
    internal var itemClickListener: OnItemClickListener? = null
    internal var contex: Context? = null
    var row_index: Int? = null
    var click_check = "1"

    init {

        this.arrayList = arrayList
        this.contex = context
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataObjectHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.child_available_price, parent, false)
        return DataObjectHolder(view)
    }

    override fun onBindViewHolder(holder: DataObjectHolder, position: Int) {

        /*if (!arrayList[position].has("isSelected")) {
            arrayList[position].put("isSelected", false)
        }*/

        /*if (arrayList.get(position).getBoolean("isSelected")) {
            holder.tv_slot.setTextColor(ContextCompat.getColor(context, R.color.colorWhite))
            holder.tv_slot.setBackgroundDrawable(
                ContextCompat.getDrawable(
                    context,
                    R.drawable.curve_rectangle_gray
                )
            )
        } else {
            holder.tv_slot.setTextColor(ContextCompat.getColor(context, R.color.gray))
            holder.tv_slot.setBackgroundDrawable(
                ContextCompat.getDrawable(
                    context,
                    R.drawable.curve_rectangle_filter
                )
            )
        }*/

        //holder.tv_slot.setText(arrayList.get(position).getString("slot"))


       /* holder.tv_slot.setOnClickListener {
            *//*row_index = position
            notifyDataSetChanged()*//*

            for (i in 0 until arrayList.size) {
                System.out.println("position===>"+position)
                if (i == position) {
                    if (arrayList.get(position).getBoolean("isSelected"))
                    {
                        arrayList.get(position).put("isSelected", false)
                    }
                    else {
                        arrayList.get(position).put("isSelected", true)
                    }

                } else {
                    arrayList.get(i).put("isSelected", false)
                }
            }
            notifyDataSetChanged()
        }*/
            /*if (row_index == position)
            {
                holder.tv_slot.setTextColor(ContextCompat.getColor(context, R.color.colorWhite))
                holder.tv_slot.setBackgroundDrawable(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.curve_rectangle_gray
                    )
                )
                if (itemClickListener != null) {
                    itemClickListener!!.OnItemClick(position, arrayList.get(position).getString("slot"))
                }

                println("click response check ====>>> "+click_check)
            } else {
                holder.tv_slot.setTextColor(ContextCompat.getColor(context, R.color.gray))
                holder.tv_slot.setBackgroundDrawable(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.curve_rectangle_filter
                    )
                )
            }*/


        (holder as DataObjectHolder).tv_area.setText(arrayList.get(position).getString("slot_display_text"))
        (holder as DataObjectHolder).tv_price_value.setText("$ "+arrayList.get(position).getString("slot_price"))


    }

    override fun getItemCount(): Int {
        println("show count in available price Adapter =======>>>>>>>>>>> " + showitemcount)
        return arrayList.size

    }

    override fun getItemViewType(position: Int): Int {
        return position
    }


    inner class DataObjectHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

       internal var tv_area: TextView
       internal var tv_price_value: TextView

        init {
            tv_area = itemView.findViewById(R.id.tv_area)
            tv_price_value = itemView.findViewById(R.id.tv_price_value)

        }
    }

    fun setOnItemClickListener(itemClickListener: OnItemClickListener) {
        this.itemClickListener = itemClickListener
    }

    interface OnItemClickListener {
        fun OnItemClick(position: Int, id: String)
    }


}
