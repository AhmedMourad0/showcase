package dev.ahmedmourad.showcase.common.utils

import android.content.Intent
import android.database.Cursor
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.lifecycle.*
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dev.ahmedmourad.showcase.common.Parcelable
import dev.ahmedmourad.showcase.common.compose.theme.LocalThemeManager

val LocalActivity = staticCompositionLocalOf<AppCompatActivity> {
    error("CompositionLocal LocalActivity not present")
}

@Composable
fun LifecycleEffect(event: Lifecycle.Event, skip: Int = 1, handler: () -> Unit) {
    val lifecycleOwner = LocalLifecycleOwner.current
    var skipped by remember { mutableIntStateOf(0) }
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, e ->
            if (e == event) {
                if (skipped < skip) {
                    skipped += 1
                } else {
                    handler()
                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}

@Composable
fun SetupSystemBarColors(
    color: Color? = null,
    useDarkIcons: Boolean? = null,
) {
    val systemUiController = rememberSystemUiController()
    val actualColor = color ?: MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp)
    val actualUseDarkIcons = useDarkIcons ?: !LocalThemeManager.current.collectIsDarkAsState()
    LaunchedEffect(systemUiController, actualUseDarkIcons, actualColor) {
        systemUiController.setSystemBarsColor(
            color = actualColor,
            darkIcons = actualUseDarkIcons
        )
    }
}

fun Cursor.asSequence(): Sequence<Cursor> {
    return sequence {
        this@asSequence.use {
            if (this@asSequence.moveToFirst()) {
                do {
                    yield(this@asSequence)
                } while (this@asSequence.moveToNext())
            }
        }
    }
}

inline fun <VM : ViewModel> viewModelFactory(
    crossinline create: (handle: SavedStateHandle) -> VM,
): ViewModelProvider.Factory {
    return object : AbstractSavedStateViewModelFactory() {
        override fun <T : ViewModel> create(
            key: String,
            modelClass: Class<T>,
            handle: SavedStateHandle,
        ): T {
            @Suppress("UNCHECKED_CAST")
            return create(handle) as T
        }
    }
}

inline fun <reified T : Parcelable> Intent.getParcelableExtraSafe(key: String): T? = when {
    Build.VERSION.SDK_INT >= 33 -> getParcelableExtra(key, T::class.java)
    else -> @Suppress("DEPRECATION") getParcelableExtra(key)
}

inline fun <reified T : Parcelable> Bundle.getParcelableSafe(key: String): T? = when {
    Build.VERSION.SDK_INT >= 33 -> getParcelable(key, T::class.java)
    else -> @Suppress("DEPRECATION") getParcelable(key)
}

inline fun <reified T : Parcelable> Intent.getParcelableArrayListExtraSafe(key: String): List<T>? = when {
    Build.VERSION.SDK_INT >= 33 -> getParcelableArrayListExtra(key, T::class.java)
    else -> @Suppress("DEPRECATION") getParcelableArrayListExtra(key)
}

inline fun <reified T : Parcelable> Bundle.getParcelableArrayListSafe(key: String): List<T>? = when {
    Build.VERSION.SDK_INT >= 33 -> getParcelableArrayList(key, T::class.java)
    else -> @Suppress("DEPRECATION") getParcelableArrayList(key)
}
