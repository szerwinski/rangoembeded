import 'dart:math';

import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:rangoembeded/dto/payment.dart';
import 'package:rangoembeded/rangoembeded.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatefulWidget {
  const MyApp({super.key});

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _platformVersion = 'Unknown';
  String _post7 = "creusa";
  final _rangoembededPlugin = Rangoembeded();

  @override
  void initState() {
    super.initState();
    initPlatformState();
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> initPlatformState() async {
    String platformVersion;
    String post7;
    Random random = Random();
    var payment = Payment(id: random.nextInt(9999).toString(), type: PaymentType.pix, amount: "12000");
    // Platform messages may fail, so we use a try/catch PlatformException.
    // We also handle the message potentially returning null.
    try {
      platformVersion = await _rangoembededPlugin.getPlatformVersion() ?? 'Unknown platform version';
       post7 = await _rangoembededPlugin.payment(payment) ?? "faiou";  
    } on PlatformException catch (err)  {
       platformVersion = 'Failed to get platform version.';
       debugPrint(err.message);
       debugPrint(err.details);
       post7 =  err.code;
    }

    // If the widget was removed from the tree while the asynchronous platform
    // message was in flight, we want to discard the reply rather than calling
    // setState to update our non-existent appearance.
    if (!mounted) return;

    setState(() {
      _platformVersion = platformVersion;
      _post7 = post7;
    });
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: Text('Plugin example app $_post7'),
        ),
        body: Center(
          child: Text('Running on: $_platformVersion\n'),
        ),
      ),
    );
  }
}
