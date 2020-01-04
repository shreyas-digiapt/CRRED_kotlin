package com.vdrop.kotline_crrud.utiles

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class ItemDecor(private val spaces: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.left = spaces
        outRect.right = spaces
        outRect.bottom = spaces
        
        if (parent.getChildAdapterPosition(view) == 0 || parent.getChildAdapterPosition(view) == 1) {
            outRect.top = spaces+spaces/2
        }
    }
}