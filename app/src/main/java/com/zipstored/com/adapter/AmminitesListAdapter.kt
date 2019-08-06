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
import com.zipstored.com.R

import org.json.JSONObject
import java.util.*


class AmminitesListAdapter(private val context: Context, arrayList: ArrayList<JSONObject>) :
    androidx.recyclerview.widget.RecyclerView.Adapter<AmminitesListAdapter.DataObjectHolder>() {

    private var arrayList = ArrayList<JSONObject>()
    private var showitemcount = "3"
    internal var itemClickListener: OnItemClickListener? = null

    init {

        this.arrayList = arrayList
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataObjectHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.child_amminites, parent, false)
        return DataObjectHolder(view)
    }

    override fun onBindViewHolder(holder: DataObjectHolder, position: Int) {
        //println("resource adapter 2:- " + arrayList[position].has("isSelected"))
        if (!arrayList[position].has("isSelected")) {
            arrayList[position].put("isSelected", false)
        }
        /*if (arrayList.get(position).getBoolean("isSelected")) {
            holder.tb_cat.isChecked = true
        } else {
            holder.tb_cat.isChecked = false
        }*/

        holder.tv_cat_name.text = (arrayList.get(position).getString("name"))

       /* holder.ll_cat_cell.setOnClickListener {
            //holder.tb_cat.performClick()
            for (i in 0 until arrayList.size)
            {
                System.out.println("position===>"+position)
                if (i == position) {
                    if(holder.tb_cat.isChecked ==false) {
                        arrayList.get(position).put("isSelected", true)
                    }else{
                        arrayList.get(position).put("isSelected", false)
                    }
                }
            }
            notifyDataSetChanged()
        }*/

        if (arrayList.get(position).getBoolean("isSelected")) {
            holder.tb_cat.isChecked = true
        } else {
            holder.tb_cat.isChecked = false
        }

        holder.ll_cat_cell.setOnClickListener {
            //holder.tb_cat.performClick()
            for (i in 0 until arrayList.size)
            {
                System.out.println("position===>"+position)
                if (i == position) {
                    if(holder.tb_cat.isChecked ==false) {
                        arrayList.get(position).put("isSelected", true)
                    }else{
                        arrayList.get(position).put("isSelected", false)
                    }
                }
            }
            notifyDataSetChanged()
        }

    }

    override fun getItemCount(): Int {
        println("show count in Adapter =======>>>>>>>>>>> "+showitemcount)
        if (showitemcount.equals("3"))
        {
            return 3
        }
        else {
            return arrayList.size
        }
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }


    inner class DataObjectHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

        internal var tv_cat_name: TextView
        internal var ll_cat_cell: LinearLayout
        internal var tb_cat: CheckBox

        init {
            tv_cat_name = itemView.findViewById(R.id.tv_cat_name)
            ll_cat_cell = itemView.findViewById(R.id.ll_cat_cell)
            tb_cat = itemView.findViewById(R.id.checkbox)
        }
    }

    fun setOnItemClickListener(itemClickListener: OnItemClickListener) {
        this.itemClickListener = itemClickListener
    }

    interface OnItemClickListener {
        fun OnItemClick(position: Int)
    }

    fun showallItem()
    {
        showitemcount = "all"
        notifyDataSetChanged()
    }

    fun showlessItem()
    {
        showitemcount = "3"
        notifyDataSetChanged()
    }

}
