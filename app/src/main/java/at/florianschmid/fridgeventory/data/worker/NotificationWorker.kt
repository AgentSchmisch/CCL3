package at.florianschmid.fridgeventory

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import at.florianschmid.fridgeventory.data.ItemRepository
import at.florianschmid.fridgeventory.data.db.ItemDatabase.Companion.getDatabase

class ExpiryNotificationWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    private val repository: ItemRepository

    init {
        Log.i("Worker", "Worker created")
        val database = getDatabase(context)

        val itemDao = database.itemDao()

        // Pass ItemDao to the repository
        repository = ItemRepository(itemDao)
    }

    override suspend fun doWork(): Result {
        val expiringItems = repository.findItemsExpiringSoon()

        if (expiringItems.isNotEmpty()) {
            // if there is only 1 item expiring soon
            if(expiringItems.size == 1) {
                showNotification("Item Expiring Soon", "The following item is expiring soon: ${expiringItems[0].name}")
            }

            // if there are more than 1 but less than 4 items expiring soon
            else if(expiringItems.size < 4) {
                val itemNames = expiringItems.joinToString(", ") { it.name }
                showNotification("Items Expiring Soon", "The following items are expiring soon: $itemNames")
            }
            // if there are 4 or more items expiring soon
            else {
                val itemNames = expiringItems.joinToString(", ") { it.name }
                showNotification("Items Expiring Soon", "The following items are expiring soon: $itemNames + ${expiringItems.size - 3} more")
            }
        }
        return Result.success()
    }

    private fun showNotification(title: String, message: String) {
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "expiry_notifications"

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Expiry Notifications",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        notificationManager.notify(1, notification)
    }
}
