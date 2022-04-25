package app.appworks.school.publisher.ext

import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

/**
 * Created by Wayne Chen on 2020-01-15.
 */
fun Long.toDisplayFormat(): String {
    return SimpleDateFormat("yyyy.MM.dd hh:mm", Locale.TAIWAN).format(this)
}

fun addData() {
    val articles = FirebaseFirestore.getInstance()
        .collection("articles")

    val document = articles.document()

    val data = hashMapOf(
        "author" to hashMapOf(
            "email" to "wayne@school.appworks.tw",
            "id" to "waynechen323",
            "name" to "AKA小安老師"
        ),
        "title" to "IU「亂穿」竟美出新境界！笑稱自己品味奇怪　網笑：靠顏值撐住女神氣場",
        "content" to "南韓歌手IU（李知恩）無論在歌唱方面或是近期的戲劇作品都有亮眼的成績，但俗話說人無完美、美玉微瑕，曾再跟工作人員的互動影片中坦言自己品味很奇怪，近日在IG上分享了宛如「媽媽們青春時代的玉女歌手」超復古穿搭造型，卻意外美出新境界。",
        "createdTime" to Calendar.getInstance().timeInMillis,
        "id" to document.id,
        "tag" to "Beauty"
    )
    document.set(data)
}