package com.vdrop.kotline_crrud.ui


import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.vdrop.kotline_crrud.R
import com.vdrop.kotline_crrud.ViewModel.ContentViewModel
import com.vdrop.kotline_crrud.models.ContentModel
import com.vdrop.kotline_crrud.models.SaveImage
import com.vdrop.kotline_crrud.utiles.MyUtiles
import kotlinx.android.synthetic.main.fragment_create.*
import kotlinx.android.synthetic.main.link_layout.*
import java.util.logging.Logger

/**
 * A simple [Fragment] subclass.
 */
class CreateFragment : Fragment(), View.OnClickListener {

    private var model: ContentModel? = null
    private var viewModel: ContentViewModel? = null
    private var imageContent: String? = ""
    private var imageSource: String? = ""
    private final val CAMERA_PIC_REQUEST:Int = 2
    private final val GALLERY_PIC_REQUEST:Int = 1

    private var mBtnbtnGallery:Button? = null
    private var mBtnLink:Button? = null
    private var mBtnCamera:Button? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_create, container, false)
        initData(view)
        return view
    }

    internal fun setModel(model: ContentModel) {
        this.model = model
    }

    private fun initData(view: View?) {
        mBtnbtnGallery = view?.findViewById<Button>(R.id.btn_choose_gallery)
        mBtnLink = view?.findViewById<Button>(R.id.btn_link)
        mBtnCamera = view?.findViewById<Button>(R.id.btn_camera)
        view?.findViewById<Button>(R.id.btn_post)?.setOnClickListener(this)
        view?.findViewById<Button>(R.id.btn_cancel)?.setOnClickListener(this)

        mBtnbtnGallery?.setOnClickListener(this)
        mBtnCamera?.setOnClickListener(this)
        mBtnLink?.setOnClickListener(this)
        setUpdateData(view);
    }

    private fun setUpdateData(view: View?) {
        if (model != null) {
            view?.findViewById<TextView>(R.id.textView)?.setText("Update Item")
            view?.findViewById<EditText>(R.id.et_item_name)?.setText(model!!.name)
            view?.findViewById<EditText>(R.id.et_item_short_desc)?.setText(model!!.shortDesc)
            view?.findViewById<EditText>(R.id.et_item_long_desc)?.setText(model!!.longDesc)
            view?.findViewById<EditText>(R.id.et_item_amount)?.setText(model!!.amount)
            view?.findViewById<EditText>(R.id.et_item_quantity)?.setText(model!!.quantity)
            imageContent = model!!.image.imageContent
            view?.findViewById<TextView>(R.id.btn_post)?.setText("Update")
            setButtonColor(model!!.image)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun setButtonColor(saveImage: SaveImage) {
        if (!saveImage.imageSource.equals("")) {
            if (saveImage.imageSource.equals(getString(R.string.camera))) {
                setColor(mBtnCamera!!, mBtnbtnGallery!!, mBtnLink!!)
            } else if (saveImage.imageSource.equals(getString(R.string.gallery))) {
                setColor(mBtnbtnGallery!!, mBtnCamera!!, mBtnLink!!)
            } else if (saveImage.imageSource.equals(getString(R.string.link))) {
                setColor(mBtnLink!!, mBtnCamera!!, mBtnbtnGallery!!)
            }
        }

    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onClick(v: View?) {
        val id = v?.id
        Logger.getLogger("qwvbn").warning(id.toString())
        when (id) {

            R.id.btn_cancel -> {
                Logger.getLogger("qwvbn").warning("close")
                activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
                activity?.recreate()
            }
            R.id.btn_choose_gallery -> {
                openGallery()
            }
            R.id.btn_camera -> {
                openCamera()
            }
            R.id.btn_link -> {
                setAlertDialog()
            }
            R.id.btn_post -> {
                if (btn_post.text.toString().equals("POST")) {
                    createItem(v)
                } else {
                    updateItem(model)

                }
            }

        }
    }

    private fun openCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST)
    }

    private fun openGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, getString(R.string.pick_image_title)), GALLERY_PIC_REQUEST)
    }

    private fun updateItem(model1: ContentModel?) {
        if (et_item_name.text.toString().equals("") || et_item_quantity.text.toString().equals("") || et_item_quantity.text.toString().equals("")) {
            et_item_name.setError(getString(R.string.empty_string))
            et_item_quantity.setError(getString(R.string.empty_string))
            et_item_amount.setError(getString(R.string.empty_string))
        } else {
            viewModel = ViewModelProvider.AndroidViewModelFactory(this.activity!!.application)
                .create(ContentViewModel::class.java)
            val model = ContentModel(
                SaveImage(imageContent!!, imageSource!!),
                et_item_name.text.toString(),
                et_item_short_desc.text.toString(),
                et_item_long_desc.text.toString(),
                et_item_amount.text.toString(),
                et_item_quantity.text.toString()
            )
            model.id = model1!!.id
            viewModel!!.updateItem(model)
            closeFragment()
        }

    }

    private fun createItem(v: View) {
        Log.d("empty","123   "+(et_item_name.text.toString().equals("")))
        if (model == null) {
            Logger.getLogger("empty13").warning(et_item_name.text.toString())
            if (et_item_name.text.toString().equals("") || et_item_quantity.text.toString().equals("") || et_item_quantity.text.toString().equals("")) {
                et_item_name.setError(getString(R.string.empty_string))
                et_item_quantity.setError(getString(R.string.empty_string))
                et_item_amount.setError(getString(R.string.empty_string))
            } else {
                viewModel = ViewModelProvider.AndroidViewModelFactory(this.activity!!.application)
                    .create(ContentViewModel::class.java)
                Log.d("qaz12",""+imageSource)
                val model = ContentModel(
                    SaveImage(imageContent!!, imageSource!!),
                    et_item_name.text.toString(),
                    et_item_short_desc.text.toString(),
                    et_item_long_desc.text.toString(),
                    et_item_amount.text.toString(),
                    et_item_quantity.text.toString()
                )
                viewModel!!.insertItem(model)
                closeFragment()
            }
        }
    }

    private fun closeFragment() {
        activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
        activity?.recreate()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun setAlertDialog() {
        val builder = AlertDialog.Builder(activity)
        val alertView = activity?.layoutInflater?.inflate(R.layout.link_layout, null)
        builder.setCancelable(false)
        builder.setView(alertView)
        val dialog = builder.create()
        val editText = alertView?.findViewById<EditText>(R.id.et_link)
        if (model != null && model?.image?.imageSource.equals("link")) {
            editText?.setText(imageContent)
        }
        alertView?.findViewById<Button>(R.id.btn_link_cancel)
            ?.setOnClickListener(View.OnClickListener {
                dialog.dismiss()
            })
        alertView?.findViewById<Button>(R.id.btn_link_ok)?.setOnClickListener(View.OnClickListener {
            Logger.getLogger("qwerty").warning(et_link?.text.toString())
            if (editText?.text.toString().equals("")) {
                Toast.makeText(context, "Enter the link", Toast.LENGTH_SHORT).show()
            } else {
                setColor(mBtnLink!!, mBtnCamera!!, mBtnbtnGallery!!)
                imageContent = editText?.text.toString()
                imageSource = getString(R.string.link)
                dialog.dismiss()
            }
        })
        dialog.show()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.d("code123", ""+requestCode+"  "+resultCode)
        try {
            if (requestCode == GALLERY_PIC_REQUEST && resultCode == Activity.RESULT_OK) {
                val imageUri = data!!.data
                imageContent = MyUtiles.getPath(activity!!.applicationContext, imageUri)
                Log.d("code123", ""+imageContent)
                imageSource = getString(R.string.gallery)
                setColor(mBtnbtnGallery!!, mBtnLink!!, mBtnCamera!!)
            } else if (requestCode == CAMERA_PIC_REQUEST) {
                val image = data!!.extras!!["data"] as Bitmap?
                imageContent = MyUtiles.bitMapToString(image!!)
                imageSource = getString(R.string.camera)
                setColor(mBtnCamera!!, mBtnLink!!, mBtnbtnGallery!!)
            }
        } catch (e: Exception) {
            Log.d("code123", "Exception: " + e.message)
            MyUtiles.checkReadPermission(context)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun setColor(btn1: Button, btn2: Button, btn3: Button
    ) {
        btn1.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.fab, null))
        btn2.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.grey, null))
        btn3.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.grey, null))
    }
}
