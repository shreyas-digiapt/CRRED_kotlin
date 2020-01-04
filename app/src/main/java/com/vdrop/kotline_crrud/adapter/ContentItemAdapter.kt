package com.vdrop.kotline_crrud.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.util.Util
import com.vdrop.kotline_crrud.R
import com.vdrop.kotline_crrud.models.ContentModel
import com.vdrop.kotline_crrud.ui.ItemDetail
import com.vdrop.kotline_crrud.utiles.MyUtiles
import kotlinx.android.synthetic.main.single_snippet.view.*
import kotlin.random.Random



class ContentItemAdapter internal constructor(
    private val context: Context
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items = emptyList<ContentModel>()
    private val inflate = LayoutInflater.from(context)

    internal fun setItems(items: List<ContentModel>) {
        this.items = items
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = inflate.inflate(R.layout.single_snippet, parent, false)
        return ItemHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemHolder) {
            holder.bind(position)
        }
    }
    
       inner class ItemHolder constructor(itemView:View) : RecyclerView.ViewHolder(itemView) {

           val contentImage = itemView.iv_item
            val contentName = itemView.tv_name
            val contentShortDesc = itemView.tv_short_desc
            val contentAmount = itemView.tv_price
           val color:String? = MyUtiles.getRandomColor()

           fun bind(position: Int) {
               val model = items.get(position)
               MyUtiles.getGlideLoad(model)
               contentImage.setBackgroundColor(Color.parseColor(color))
               if(!model.image.imageContent.equals("")) {
                   val image = MyUtiles.getGlideLoad(model)
                   Log.d("uuytt", "getGlideLoad:  "+image)
                   Glide.with(context).load(image).listener(object: RequestListener<Drawable> {
                       override fun onLoadFailed(
                           e: GlideException?,
                           model: Any?,
                           target: Target<Drawable>?,
                           isFirstResource: Boolean
                       ): Boolean {
                           contentImage.setBackgroundColor(Color.parseColor(color))
                           return false
                       }

                       override fun onResourceReady(
                           resource: Drawable?,
                           model: Any?,
                           target: Target<Drawable>?,
                           dataSource: DataSource?,
                           isFirstResource: Boolean
                       ): Boolean {
                           contentImage.setBackgroundColor(Color.parseColor("#ffffff"))
                           return false
                       }

                   }).into(contentImage)
               }
               contentName.setText(model.name)
               contentShortDesc.setText(model.shortDesc)
               contentAmount.setText("₹ " + model.amount)

               itemView.cv_item.setOnClickListener( View.OnClickListener {
                   val intent = Intent(context, ItemDetail::class.java)
                   intent.putExtra("id", model.id.toString())
                   intent.putExtra("color", color)
                   context.startActivity(intent)
               })
           }

        }
}