package dev.ahmedmourad.showcase.common

import dev.icerock.moko.resources.desc.StringDesc

actual fun StringDesc.get(): String = this.localized()
