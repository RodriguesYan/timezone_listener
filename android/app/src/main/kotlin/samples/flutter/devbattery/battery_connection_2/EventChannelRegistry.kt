package samples.flutter.devbattery.battery_connection_2

import android.content.Context
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.EventChannel

class EventChannelRegistry private constructor() {
    companion object {
        val shared = EventChannelRegistry()
        private const val TIMEZONE_CHANNEL = "timeHandlerEvent"
    }

    fun setupEventChannels(binaryMessenger: BinaryMessenger, context: Context) {
        EventChannel(binaryMessenger, TIMEZONE_CHANNEL).setStreamHandler(
            TimezoneListener(context)
        )
    }
}
