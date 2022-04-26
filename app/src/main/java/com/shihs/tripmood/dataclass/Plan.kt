package com.shihs.tripmood.dataclass

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Plan(
    var title: String? = null,
    var startDate: Long? = null,
    var endDate: Long? = null,
    var id: String? = null,
    var owner: User? = null,
    var friends: List<String>? = null
) : Parcelable


