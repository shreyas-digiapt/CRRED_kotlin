package com.vdrop.kotline_crrud.utiles

import android.Manifest
import android.R
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.provider.Settings
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.core.app.ActivityCompat
import com.google.android.material.snackbar.Snackbar
import com.vdrop.kotline_crrud.BuildConfig
import com.vdrop.kotline_crrud.models.ContentModel
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.*

class MyUtiles {

    companion object {

        fun getRandomColor(): String? {
            val colors = arrayOf(
                "#BC8F8F",
                "#F4A460",
                "#FAEBD7",
                "#7B68EE",
                "#FFA500",
                "#FFFF00",
                "#228B22",
                "#4169E1"
            )
            val random = Random()
            return colors[random.nextInt(7)]
        }

        fun checkReadPermission(context: Context?) {
            ActivityCompat.requestPermissions(
                (context as Activity?)!!,
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA
                ),
                1
            )
        }

        fun getGlideLoad(model: ContentModel): Any? {
            val imageSource: String = model.image.imageSource
            val imageContent: String = model.image.imageContent
            Log.d("uuyt", "getGlideLoad: $imageSource")
            if (imageSource == "link") {
                return imageContent
            } else if (imageSource == "camera") {
                return MyUtiles.stringToBitMap(imageContent)
            } else if (imageSource == "gallery") {
                Log.d("aasd",""+File(Uri.parse(imageContent).path))
                return File(Uri.parse(imageContent).path)
            }
            return ""
        }

        fun bitMapToString(bitmap: Bitmap): String? {
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
            val b = baos.toByteArray()
            return Base64.encodeToString(b, Base64.DEFAULT)
        }

        private fun stringToBitMap(encodedString: String): Bitmap? {
            return try {
                val encodeByte =
                    Base64.decode(encodedString, Base64.DEFAULT)
                BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.size)
            } catch (e: Exception) {
                e.message
                null
            }
        }

        fun permissionError(activity: Activity) {
            val snackbar = Snackbar.make(
                activity.findViewById(R.id.content),
                activity.getString(com.vdrop.kotline_crrud.R.string.status_color),
                Snackbar.LENGTH_LONG
            )
                .setActionTextColor(Color.parseColor(activity.getString(com.vdrop.kotline_crrud.R.string.status_color)))
                .setAction("Settings") {
                    activity.startActivity(
                        Intent(
                            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.parse("package:" + BuildConfig.APPLICATION_ID)
                        )
                    )
                }
            val snackbarView = snackbar.view
            val textView =
                snackbarView.findViewById<View>(com.google.android.material.R.id.snackbar_text) as TextView
            textView.maxLines = 5 //Or as much as you need
            snackbar.show()
        }

        fun getPath(context: Context, uri: Uri?): String? {
            var filePath = ""
            val wholeID = DocumentsContract.getDocumentId(uri)
            // Split at colon, use second item in the array
            val id = wholeID.split(":").toTypedArray()[1]
            val column = arrayOf(
                MediaStore.Images.Media.DATA
            )
            // where id is equal to
            val sel = MediaStore.Images.Media._ID + "=?"
            val cursor = context.contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                column, sel, arrayOf(id), null
            )
            val columnIndex = cursor!!.getColumnIndex(column[0])
            if (cursor.moveToFirst()) {
                filePath = cursor.getString(columnIndex)
            }
            cursor.close()
            return filePath
        }
    }


}