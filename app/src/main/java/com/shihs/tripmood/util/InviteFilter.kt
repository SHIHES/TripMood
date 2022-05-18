package com.shihs.tripmood.util


enum class InviteFilter(val value: String, val status: Int) {
    WAITING("等待中",0),
    APPROVAL("已接受",1),
    REFUSED("已拒絕",2)
}