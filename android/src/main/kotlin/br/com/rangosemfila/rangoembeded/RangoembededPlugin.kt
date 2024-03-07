package br.com.rangosemfila.rangoembeded

import android.app.Activity
import android.content.Context
import ger7.com.br.pos7api.LastTransaction
import ger7.com.br.pos7api.POS7API
import ger7.com.br.pos7api.ParamIn
import ger7.com.br.pos7api.ParamOut
import ger7.com.br.pos7api.TransactionReport
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result


/** RangoembededPlugin */
class RangoembededPlugin : FlutterPlugin, MethodCallHandler, ActivityAware {
    /// The MethodChannel that will the communication between Flutter and native Android
    ///
    /// This local reference serves to register the plugin with the Flutter Engine and unregister it
    /// when the Flutter Engine is detached from the Activity
    private lateinit var context: Context
    private lateinit var channel: MethodChannel
    private lateinit var post7: POS7API
    private lateinit var activity: Activity

    override fun onAttachedToEngine(flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        context = flutterPluginBinding.applicationContext
        post7 = POS7API(context)
        channel = MethodChannel(flutterPluginBinding.binaryMessenger, "rangoembeded")
        channel.setMethodCallHandler(this)

    }

    private fun responseHandler(out: ParamOut, result: Result) {
        when (out.response) {
            0 -> {
                result.success("APROVADA: ${out.resPrint}")
            }

            1 -> {
                result.error("NEGADA: ${out.resDisplay} ", out.resDisplay, out.resDisplay)
            }

            2 -> {
                result.error("CANCELADA: ${out.resDisplay} ", out.resDisplay, out.resDisplay)
            }

            3 -> {
                result.error("FALHOU: ${out.resDisplay} ", out.resDisplay, out.resDisplay)
            }

            else -> {
                result.error(
                    "INDISPONIVEL",
                    "Houve falha sistemica",
                    "Houve um erro durante a transação"
                )
            }
        }
    }

    override fun onMethodCall(call: MethodCall, result: Result) {
        when (call.method) {
            "getPlatformVersion" -> {
                result.success("Android ${android.os.Build.VERSION.RELEASE}")
            }

            "payment" -> {
                println(call.arguments)
                val req = ParamIn()
                req.setTrsProduct(call.argument("type")!!)
                req.trsInstallments = 1
                req.trsId = call.argument("id")
                req.isTrsReceipt = false
                req.trsAmount = call.argument("amount")
                post7.processTransaction(req) { paramOut ->
                    responseHandler(paramOut, result)
                }
            }

            "last" -> {
                val lr: LastTransaction = post7.lastTransaction
                result.success(lr)
            }

            "undoing" -> {
                //desfazimento
                val lr: LastTransaction = post7.lastTransaction
                if (lr.trsStatus == LastTransaction.TransactionStatus.NOT_FOUND) {
                    result.error("UNDOING", "Não há transações a serem desfeitas", null)
                } else {
                    if (lr.trsStatus == LastTransaction.TransactionStatus.APPROVED || lr.trsStatus == LastTransaction.TransactionStatus.NO_ANSWER) {
                        val req = ParamIn()
                        req.setTrsProduct(20)
                        req.trsInstallments = 1
                        req.trsId = call.argument("id")
                        req.isTrsReceipt = false
                        req.trsAmount = call.argument("amount")
                        post7.processTransaction(req) { paramOut ->
                            responseHandler(paramOut, result)
                        }
                    }
                }
            }

            "cancel" -> {
                val req = ParamIn()
                req.setTrsProduct(call.argument("type")!!)
                req.trsInstallments = 1
                req.trsId = call.argument("id")
                req.isTrsReceipt = true
                req.merchantPwd = false
                req.setTrsType(2)
                req.trsAmount = call.argument("amount")
                post7.processTransaction(req) { paramOut ->
                    responseHandler(paramOut, result)
                }
            }

            "transacoes" -> {
                val logTrs = TransactionReport().report
                if (logTrs == null) {
                    result.error("TRSLOG: null", "TRSLOG null", "TRSLOG null")
                } else {
                    result.success(logTrs)
                }
            }

            "ping" -> {
                result.success("pong")
            }

            else -> {
                result.error("Unknown method", "O método especificado não é conhecido", null)
            }
        }
    }

    override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        channel.setMethodCallHandler(null)
    }

    override fun onAttachedToActivity(binding: ActivityPluginBinding) {
        activity = binding.activity
    }

    override fun onDetachedFromActivityForConfigChanges() {
        TODO("Not yet implemented")
    }

    override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
        TODO("Not yet implemented")
    }

    override fun onDetachedFromActivity() {
        TODO("Not yet implemented")
    }


}
