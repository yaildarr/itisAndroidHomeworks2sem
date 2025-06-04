package ru.practice.homeworks

import android.Manifest
import android.app.ActivityManager
import android.app.ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND
import android.content.Intent
import android.util.Log
import androidx.annotation.RequiresPermission
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import ru.practice.homeworks.data.remote.ConvertApi
import ru.practice.homeworks.domain.usecase.HandleBackgroundUseCase
import javax.inject.Inject

@AndroidEntryPoint
class AppMessagingService : FirebaseMessagingService() {

    @Inject
    lateinit var notificationHandler: NotificationHandler

    @Inject
    lateinit var handleBackgroundUseCase: HandleBackgroundUseCase

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        val type = message.data[DATA_TYPE] ?: "-1"
        when (type) {
            "-1" -> {
            }
            "1" -> {
                val titleText = message.data[DATA_TITLE] ?: "title"
                val messageText = message.data[DATA_MESSAGE] ?: "message"
                notificationHandler.showNotification(titleText, messageText)
            }
            "2" -> {
                val data = message.data[DATA_DATA] ?: "unknown"
                CoroutineScope(Dispatchers.IO + Job()).launch {
                    handleBackgroundUseCase.invoke(data)
                }
            }
            "3" -> {
                if (isAppInForeground()){
                    val featureToOpen = message.data[KEY_FEATURE]
                    val intent = Intent(this, MainActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
                        putExtra(KEY_FEATURE,featureToOpen)
                    }
                    startActivity(intent)
                }
            }
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("MyLog","TOKEN - $token")
    }

    private fun isAppInForeground(): Boolean {
        val appProcessInfo = ActivityManager.RunningAppProcessInfo()
        ActivityManager.getMyMemoryState(appProcessInfo)
        return appProcessInfo.importance == IMPORTANCE_FOREGROUND
    }

    companion object{
        const val DATA_DATA = "data"
        const val DATA_TITLE = "title"
        const val DATA_MESSAGE = "message"
        const val DATA_TYPE = "type"
        const val KEY_FEATURE = "openFeature"
    }
}