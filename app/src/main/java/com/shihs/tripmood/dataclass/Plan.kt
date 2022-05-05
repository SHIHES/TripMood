package com.shihs.tripmood.dataclass

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Plan(
    var title: String? = "",
    var startDate: Long? = null,
    var endDate: Long? = null,
    var id: String? = "",
    var owner: User? = null,
    var friends: List<String>? = null,
    var private: Boolean = true,
    var status: Int = 0
) : Parcelable


