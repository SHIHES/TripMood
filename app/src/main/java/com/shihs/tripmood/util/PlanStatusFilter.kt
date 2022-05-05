package com.shihs.tripmood.util

enum class PlanStatusFilter(val value: String, val code: Int){
    PLANNING("規劃中", 0),
    ONGOING("進行中", 1),
    END("回憶", 2)
}