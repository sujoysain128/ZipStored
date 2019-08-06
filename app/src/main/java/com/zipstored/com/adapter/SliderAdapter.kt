package com.zipstored.com.adapter


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.squareup.picasso.Picasso
import com.zipstored.com.R
import org.json.JSONObject
import java.util.*

/**
 * Created by SPECBEE on 8/4/2017.
 */

class SliderAdapter(private val context: Context, arrayList: ArrayList<JSONObject>) : PagerAdapter() {
    internal var arrayList = ArrayList<JSONObject>()

    init {
        this.arrayList = arrayList
    }

    override fun getCount(): Int {
        return arrayList.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        var iv_vp_img: ImageView

        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.item_slider, null)
        val viewPager = container as ViewPager
        viewPager.addView(view, 0)
        iv_vp_img = view.findViewById(R.id.iv_vp_img)
        val iamge_url = arrayList.get(position).getString("store_image_url")+"/h_500,w_500/"+arrayList.get(position).getString("store_image")
       // iv_vp_img.setImageResource(arrayList.get(position).getInt("PIC"))
        //iv_vp_img.setImageResource(arrayList.get(position).getString("image_url"))
        Picasso.get().load(iamge_url)
            .placeholder(R.mipmap.default_image_thumbnail).error(R.mipmap.default_image_thumbnail).into(iv_vp_img)

        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        val viewPager = container as ViewPager
        val view = `object` as View
        viewPager.removeView(view)
    }
}
