import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';
import 'package:memory_info/memory_info_usage.dart';

import 'memory_info_platform_interface.dart';

/// An implementation of [MemoryInfoPlatform] that uses method channels.
class MethodChannelMemoryInfo extends MemoryInfoPlatform {
  /// The method channel used to interact with the native platform.
  @visibleForTesting
  final methodChannel = const MethodChannel('memory_info');

  @override
  Future<String?> getPlatformVersion() async {
    final version =
        await methodChannel.invokeMethod<String>('getPlatformVersion');
    return version;
  }

  @override
  Future<MemoryInfoUsage?> getMemoryUsage() async {
    final memoryUsage =
        await methodChannel.invokeMethod<dynamic>('getMemoryUsage');
    return MemoryInfoUsage(
      memoryUsage!["availableMemory"] as String,
      memoryUsage["totalMemory"] as String,
    );
  }
}
