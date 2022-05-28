package com.shihs.tripmood.dataclass

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class Chat(
    var speaker: User? = null,
    var msg: String? = null,
    var createdTime: Long? = null,
    var id: String? = null,
    var planID: String? = null
) : Parcelable
