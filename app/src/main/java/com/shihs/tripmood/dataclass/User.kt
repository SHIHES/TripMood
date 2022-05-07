package com.shihs.tripmood.dataclass

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class User(
    var name: String? = null,
    var email: String? = null,
    var image: String? = null,
    var id: String? = null,
    var myPlanID: List<String> = emptyList(),
    var collectionPlanID: List<String> = emptyList(),
): Parcelable
