package samples.flutter.devbattery.battery_connection_2

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import io.flutter.plugin.common.EventChannel
import java.util.TimeZone

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
        
        events?.success(TimeZone.getDefault().id)
    }

    override fun onCancel(arguments: Any?) {
        eventSink = null
        if (receiver != null) {
            context.unregisterReceiver(receiver)
            receiver = null
        }
    }
}
