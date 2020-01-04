package com.vdrop.kotline_crrud.ui

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.vdrop.kotline_crrud.R
import com.vdrop.kotline_crrud.ViewModel.ContentViewModel
import com.vdrop.kotline_crrud.models.ContentModel
import com.vdrop.kotline_crrud.utiles.FabAnimation
import com.vdrop.kotline_crrud.utiles.MyUtiles
import kotlinx.android.synthetic.main.activity_item_detail.*

class ItemDetail : AppCompatActivity(), View.OnClickListener {

    var viewModel:ContentViewModel? = null
    var itemId:String? = null
    var color:String? = null
    var isRotate:Boolean = false
    var model:ContentModel? = null

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.statusBarColor = Color.TRANSPARENT
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        setContentView(R.layout.activity_item_detail)

        itemId = intent.extras?.getString("id")
        color = intent.extras?.getString("color")
        iv_content_image.setBackgroundColor(Color.parseColor(color))

        iv_back.setOnClickListener(this)
        content_fab.setOnClickListener(this)
        fab_edit.setOnClickListener(this)
        fab_delete.setOnClickListener(this)

        FabAnimation.init(fab_edit)
        FabAnimation.init(fab_delete)

    }

    override fun onStart() {
        super.onStart()
        setData()
    }

    private fun setData() {
        viewModel = ViewModelProvider.AndroidViewModelFactory(this.application).create(ContentViewModel::class.java)
       viewModel!!.getParicularItem(itemId!!.toInt()).observe(this, Observer {items ->
           items?.let {
               model = it
               if (!it.image.imageContent.equals("")) {
                   val image = MyUtiles.getGlideLoad(it)
                   Glide.with(this@ItemDetail).load(image).listener(object : RequestListener<Drawable> {
                       override fun onLoadFailed(
                           e: GlideException?,
                           model: Any?,
                           target: Target<Drawable>?,
                           isFirstResource: Boolean
                       ): Boolean {
                           iv_content_image.setBackgroundColor(Color.parseColor(color))
                           return false
                       }

                       override fun onResourceReady(
                           resource: Drawable?,
                           model: Any?,
                           target: Target<Drawable>?,
                           dataSource: DataSource?,
                           isFirstResource: Boolean
                       ): Boolean {
                           iv_content_image.setBackgroundColor(Color.parseColor("#ffffff"))
                           return false
                       }

                   }).into(iv_content_image)
               }
               tv_content_title.setText(it.name)
               tv_content_desc.setText(it.longDesc)
               tv_content_amount.setText("₹ " +it.amount)
               tv_content_quantity.setText("Qty:"+it.quantity)
           }

       })
    }

    override fun onClick(v: View?) {
        val id = v?.id
        when(id) {
            R.id.iv_back -> {finish()}

            R.id.content_fab -> {
                setAnimation(v)
            }

            R.id.fab_delete -> {
                deleteItem(itemId!!.toInt())
            }

            R.id.fab_edit -> {
                hideView()

                updateItem(model!!)
            }
        }
    }

    private fun hideView() {
        appbarLayout.visibility = View.GONE
        fab_delete.hide()
        fab_edit.hide()
        content_fab.hide()
    }

    private fun updateItem(model:ContentModel) {
        val transaction = supportFragmentManager.beginTransaction()
        var fragment = CreateFragment()
        fragment.setModel(model)
        transaction.add(R.id.edit_item_container, fragment)
        transaction.commit()
    }

    private fun deleteItem(id:Int) {
        viewModel = ViewModelProvider.AndroidViewModelFactory(this.application).create(ContentViewModel::class.java)
        viewModel!!.deleteItem(id)
        finish()
    }

    private fun setAnimation(v: View) {
        isRotate = FabAnimation.rotateFab(v, !isRotate)
        if (isRotate) {
            FabAnimation.showIn(fab_delete)
            FabAnimation.showIn(fab_edit)
        }else {
            FabAnimation.showOut(fab_edit)
            FabAnimation.showOut(fab_delete)
        }
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {

        }else {
            super.onBackPressed()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            1 -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.size > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                } else { //if permission is made dont show again
                    if (ActivityCompat.shouldShowRequestPermissionRationale(
                            this@ItemDetail,
                            Manifest.permission.READ_CALL_LOG
                        )
                    ) { // now, user has denied permission (but not permanently!)
                    } else {
                        MyUtiles.permissionError(this@ItemDetail)
                    }
                }
                return
            }
        }
    }
}
