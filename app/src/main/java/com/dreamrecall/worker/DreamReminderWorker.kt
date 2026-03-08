package com.dreamrecall.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.dreamrecall.data.DreamDatabase
import com.dreamrecall.data.DreamRepository

class DreamReminderWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        val database = DreamDatabase.getDatabase(applicationContext)
        val repository = DreamRepository(database.dreamDao())
        val dreamToShow = repository.getLeastRecentlyShownDream() ?: return Result.success()

        createNotificationChannel()

        val intent = Intent(applicationContext, Class.forName("com.dreamrecall.MainActivity")).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("SHOW_DREAM_ID", dreamToShow.id)
        }

        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            applicationContext, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Remember this dream?")
            .setContentText(dreamToShow.title)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setColor(android.graphics.Color.parseColor("#1A0033"))

        try {
            NotificationManagerCompat.from(applicationContext).notify(NOTIFICATION_ID, builder.build())
            repository.markDreamAsShown(dreamToShow.id)
        } catch (e: SecurityException) {
            e.printStackTrace()
        }

        return Result.success()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Dream Recall Reminders"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance)
            val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object {
        const val CHANNEL_ID = "dream_recall_channel"
        const val NOTIFICATION_ID = 1001
    }
}
