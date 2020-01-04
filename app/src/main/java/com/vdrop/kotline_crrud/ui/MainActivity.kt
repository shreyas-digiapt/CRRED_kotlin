package com.vdrop.kotline_crrud.ui

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vdrop.kotline_crrud.R
import com.vdrop.kotline_crrud.ViewModel.ContentViewModel
import com.vdrop.kotline_crrud.adapter.ContentItemAdapter
import com.vdrop.kotline_crrud.utiles.ItemDecor
import com.vdrop.kotline_crrud.utiles.MyUtiles
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener {


   private lateinit var rvContent: RecyclerView
    private lateinit var viewModel: ContentViewModel


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.statusBarColor = Color.TRANSPARENT
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        setContentView(R.layout.activity_main)

        MyUtiles.checkReadPermission(this)

        fab.setOnClickListener(this)

        initAdpater()

    }

    override fun onStart() {
        super.onStart()
        setAdapter()
    }

    private fun initAdpater() {
        rvContent = findViewById<RecyclerView>(R.id.rv_content)
        rvContent.layoutManager = GridLayoutManager(this@MainActivity, 2)
        rvContent.addItemDecoration(ItemDecor(30))
    }

    private fun setAdapter() {
        val adapter = ContentItemAdapter(this@MainActivity)
        viewModel = ViewModelProvider.AndroidViewModelFactory(this.application).create(ContentViewModel::class.java)
        viewModel.getAllItems.observe(this, Observer {items ->
            items?.let {
                adapter.setItems(it)
                if (it.size > 0) {
                    tv_empty.visibility = View.GONE
                }else {
                    tv_empty.visibility = View.VISIBLE
                }
            }
        })
        rvContent.adapter = adapter
    }

    override fun onClick(v: View?) {
        val id = v?.id
        when(id) {
            R.id.fab -> {
                fab.hide()
                val transaction = supportFragmentManager.beginTransaction()
                transaction.add(R.id.frame_container, CreateFragment())
                transaction.commit()
            }
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
                            this@MainActivity,
                            Manifest.permission.READ_CALL_LOG
                        )
                    ) { // now, user has denied permission (but not permanently!)
                    } else {
                        MyUtiles.permissionError(this@MainActivity)
                    }
                }
                return
            }
        }
    }


}
