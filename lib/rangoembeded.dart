import 'package:rangoembeded/dto/payment.dart';

import 'dto/last_transaction.dart';
import 'rangoembeded_platform_interface.dart';

class Rangoembeded {
  Future<String?> getPlatformVersion() {
    return RangoembededPlatform.instance.getPlatformVersion();
  }

  Future<String?> payment(Payment data) {
    return RangoembededPlatform.instance.payment(data);
  }

  Future<LastTransactions?> lastTransaction() async {
    return RangoembededPlatform.instance.lastTransaction();
  }

  Future<String?> undoing(Payment data) {
    return RangoembededPlatform.instance.undoing(data);
  }

  Future<String?> cancel(Payment data) {
    return RangoembededPlatform.instance.cancel(data);
  }
}
