package com.shihs.tripmood.util

interface ItemTouchHelperInterface {

    fun onItemMove(fromPosition: Int, toPosition: Int)

    fun onItemDelete(position: Int)

}