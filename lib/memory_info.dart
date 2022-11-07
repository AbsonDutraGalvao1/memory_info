import 'package:memory_info/memory_info_usage.dart';

import 'memory_info_platform_interface.dart';

class MemoryInfo {
  Future<String?> getPlatformVersion() {
    return MemoryInfoPlatform.instance.getPlatformVersion();
  }

  Future<MemoryInfoUsage?> getMemoryUsage() {
    return MemoryInfoPlatform.instance.getMemoryUsage();
  }
}
