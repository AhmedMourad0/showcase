package dev.ahmedmourad.showcase.common

import dev.ahmedmourad.showcase.common.utils.Message

actual fun toast(msg: Message) = toast(msg.v)
actual fun toast(msg: String) = Unit
