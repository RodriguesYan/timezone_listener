import Flutter
import UIKit
import Foundation

@main
@objc class AppDelegate: FlutterAppDelegate {
  override func application(
    _ application: UIApplication,
    didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?
  ) -> Bool {
    let controller : FlutterViewController = window?.rootViewController as! FlutterViewController  
    let eventChannel = FlutterEventChannel(
      name: "timeHandlerEvent", 
      binaryMessenger: controller.binaryMessenger
    )

    eventChannel.setStreamHandler(TimezoneListener())

    GeneratedPluginRegistrant.register(with: self)
    return super.application(application, didFinishLaunchingWithOptions: launchOptions)
  }
}

class TimezoneListener: NSObject, FlutterStreamHandler {
  var eventSink: FlutterEventSink?
  private var observer: Any?
  
  func onListen(withArguments arguments: Any?, eventSink events: @escaping FlutterEventSink) -> FlutterError? {
    self.eventSink = events
    
    // Register for timezone change notifications
    observer = NotificationCenter.default.addObserver(
      forName: NSNotification.Name.NSSystemTimeZoneDidChange,
      object: nil,
      queue: OperationQueue.main
    ) { [weak self] _ in
      let timezone = TimeZone.current.identifier
      self?.eventSink?(timezone)
    }
    
    let timezone = TimeZone.current.identifier
    events(timezone)
    
    return nil
  }
  
  func onCancel(withArguments arguments: Any?) -> FlutterError? {
    if let observer = observer {
      NotificationCenter.default.removeObserver(observer)
      self.observer = nil
    }
    self.eventSink = nil
    return nil
  }
}