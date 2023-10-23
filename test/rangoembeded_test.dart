// import 'package:flutter_test/flutter_test.dart';
// import 'package:rangoembeded/rangoembeded.dart';
// import 'package:rangoembeded/rangoembeded_platform_interface.dart';
// import 'package:rangoembeded/rangoembeded_method_channel.dart';
// import 'package:plugin_platform_interface/plugin_platform_interface.dart';

// class MockRangoembededPlatform
//     with MockPlatformInterfaceMixin
//     implements RangoembededPlatform {

//   @override
//   Future<String?> getPlatformVersion() => Future.value('42');
// }

// void main() {
//   final RangoembededPlatform initialPlatform = RangoembededPlatform.instance;

//   test('$MethodChannelRangoembeded is the default instance', () {
//     expect(initialPlatform, isInstanceOf<MethodChannelRangoembeded>());
//   });

//   test('getPlatformVersion', () async {
//     Rangoembeded rangoembededPlugin = Rangoembeded();
//     MockRangoembededPlatform fakePlatform = MockRangoembededPlatform();
//     RangoembededPlatform.instance = fakePlatform;

//     expect(await rangoembededPlugin.getPlatformVersion(), '42');
//   });
// }
