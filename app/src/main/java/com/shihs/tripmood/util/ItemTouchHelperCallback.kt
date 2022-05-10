package com.shihs.tripmood.util

import android.content.Context
import android.graphics.Canvas
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class ItemTouchHelperCallback(var adapter: ItemTouchHelperInterface, context: Context) : ItemTouchHelper.Callback() {

    private val limitScrollX = dipToPx(100f, context)
    private var currentSrollX = 0
    private var currentScrollXwhenInActive = 0
    private var initXWhenInActive = 0f
    private var firstInActive = false


    override fun isItemViewSwipeEnabled(): Boolean {

        return true
    }


    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {

        val dragFlags = 0

        val swipeFlags = ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT

        return makeMovementFlags(dragFlags, swipeFlags)
    }

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {

//        adapter.onItemMove(viewHolder.adapterPosition, target.adapterPosition)

        return true
    }

    override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
        return Integer.MAX_VALUE.toFloat()
    }

    override fun getSwipeVelocityThreshold(defaultValue: Float): Float {
        return Integer.MAX_VALUE.toFloat()
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {

        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE){
            if(dX == 0f){
                currentSrollX = viewHolder.itemView.scrollX
                firstInActive = true
            }

            if(isCurrentlyActive){

                var scrollOffset = currentSrollX + (-dX).toInt()
                if(scrollOffset > limitScrollX){
                    scrollOffset = limitScrollX
                } else if (scrollOffset < 0){
                    scrollOffset = 0
                }

                viewHolder.itemView.scrollTo(scrollOffset, 0)
            } else {
                if(firstInActive){
                    firstInActive = false
                    currentScrollXwhenInActive = viewHolder.itemView.scrollX
                    initXWhenInActive = dX
                }
                if (viewHolder.itemView.scrollX < limitScrollX){
                    viewHolder.itemView.scrollTo((currentScrollXwhenInActive * dX / initXWhenInActive).toInt(),0)
                }
            }
        }
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)

        if (viewHolder.itemView.scrollX > limitScrollX){
            viewHolder.itemView.scrollTo(limitScrollX, 0)
        } else if (viewHolder.itemView.scrollX < 0){
            viewHolder.itemView.scrollTo(0, 0)
        }
    }



    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        adapter.onItemDelete(viewHolder.adapterPosition)
    }

    private fun dipToPx(dipValue: Float, context: Context): Int{
        return  (dipValue * context.resources.displayMetrics.density).toInt()
    }
}

