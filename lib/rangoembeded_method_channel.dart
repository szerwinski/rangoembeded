import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';
import 'package:rangoembeded/dto/last_transaction.dart';
import 'package:rangoembeded/dto/payment.dart';

import 'rangoembeded_platform_interface.dart';

/// An implementation of [RangoembededPlatform] that uses method channels.
class MethodChannelRangoembeded extends RangoembededPlatform {
  /// The method channel used to interact with the native platform.
  @visibleForTesting
  final methodChannel = const MethodChannel('rangoembeded');

  @visibleForTesting
  final post7channel = const MethodChannel('post7channel');

  @override
  Future<String?> getPlatformVersion() async {
    final version =
        await methodChannel.invokeMethod<String>('getPlatformVersion');
    return version;
  }

  @override
  Future<String?> payment(Payment data) async {
    var operation = 0;
    switch (data.type) {
      case PaymentType.debito:
        operation = 2;
        break;
      case PaymentType.voucher:
        operation = 4;
        break;
      default:
        operation = 1;
        break;
    }
    // final post = await methodChannel.invokeMethod('getLastTransaction');
    final response = await methodChannel.invokeMethod<String>(
        'payment', <String, dynamic>{
      "id": data.id,
      "amount": data.amount,
      "type": operation
    });
    return response;
  }

  @override
  Future<LastTransactions?> lastTransaction() async {
    final response = await methodChannel.invokeMethod<LastTransactions>("last");
    return response;
  }

  @override
  Future<String?> undoing(Payment data) async {
    final response = await methodChannel.invokeMethod<String?>("undoing");
    return response;
  }

  @override
  Future<String?> cancel(Payment data) async {
    var operation = 0;
    switch (data.type) {
      case PaymentType.debito:
        operation = 2;
        break;
      case PaymentType.voucher:
        operation = 4;
        break;
      default:
        operation = 1;
        break;
    }
    final response = await methodChannel.invokeMethod<String?>(
        "cancel", <String, dynamic>{
      "id": data.id,
      "amount": data.amount,
      "type": operation
    });
    return response;
  }
}
