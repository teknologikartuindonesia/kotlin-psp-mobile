package id.co.pspmobile.data.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.AudioAttributes
import android.net.Uri
import android.os.Build
import android.text.Html
import android.util.Log
import android.widget.RemoteViews
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import id.co.pspmobile.R
import id.co.pspmobile.data.local.SharePreferences
import id.co.pspmobile.ui.HomeActivity
import id.co.pspmobile.ui.main.MainActivity
import kotlinx.coroutines.runBlocking
import org.json.JSONObject
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.Date

class FirebaseService : FirebaseMessagingService() {

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
        Log.d("Firebase onNewToken","token "+ p0)
        if (!p0.isNullOrEmpty()) {
            SharePreferences.saveFbToken(this, p0)
        }
        Log.d("Success save token", SharePreferences.getFbToken(this).toString())
    }

    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)
        try {
            var title = p0.notification?.title
            var subtitle = ""
            var date = ""
            var body = ""
            val type = p0.data["type"]//bundle?.getString("type").toString()
            val content = p0.data["content"]//bundle?.getString("content").toString()
//            var images: List<String> = listOf()
//
            Log.d("content", p0.data.toString())

            when (type) {
                "invoice" -> {
                    val jsonArray = JSONObject(content)
                    body = p0.data["body"].toString()
                    if (title == "") {
                        title = jsonArray.optString("title").toString()
                    }
                }

                "notification" -> {
                    val jsonArray = JSONObject(content)
                    body = p0.data["body"].toString()
                    title = jsonArray.optString("title").toString()
                }

                "academic-calendar" -> {
                    val jsonArray = JSONObject(content)
                    body = jsonArray.optString("message").toString()
                    title = "Kalender Akademik"
                }

                "calendar-academic" -> {
                    val jsonArray = JSONObject(content)
                    body = jsonArray.optString("message").toString()
                    title = "Kalender Akademik"
                }

                "broadcast-text" -> {
                    val jsonArray = JSONObject(content)
                    body = jsonArray.optString("message").toString()
                    title = jsonArray.optString("title").toString()
                    subtitle = jsonArray.optString("subtitle").toString()
                    val start = if (!jsonArray.optString("startTime")
                            .isNullOrEmpty()
                    ) jsonArray.optString("startTime")
                    else jsonArray.optString("dateTime")
                    val end = if (!jsonArray.optString("endTime")
                            .isNullOrEmpty()
                    ) jsonArray.optString("endTime")
                    else jsonArray.optString("dateTime")
                    date = "Dari: $start \nSampai: $end"
                }

                "broadcast-image" -> {
                    val jsonArray = JSONObject(content)
                    Log.d("jsonArray", jsonArray.toString())
                    body = jsonArray.optString("images").toString()
                    title = jsonArray.optString("title").toString()
                    subtitle = jsonArray.optString("subtitle").toString()
                    val start = if (!jsonArray.optString("startTime")
                            .isNullOrEmpty()
                    ) jsonArray.optString("startTime")
                    else jsonArray.optString("dateTime")
                    val end = if (!jsonArray.optString("endTime")
                            .isNullOrEmpty()
                    ) jsonArray.optString("endTime")
                    else jsonArray.optString("dateTime")
                    date = "Dari: $start \nSampai: $end"
                }
            }
            Log.d("titleToShow", title.toString())
            Log.d("bodyToShow", body)
//            var field = p0.data["field"]
            val intent = Intent("FirebaseFunction")
            intent.putExtra("title", title)
            intent.putExtra("subtitle", subtitle)
            intent.putExtra("date", date)
            intent.putExtra("body", body)
            intent.putExtra("type", type)
            intent.putExtra("content", content)
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
            generateNotification(applicationContext, title.toString(), body.toString(),type.toString())

            if (p0.getNotification() != null) {
                Log.e("", "Notification Body: " + p0.getNotification()!!.getBody());
                //Show Notfication
                generateNotification(applicationContext, title.toString(), subtitle.toString(),type.toString())

            }
        } catch (ex: Exception) {
            Log.wtf("onMessageReceivedEX", ex.message.toString())
        }
    }

    fun generateNotification(context: Context, title: String, message: String,type:String) {
        val notifyIntent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        notifyIntent.putExtra("title", title)
        notifyIntent.putExtra("id", "notification")
        notifyIntent.putExtra("type", type)
        notifyIntent.putExtra("message", message)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )
        val intent = Intent("myFunction")
        intent.putExtra("title", title)
        notifyIntent.putExtra("id", "notification")
        intent.putExtra("body",message)
        intent.putExtra("type", "openActivity")

        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)

        try {
            val channel_id = createNotificationChannel(context)
//            val sound =
//                Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE.toString() + "://" + context.packageName + "/" + R.raw.sound_notification) //Here is FILE_NAME is the name of file that you want to play

            val notificationBuilder = NotificationCompat.Builder(
                context,
                channel_id!!
            )
                .setContentTitle(title)
                .setContentText(message)
                .setStyle(
                    NotificationCompat.BigTextStyle().bigText(message)
                ) /*.setLargeIcon(largeIcon)*/
                .setSmallIcon(getNotificationIcon()) //needs white icon with transparent BG (For all platforms)
                .setColor(ContextCompat.getColor(context, R.color.primary))
                .setVibrate(longArrayOf(1000, 1000))
                .setContentIntent(pendingIntent)
                .setPriority(Notification.PRIORITY_MAX)
                .setAutoCancel(true)
            val notificationManager =
                context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                notificationManager.notify(
                    (Date(System.currentTimeMillis()).time / 1000L % Int.MAX_VALUE).toInt() /* ID of notification */,
                    notificationBuilder.build()
                )
            } else {
                notificationManager.notify(
                    (Date(System.currentTimeMillis()).time / 1000L % Int.MAX_VALUE).toInt() /* ID of notification */,
                    notificationBuilder.getNotification()
                );
            }

            notificationManager.notify(
                (Date(System.currentTimeMillis()).time / 1000L % Int.MAX_VALUE).toInt() /* ID of notification */,
                notificationBuilder.build()
            )

        } catch (e: Exception) {

        }

    }

    fun showNotificationCenter(
        context: Context,
        title: String?,
        messageBody: String?,
        imageUrl: String?,
        titleUrl: String?,
        url: String?,
        date: String?,
        type: String?
    ) {
        val notifyIntent = Intent(this, HomeActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        notifyIntent.putExtra("title", title)
        notifyIntent.putExtra("id", "notification")
        notifyIntent.putExtra("message", messageBody)
        notifyIntent.putExtra("image", imageUrl)
        notifyIntent.putExtra("titleUrl", titleUrl)
        notifyIntent.putExtra("url", url)
        notifyIntent.putExtra("date", date)
        notifyIntent.putExtra("type", type)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )

        try {
            Log.e("imageURL", imageUrl.toString())
//            val sound =
//                Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE.toString() + "://" + context.packageName + "/" + R.raw.sound_notification) //Here is FILE_NAME is the name of file that you want to play

            if (!imageUrl.isNullOrEmpty()) {
                val url = URL(imageUrl)
                val `in`: InputStream
                val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
                connection.setDoInput(true)
                connection.connect()
                `in` = connection.getInputStream()
                val image: Bitmap = BitmapFactory.decodeStream(`in`)
                val notificationCompat = NotificationCompat.BigPictureStyle().bigPicture(image)

                val channel_id = createNotificationChannel(context)

                val notificationBuilder = NotificationCompat.Builder(
                    context,
                    channel_id!!
                )
                    .setContentTitle(title)
                    .setContentText(messageBody)
                    .setStyle(
                        notificationCompat
                    ) /*.setLargeIcon(largeIcon)*/
                    .setSmallIcon(getNotificationIcon()) //needs white icon with transparent BG (For all platforms)
                    .setColor(ContextCompat.getColor(context, R.color.primary))
                    .setVibrate(longArrayOf(1000, 1000))
                    .setContentIntent(pendingIntent)
                    .setPriority(Notification.PRIORITY_MAX)
                    .setAutoCancel(true)
                val notificationManager =
                    context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    notificationManager.notify(
                        (Date(System.currentTimeMillis()).time / 1000L % Int.MAX_VALUE).toInt() /* ID of notification */,
                        notificationBuilder.build()
                    )
                } else {
                    notificationManager.notify(
                        (Date(System.currentTimeMillis()).time / 1000L % Int.MAX_VALUE).toInt() /* ID of notification */,
                        notificationBuilder.getNotification()
                    );
                }

                notificationManager.notify(
                    (Date(System.currentTimeMillis()).time / 1000L % Int.MAX_VALUE).toInt() /* ID of notification */,
                    notificationBuilder.build()
                )
            } else {
                val channel_id = createNotificationChannel(context)

                val notificationBuilder = NotificationCompat.Builder(
                    context,
                    channel_id!!
                )
                    .setContentTitle(title)
                    .setContentText(messageBody)
                    .setStyle(
                        NotificationCompat.BigTextStyle().bigText(messageBody)
                    ) /*.setLargeIcon(largeIcon)*/
                    .setSmallIcon(getNotificationIcon()) //needs white icon with transparent BG (For all platforms)
                    .setColor(ContextCompat.getColor(context, R.color.primary))
                    .setVibrate(longArrayOf(1000, 1000))
                    .setContentIntent(pendingIntent)
                    .setPriority(Notification.PRIORITY_MAX)
                    .setAutoCancel(true)
                val notificationManager =
                    context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    notificationManager.notify(
                        (Date(System.currentTimeMillis()).time / 1000L % Int.MAX_VALUE).toInt() /* ID of notification */,
                        notificationBuilder.build()
                    )
                } else {
                    notificationManager.notify(
                        (Date(System.currentTimeMillis()).time / 1000L % Int.MAX_VALUE).toInt() /* ID of notification */,
                        notificationBuilder.getNotification()
                    );
                }

                notificationManager.notify(
                    (Date(System.currentTimeMillis()).time / 1000L % Int.MAX_VALUE).toInt() /* ID of notification */,
                    notificationBuilder.build()
                )
            }

        } catch (e: Exception) {

        }

    }

    fun createNotificationChannel(context: Context): String? {
//        val sound =
//            Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE.toString() + "://" + context.packageName + "/" + R.raw.sound_notification) //Here is FILE_NAME is the name of file that you want to play

        // NotificationChannels are required for Notifications on O (API 26) and above.
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            // The id of the channel.
            val channelId = "123"
            // The user-visible name of the channel.
            val channelName: CharSequence = "PSP Mobile"
            // The user-visible description of the channel.
            val channelDescription = "PSP Mobile Notification"
            val channelImportance = NotificationManager.IMPORTANCE_HIGH
            val channelEnableVibrate = true
            val audioAttributes = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_ALARM)
                .build()
            // Initializes NotificationChannel.
            val notificationChannel = NotificationChannel(channelId, channelName, channelImportance)
            notificationChannel.description = channelDescription
            notificationChannel.enableVibration(channelEnableVibrate)
            notificationChannel.setShowBadge(true)
