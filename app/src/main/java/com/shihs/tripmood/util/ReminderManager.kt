package com.shihs.tripmood.util
//
// import android.annotation.SuppressLint
// import android.app.AlarmManager
// import android.app.PendingIntent
// import android.content.Context
// import android.content.Intent
// import java.text.SimpleDateFormat
// import java.util.*
//
// object ReminderManager {
//    const val REMINDER_NOTIFICATION_REQUEST_CODE = 123
//
//    @SuppressLint("MissingPermission")
//    fun startReminder(
//        context: Context,
//        reminderTime: Long = 1000000000000,
//        reminderId: Int = REMINDER_NOTIFICATION_REQUEST_CODE
//    ) {
//        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
//
//        val intent =
//            Intent(context.applicationContext, AlarmReceiver::class.java).let { intent ->
//                PendingIntent.getBroadcast(
//                    context.applicationContext,
//                    reminderId,
//                    intent,
//                    PendingIntent.FLAG_UPDATE_CURRENT
//                )
//            }
//
//        val fmt = SimpleDateFormat("yyyy/MM/dd/HH/mm").format(reminderTime)
//
//        val (year, month, date, hour, minute) = fmt.split("/").map { it.toInt() }
//
//        val calendar: Calendar = Calendar.getInstance(Locale.getDefault()).apply {
//            set(Calendar.YEAR,year)
//            set(Calendar.MONTH + 1, month)
//            set(Calendar.DATE, date)
//            set(Calendar.HOUR_OF_DAY, hour)
//            set(Calendar.MINUTE, minute)
//        }
//
//        // If the trigger time you specify is in the past, the alarm triggers immediately.
//        // if soo just add one day to required calendar
//        // Note: i'm also adding one min cuz if the user clicked on the notification as soon as
//        // he receive it it will reschedule the alarm to fire another notification immediately
//        if (Calendar.getInstance(Locale.getDefault())
//                .apply { add(Calendar.MINUTE, 1) }.timeInMillis - calendar.timeInMillis > 0
//        ) {
//            calendar.add(Calendar.DATE, 1)
//        }
//
//        alarmManager.setAlarmClock(
//            AlarmManager.AlarmClockInfo(calendar.timeInMillis, intent),
//            intent
//        )
//
//
//    }
//
//    fun stopReminder(
//        context: Context,
//        reminderId: Int = REMINDER_NOTIFICATION_REQUEST_CODE
//    ) {
//        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
//        val intent = Intent(context, AlarmReceiver::class.java).let { intent ->
//            PendingIntent.getBroadcast(
//                context,
//                reminderId,
//                intent,
//                0
//            )
//        }
//        alarmManager.cancel(intent)
//    }
// }
