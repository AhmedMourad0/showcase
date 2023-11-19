package dev.ahmedmourad.showcase.common.initializers

import android.app.Application
import android.content.Context

lateinit var appCtx: Application
    private set

fun init(context: Context) {
    appCtx = context.applicationContext as Application
}
