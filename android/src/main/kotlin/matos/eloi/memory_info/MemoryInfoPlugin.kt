package matos.eloi.memory_info

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.Context.ACTIVITY_SERVICE
import androidx.annotation.NonNull

import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import kotlin.math.floor
import kotlin.math.ln
import kotlin.math.pow

/** MemoryInfoPlugin */
class MemoryInfoPlugin : FlutterPlugin, MethodCallHandler, ActivityAware {
    /// The MethodChannel that will the communication between Flutter and native Android
    ///
    /// This local reference serves to register the plugin with the Flutter Engine and unregister it
    /// when the Flutter Engine is detached from the Activity
    private lateinit var channel: MethodChannel
    private lateinit var context: Context
    private lateinit var activity: Activity

    override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        channel = MethodChannel(flutterPluginBinding.binaryMessenger, "memory_info")
        channel.setMethodCallHandler(this)
        context = flutterPluginBinding.applicationContext
    }

    override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
        when (call.method) {
            "getMemoryUsage" -> result.success(getMemoryUsage())
            else -> result.notImplemented()
        }
    }

    override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
        channel.setMethodCallHandler(null)
    }

    private fun getMemoryUsage(): HashMap<String, String> {
        val activityManager = activity.getSystemService(ACTIVITY_SERVICE) as ActivityManager
        val memoryInfo = ActivityManager.MemoryInfo()
        activityManager.getMemoryInfo(memoryInfo)
        val memoryUsageInfo = MemoryUsageInfo(
            totalMemory = formatMemorySize(memoryInfo.totalMem),
            availableMemory = formatMemorySize(memoryInfo.availMem)
        )

        return hashMapOf(
            "totalMemory" to memoryUsageInfo.totalMemory,
            "availableMemory" to memoryUsageInfo.availableMemory,
        )
    }

    private fun formatMemorySize(memorySize: Long): String {
        val k = 1024
        val sizes = listOf(
            "Bytes", "KB", "MB", "GB", "TB", "PB", "EB", "ZB", "YB"
        )
        val i = floor(ln(memorySize.toDouble()) / ln(k.toDouble()))
        val memory = (memorySize / k.toDouble().pow(i))
        return String.format("%.2f${sizes[i.toInt()]}", memory)
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

data class MemoryUsageInfo(
    val totalMemory: String,
    val availableMemory: String,
)
