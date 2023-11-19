package dev.ahmedmourad.showcase.common

import android.view.View
import dev.ahmedmourad.showcase.common.initializers.appCtx
import java.util.UUID

actual fun randomUUID(): String = UUID.randomUUID().toString()

actual fun isRtl(): Boolean = appCtx.resources.configuration.layoutDirection == View.LAYOUT_DIRECTION_RTL
