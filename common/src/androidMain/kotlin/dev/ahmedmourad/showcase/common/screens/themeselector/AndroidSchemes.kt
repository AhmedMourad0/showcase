package dev.ahmedmourad.showcase.common.screens.themeselector

import android.os.Build
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import dev.ahmedmourad.showcase.common.initializers.appCtx

val AndroidSchemes = buildList {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        add(ThemeSchemes(
            id = "material_you",
            name = "Material You",
            light = dynamicLightColorScheme(appCtx),
            dark = dynamicDarkColorScheme(appCtx)
        ))
    }
    addAll(CommonSchemes)
}
