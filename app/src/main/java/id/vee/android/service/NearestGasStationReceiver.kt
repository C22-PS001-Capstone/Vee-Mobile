package id.vee.android.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofenceStatusCodes
import com.google.android.gms.location.GeofencingEvent
import id.vee.android.R
import id.vee.android.domain.model.Notification
import id.vee.android.domain.repository.VeeDataSource
import id.vee.android.ui.main.MainActivity
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import timber.log.Timber

@DelicateCoroutinesApi
class NearestGasStationReceiver : KoinComponent, BroadcastReceiver() {

    private val repository: VeeDataSource by inject()
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == ACTION_GEOFENCE_EVENT) {
            val geofencingEvent = GeofencingEvent.fromIntent(intent)

            if (geofencingEvent.hasError()) {
                val errorMessage =
                    GeofenceStatusCodes.getStatusCodeString(geofencingEvent.errorCode)
                Timber.e(errorMessage)
                sendNotification(context, errorMessage)
                return
            }
            val geofenceTransition = geofencingEvent.geofenceTransition

            if (
                geofenceTransition == Geofence.GEOFENCE_TRANSITION_DWELL ||
                geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER ||
                geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT
            ) {
                val geofenceTransitionString =
                    when (geofenceTransition) {
                        Geofence.GEOFENCE_TRANSITION_DWELL -> "Are you filling up your vehicle at %s?"
                        Geofence.GEOFENCE_TRANSITION_ENTER -> "You are near %s"
                        Geofence.GEOFENCE_TRANSITION_EXIT -> "You are leaving %s"
                        else -> "Invalid transition type"
                    }

                val triggeringGeofences = geofencingEvent.triggeringGeofences
                val requestId = triggeringGeofences[0].requestId

                val geofenceTransitionDetails = geofenceTransitionString.format(requestId)
                Timber.i(geofenceTransitionDetails)
                sendNotification(context, geofenceTransitionDetails)
            } else {
                val errorMessage = "Invalid transition type: $geofenceTransition"
                Timber.e(errorMessage)
                sendNotification(context, errorMessage)
            }
        }
    }

    private fun sendNotification(context: Context, geofenceTransitionDetail: String) {
        val mNotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notifyIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val notifyPendingIntent = PendingIntent.getActivity(
            context, 0, notifyIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val mBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle(geofenceTransitionDetail)
            .setContentText("Click here to add it to your activity")
            .setSmallIcon(R.drawable.ic_baseline_notifications_active_24)
            .setContentIntent(notifyPendingIntent)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
            mBuilder.setChannelId(CHANNEL_ID)
            mNotificationManager.createNotificationChannel(channel)
        }
        val notification = mBuilder.build()
        mNotificationManager.notify(NOTIFICATION_ID, notification)
        GlobalScope.launch(Dispatchers.IO) {
            repository.insertNotification(
                Notification(
                    id = null,
                    notification = geofenceTransitionDetail,
                    createdAt = System.currentTimeMillis()
                )
            )
        }
    }

    companion object {
        const val ACTION_GEOFENCE_EVENT = "GeofenceEvent"
        private const val CHANNEL_ID = "1"
        private const val CHANNEL_NAME = "Geofence Channel"
        private const val NOTIFICATION_ID = 1
    }
}