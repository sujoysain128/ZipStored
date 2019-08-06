package com.liquorworldkotlin.adapter

import android.content.Context
import android.os.Bundle
import android.os.Handler
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.squareup.picasso.Picasso
import com.zipstored.com.R
import org.json.JSONException
import org.json.JSONObject

import java.util.ArrayList
import android.widget.Toast
import com.squareup.picasso.Callback
import java.lang.Exception


/**
 * Created by User on 8/18/2016.
 */
class ProviderStorageListAdapter(internal var context: Context, arrayList: ArrayList<JSONObject>) :
    androidx.recyclerview.widget.RecyclerView.Adapter<androidx.recyclerview.widget.RecyclerView.ViewHolder>() {
    internal lateinit var updateData: UpdateData
    internal var upDateAgain = true
    internal var updateLoader = true
    internal lateinit var parent: ViewGroup

    internal var arrayList = ArrayList<JSONObject>()
    internal var itemClickListener: OnItemClickListener? = null
    //internal lateinit var mySharedPreferance: MySharedPreferance

    init {
        this.arrayList = arrayList
    }

    fun paginate(updateData: UpdateData) {
        this.updateData = updateData
    }

    fun updateAgain(upDateAgain: Boolean) {
        this.upDateAgain = upDateAgain
    }

    fun loader(updateLoader: Boolean) {
        this.updateLoader = updateLoader
    }

    interface UpdateData {
        operator fun get(position: Int)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): androidx.recyclerview.widget.RecyclerView.ViewHolder {
        when (viewType) {
            NORMAL -> {
                this.parent = parent
                val view = LayoutInflater.from(parent.context).inflate(R.layout.child_provider_list, parent, false)
                return DataObjectHolder(view)
            }
            FOOTER -> {
                val view1 = LayoutInflater.from(parent.context).inflate(R.layout.pagination_loader, parent, false)
                return FooterProgressbar(view1)
            }
            else -> throw IllegalStateException("Unknown view type: $viewType")
        }
    }

    override fun onBindViewHolder(holder: androidx.recyclerview.widget.RecyclerView.ViewHolder, position: Int) {

        println("list position======================>$position")
        //mySharedPreferance = MySharedPreferance(context)

        if (holder is FooterProgressbar) {
            if (updateLoader) {
                holder.ll_loader.visibility = View.GONE
            } else {
                holder.ll_loader.visibility = View.GONE
            }
        } else {
            if (position > 0 && position == arrayList.size - 5) {
                println("the Position pgnton======================>$upDateAgain")
                if (upDateAgain) {
                    println("the Position pgnton exact======================>$position")
                    updateData[arrayList.size]
                }
            }

            try {
                (holder as DataObjectHolder).tv_storage_title.setText(arrayList.get(position).getString("storage_title"))
                holder.tv_storage_type_name.setText(arrayList.get(position).getString("storage_type_name"))
                holder.tv_address.setText(arrayList.get(position).getString("address") + " ")
                holder.tv_price.setText("$ " + arrayList.get(position).getString("min_price"))
                holder.tv_total_space.setText(arrayList.get(position).getString("total_area") + "ft")

                val number2digits: Double =
                    String.format("%.1f", arrayList.get(position).getString("distance").toDouble()).toDouble()
                holder.tv_distance.setText("(" + number2digits.toString() + " miles away)")


                val iamge_url = arrayList.get(position).getString("image_path")+"/h_700,w_700/"+arrayList.get(position).getString("location_image")
                println("Image path ==========>>>>>>>> "+iamge_url)







                holder.ll_rating_layout.visibility = View.VISIBLE
                holder.ll_no_rating_layout.visibility = View.GONE
                holder.iv_1.visibility  = View.GONE
                holder.iv_2.visibility  = View.GONE
                holder.iv_3.visibility  = View.GONE
                holder.iv_4.visibility  = View.GONE
                holder.iv_5.visibility  = View.GONE

                if (arrayList.get(position).getString("average_ratings").toString().equals("1")) {
                    holder.iv_1.visibility  = View.VISIBLE
                }
                if (arrayList.get(position).getString("average_ratings").toString().equals("2")) {
                    holder.iv_1.visibility  = View.VISIBLE
                    holder.iv_2.visibility  = View.VISIBLE
                }
                if (arrayList.get(position).getString("average_ratings").toString().equals("3")) {
                    holder.iv_1.visibility  = View.VISIBLE
                    holder.iv_2.visibility  = View.VISIBLE
                    holder.iv_3.visibility  = View.VISIBLE
                }
                if (arrayList.get(position).getString("average_ratings").toString().equals("4")) {
                    holder.iv_1.visibility  = View.VISIBLE
                    holder.iv_2.visibility  = View.VISIBLE
                    holder.iv_3.visibility  = View.VISIBLE
                    holder.iv_4.visibility  = View.VISIBLE
                }
                if (arrayList.get(position).getString("average_ratings").toString().equals("5")) {
                    holder.iv_1.visibility  = View.VISIBLE
                    holder.iv_2.visibility  = View.VISIBLE
                    holder.iv_3.visibility  = View.VISIBLE
                    holder.iv_4.visibility  = View.VISIBLE
                    holder.iv_5.visibility  = View.VISIBLE
                }
                if (arrayList.get(position).getString("average_ratings").toString().equals("0")) {
                    holder.ll_rating_layout.visibility = View.GONE
                    holder.ll_no_rating_layout.visibility = View.VISIBLE
                }


                Picasso.get().load(iamge_url).placeholder(R.mipmap.default_image_thumbnail).error(R.mipmap.default_image_thumbnail).into(holder.iv_thumb)

                /*Picasso.get().load(iamge_url).error(R.mipmap.ic_launcher)
                    .into(holder.iv_thumb, object : Callback {
                        override fun onSuccess() {
                            println("Picasso success")
                        }

                        override fun onError(e: Exception?) {
                            println("Picasso onError ==> "+e)
                        }

                    })*/

            holder.rr_root.setOnClickListener {

                if (itemClickListener != null) {
                    itemClickListener!!.OnItemClick(position.toString(), arrayList.get(position).getString("location_id"))
                }
            }


            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }

    }


    override fun getItemCount(): Int {
        return arrayList.size + 1
        //return 20
    }

    override fun getItemViewType(position: Int): Int {
        return if (isPositionFooter(position)) {
            FOOTER
        } else NORMAL
    }

    private fun isPositionFooter(position: Int): Boolean {
        return position == arrayList.size
    }

    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }


    inner class DataObjectHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

        internal var iv_thumb: ImageView
        internal var tv_storage_title: TextView
        internal var tv_storage_type_name: TextView
        internal var tv_address: TextView
        internal var tv_price: TextView
        internal var tv_total_space: TextView
        internal var tv_distance: TextView
        internal var tv_no_reviews: TextView
        internal var ll_rating_layout: LinearLayout
        internal var iv_1: ImageView
        internal var iv_2: ImageView
        internal var iv_3: ImageView
        internal var iv_4: ImageView
        internal var iv_5: ImageView
        internal var ll_no_rating_layout: LinearLayout
        internal var rr_root: RelativeLayout

        init {
            iv_thumb = itemView.findViewById(R.id.iv_thumb)
            tv_storage_title = itemView.findViewById(R.id.tv_storage_title)
            tv_storage_type_name = itemView.findViewById(R.id.tv_storage_type_name)
            tv_address = itemView.findViewById(R.id.tv_address)
            tv_price = itemView.findViewById(R.id.tv_price)
            tv_total_space = itemView.findViewById(R.id.tv_total_space)
            tv_distance = itemView.findViewById(R.id.tv_distance)
            ll_rating_layout = itemView.findViewById(R.id.ll_rating_layout)
            tv_no_reviews = itemView.findViewById(R.id.tv_no_reviews)
            iv_1 = itemView.findViewById(R.id.iv_1)
            iv_2 = itemView.findViewById(R.id.iv_2)
            iv_3 = itemView.findViewById(R.id.iv_3)
            iv_4 = itemView.findViewById(R.id.iv_4)
            iv_5 = itemView.findViewById(R.id.iv_5)
            ll_no_rating_layout = itemView.findViewById(R.id.ll_no_rating_layout)
            rr_root = itemView.findViewById(R.id.rr_root)
        }
    }

    inner class FooterProgressbar(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

        internal var ll_footer: LinearLayout
        internal var ll_loader: LinearLayout


        init {
            ll_footer = itemView.findViewById(R.id.ll_footer)
            ll_loader = itemView.findViewById(R.id.ll_loader)

        }
    }

    fun setOnItemClickListener(itemClickListener: OnItemClickListener) {
        this.itemClickListener = itemClickListener
    }

    interface OnItemClickListener {
        fun OnItemClick(position: String, order_id: String)
        //fun OnItemClickRate(position: Int, order_id: String)
    }

    companion object {

        val NORMAL = 1
        val FOOTER = 2
    }
}
