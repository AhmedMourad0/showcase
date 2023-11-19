package dev.ahmedmourad.showcase.common.main

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.CompositionLocalProvider
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.ramcosta.composedestinations.DestinationsNavHost
import dev.ahmedmourad.showcase.common.R
import dev.ahmedmourad.showcase.common.compose.theme.AndroidShowcaseTheme
import dev.ahmedmourad.showcase.common.screens.home.NavGraphs
import dev.ahmedmourad.showcase.common.utils.LocalActivity
import dev.ahmedmourad.showcase.common.utils.SetupSystemBarColors

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_Showcase)
        setContent {
            CompositionLocalProvider(LocalActivity provides this@MainActivity) {
                AndroidShowcaseTheme {
                    SetupSystemBarColors()
                    DestinationsNavHost(navGraph = NavGraphs.main)
                }
            }
        }
    }
}
