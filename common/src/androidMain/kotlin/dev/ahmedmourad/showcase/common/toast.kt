package dev.ahmedmourad.showcase.common

import android.widget.Toast
import dev.ahmedmourad.showcase.common.initializers.appCtx
import dev.ahmedmourad.showcase.common.utils.Message

actual fun toast(msg: Message) = toast(msg.v)

actual fun toast(msg: String) {
    Toast.makeText(appCtx, msg, Toast.LENGTH_LONG).show()
}
