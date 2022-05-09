package com.shihs.tripmood.dataclass

import android.os.Parcelable
import com.shihs.tripmood.util.UserManager
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Plan(
    var title: String? = "",
    var startDate: Long? = null,
    var endDate: Long? = null,
    var id: String? = "",
    var ownerID: String? = null,
    var coworkList: List<User>? = emptyList(),
    var private: Boolean = true,
    var status: Int = 0,
    var image: String = ""
) : Parcelable