//            notificationChannel.setSound(sound, audioAttributes)
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);

            // Adds NotificationChannel to system. Attempting to create an existing notification
            // channel with its original values performs no operation, so it's safe to perform the
            // below sequence.
            val notificationManager =
                (context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager)
            notificationManager.createNotificationChannel(notificationChannel)
            channelId
        } else {
            // Returns null for pre-O (26) devices.
            null
        }
    }

    private fun getNotificationIcon(): Int {
        val useWhiteIcon =
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD_MR1
        return if (useWhiteIcon) R.drawable.ic_firebase_notif else R.drawable.ic_firebase_notif
    }

    fun subscribeTopic(context: Context, topic: String) {
        FirebaseMessaging.getInstance().subscribeToTopic(topic).addOnSuccessListener {
            Log.d("FirebaseService", "Subscribed $topic")
//            Toast.makeText(context, "Subscribed $topic", Toast.LENGTH_LONG).show()
        }.addOnFailureListener {
            Log.d("FirebaseService", "Failed to Subscribe $topic")
//            Toast.makeText(context, "Failed to Subscribe $topic", Toast.LENGTH_LONG).show()
        }
    }

    fun unsubscribeTopic(context: Context, topic: String) {
        FirebaseMessaging.getInstance().unsubscribeFromTopic(topic).addOnSuccessListener {
//            Toast.makeText(context, "Unsubscribed $topic", Toast.LENGTH_LONG).show()
        }.addOnFailureListener {
//            Toast.makeText(context, "Failed to Unsubscribe $topic", Toast.LENGTH_LONG).show()
        }
    }
}