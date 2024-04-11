import 'package:plugin_platform_interface/plugin_platform_interface.dart';
import 'package:rangoembeded/dto/payment.dart';

import 'rangoembeded_method_channel.dart';

abstract class RangoembededPlatform extends PlatformInterface {
  /// Constructs a RangoembededPlatform.
  RangoembededPlatform() : super(token: _token);

  static final Object _token = Object();

  static RangoembededPlatform _instance = MethodChannelRangoembeded();

  /// The default instance of [RangoembededPlatform] to use.
  ///
  /// Defaults to [MethodChannelRangoembeded].
  static RangoembededPlatform get instance => _instance;

  /// Platform-specific implementations should set this with their own
  /// platform-specific class that extends [RangoembededPlatform] when
  /// they register themselves.
  static set instance(RangoembededPlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Future<String?> getPlatformVersion() {
    throw UnimplementedError('platformVersion() has not been implemented.');
  }

  Future<String?> payment(Payment data) async {
    throw UnimplementedError('payment() has not been implemented.');
  }

    Future<String?> cancel(Payment data) {
    throw UnimplementedError('cancel has not been implemented.');
  }

  Future<String?> cancelPix(Payment data) {
    throw UnimplementedError('cancel_pix has not been implemented.');
  }
}
