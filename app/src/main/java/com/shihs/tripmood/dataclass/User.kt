package com.shihs.tripmood.dataclass

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    var name: String? = null,
    var email: String? = null,
    var image: String? = null,
    var uid: String? = null,
    var favoritePlansID: List<String> = emptyList(),
) : Parcelable
