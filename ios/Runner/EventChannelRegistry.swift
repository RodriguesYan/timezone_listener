import Flutter
import Foundation

class EventChannelRegistry {
  static let shared = EventChannelRegistry()
  
  private init() {}
  
  func setupEventChannels(with controller: FlutterViewController) {
    let timezoneChannel = FlutterEventChannel(
      name: "timeHandlerEvent",
      binaryMessenger: controller.binaryMessenger
    )
    timezoneChannel.setStreamHandler(TimezoneListener())
  }
}
