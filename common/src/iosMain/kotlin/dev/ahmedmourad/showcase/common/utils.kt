package dev.ahmedmourad.showcase.common

import platform.Foundation.NSUUID.Companion.UUID
import platform.UIKit.UIApplication
import platform.UIKit.UIUserInterfaceLayoutDirection

actual fun randomUUID(): String = UUID().UUIDString

actual fun isRtl(): Boolean {
    return UIApplication.sharedApplication.userInterfaceLayoutDirection ==
            UIUserInterfaceLayoutDirection.UIUserInterfaceLayoutDirectionRightToLeft
}
