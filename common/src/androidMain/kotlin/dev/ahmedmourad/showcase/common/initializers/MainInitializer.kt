package dev.ahmedmourad.showcase.common.initializers

import android.content.Context
import androidx.startup.Initializer

class MainInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        init(context)
    }
    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }
}
