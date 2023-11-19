package dev.ahmedmourad.showcase.common

import dev.ahmedmourad.showcase.common.initializers.appCtx
import dev.icerock.moko.resources.desc.StringDesc

actual fun StringDesc.get(): String = this.toString(appCtx)
