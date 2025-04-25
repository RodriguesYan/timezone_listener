package samples.flutter.devbattery.battery_connection_2

import android.content.BroadcastReceiver
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import androidx.annotation.NonNull
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.EventChannel
import io.flutter.plugin.common.MethodChannel
import java.util.TimeZone


class MainActivity: FlutterActivity() {
    private val eventChannel = "timeHandlerEvent"

    override fun configureFlutterEngine(@NonNull flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)
        EventChannel(flutterEngine.dartExecutor.binaryMessenger, eventChannel).setStreamHandler(
            TimezoneListener(context)
        )
    }

    class TimezoneListener(private val context: Context) : EventChannel.StreamHandler {
        private var eventSink: EventChannel.EventSink? = null
        private var receiver: BroadcastReceiver? = null

        override fun onListen(arguments: Any?, events: EventChannel.EventSink?) {
            eventSink = events
            val intentFilter = IntentFilter(Intent.ACTION_TIMEZONE_CHANGED)
            receiver = object : BroadcastReceiver() {
                override fun onReceive(context: Context, intent: Intent) {
                    val timezone = TimeZone.getDefault().id
                    eventSink?.success(timezone)
                }
            }
            context.registerReceiver(receiver, intentFilter)
        }

        override fun onCancel(arguments: Any?) {
            eventSink = null
            if (receiver != null) {
                context.unregisterReceiver(receiver)
                receiver = null
            }
        }
    }
}