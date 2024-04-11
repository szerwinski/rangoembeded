import 'package:rangoembeded/dto/payment.dart';

import 'rangoembeded_platform_interface.dart';

class Rangoembeded {
  Future<String?> getPlatformVersion() {
    return RangoembededPlatform.instance.getPlatformVersion();
  }

  Future<String?> payment(Payment data) {
    return RangoembededPlatform.instance.payment(data);
  }

  Future<String?> cancel(Payment data) {
    return RangoembededPlatform.instance.cancel(data);
  }

    Future<String?> cancelPix(Payment data) {
    return RangoembededPlatform.instance.cancelPix(data);
  }
}
