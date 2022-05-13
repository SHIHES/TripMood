package com.shihs.tripmood.util
//
//import android.app.NotificationManager
//import android.app.PendingIntent
//import android.content.BroadcastReceiver
//import android.content.Context
//import android.content.Intent
//import androidx.core.app.NotificationCompat
//import androidx.core.content.ContextCompat
//import com.shihs.tripmood.MainActivity
//import com.shihs.tripmood.R
//
//class AlarmReceiver : BroadcastReceiver() {
//    /**
//     * sends notification when receives alarm
//     * and then reschedule the reminder again
//     * */
//    override fun onReceive(context: Context, intent: Intent) {
//        val notificationManager = ContextCompat.getSystemService(
//            context,
//            NotificationManager::class.java
//        ) as NotificationManager
//
//        notificationManager.sendReminderNotification(
//            applicationContext = context,
//            channelId = context.getString(R.string.reminders_notification_channel_id)
//        )
//        // Remove this line if you don't want to reschedule the reminder
//        ReminderManager.startReminder(context.applicationContext)
//    }
//}
//
//fun NotificationManager.sendReminderNotification(
//    applicationContext: Context,
//    channelId: String,
//) {
//    val contentIntent = Intent(applicationContext, MainActivity::class.java)
//    val pendingIntent = PendingIntent.getActivity(
//        applicationContext,
//        1,
//        contentIntent,
//        PendingIntent.FLAG_UPDATE_CURRENT
//    )
//    val builder = NotificationCompat.Builder(applicationContext, channelId)
//        .setContentTitle(applicationContext.getString(R.string.title_notification_reminder))
//        .setContentText(applicationContext.getString(R.string.description_notification_reminder))
//        .setSmallIcon(R.drawable.ic_baseline_location_on_24)
//        .setStyle(
//            NotificationCompat.BigTextStyle()
//                .bigText(applicationContext.getString(R.string.description_notification_reminder))
//        )
//        .setContentIntent(pendingIntent)
//        .setAutoCancel(true)
//
//    notify(NOTIFICATION_ID, builder.build())
//}
//
//const val NOTIFICATION_ID = 1
