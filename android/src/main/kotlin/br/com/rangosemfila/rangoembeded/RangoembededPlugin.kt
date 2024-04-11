package br.com.rangosemfila.rangoembeded

import android.app.Activity
import android.content.Context


import ger7.com.br.pos7api.POS7API
import ger7.com.br.pos7api.ParamIn
import ger7.com.br.pos7api.ParamOut

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
                req.setTrsProduct(call.argument("type")!!) //tipo da transação 1. credito, 2. debito, 4. voucher, 8. pix
                req.trsInstallments = 0 //numero de parcelas
                req.trsInstMode = 0 //modo de parcelamento
                req.trsId = call.argument("id") //id da transação
                req.isTrsReceipt = true //imprime o comprovante
                req.trsAmount = call.argument("amount") //valor em inteiro
                req.setTrsType(1) //1 pagamento, 2 cancelmaneto
                post7.processTransaction(req) { paramOut ->
                    responseHandler(paramOut, result)
                }
            }
            "cancel" -> {
                val req = ParamIn()
                req.setTrsProduct(call.argument("type")!!) //tipo da transação 1. credito, 2. debito, 4. voucher
                req.trsInstallments = 0 //numero de parcelas
                req.trsInstMode = 0 //modo de parcelamento
                req.trsId = call.argument("id") //id da transação
                req.isTrsReceipt = true //imprime o comprovante
                req.merchantPwd = false //senha do lojista para cancelamento
                req.setTrsType(2) // 1 pagamento, 2 cancelmaneto
                req.trsAmount = call.argument("amount") //valor em inteiro
                post7.processTransaction(req) { paramOut ->
                    responseHandler(paramOut, result)
                }
            }

            "cancel_pix" -> {
                val req = ParamIn()
                req.setTrsProduct(8) //tipo da transação 1. credito, 2. debito, 4. voucher
                req.trsInstallments = 0 //numero de parcelas
                req.trsInstMode = 0 //modo de parcelamento
                req.trsId = call.argument("id") //id da transação
                req.isTrsReceipt = true //imprime o comprovante
                req.merchantPwd = false //senha do lojista para cancelamento
                req.setTrsType(21) // 21 cancelamento pix
                req.trsAmount = call.argument("amount") //valor em inteiro
                post7.processTransaction(req) { paramOut ->
                    responseHandler(paramOut, result)
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
