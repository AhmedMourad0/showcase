package dev.ahmedmourad.showcase.common.main

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.ui.window.ComposeUIViewController
import dev.ahmedmourad.showcase.common.PreferenceManager
import dev.ahmedmourad.showcase.common.home.HomeScreen
import dev.ahmedmourad.showcase.common.compose.theme.IosShowcaseTheme
import dev.ahmedmourad.showcase.common.defaultIsInDarkMode
import dev.ahmedmourad.showcase.common.ios.LocalObservableUIController
import dev.ahmedmourad.showcase.common.ios.ObservableUIController
import dev.ahmedmourad.showcase.common.ios.UIControllerEvent
import dev.ahmedmourad.showcase.common.ios.UIControllerObserver
import dev.ahmedmourad.showcase.common.ios.UIViewControllerDelegate
import dev.ahmedmourad.showcase.common.ios.embed
import dev.ahmedmourad.showcase.common.navigation.NavigationRoot
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import platform.UIKit.UIStatusBarStyleDarkContent
import platform.UIKit.UIStatusBarStyleLightContent
import platform.UIKit.UIViewController

@Stable
class RootViewControllerDelegate : UIViewControllerDelegate, ObservableUIController {

    private val prefManager = PreferenceManager()
    private val scope = CoroutineScope(Job() + Dispatchers.Main)
    private val lifecycleObservers = mutableStateMapOf<String, UIControllerObserver>()
    override var statusBarStyle = UIStatusBarStyleDarkContent

    override fun onViewDidLoad(self: UIViewController) {
        val controller = RootViewControllerScreen(this)
        self.embed(controller, into = self.view)
    }

    override fun onViewWillAppear(self: UIViewController) {
        prefManager.isInDarkMode(::defaultIsInDarkMode).onEach { dark ->
            statusBarStyle = if (dark) UIStatusBarStyleLightContent else UIStatusBarStyleDarkContent
            self.setNeedsStatusBarAppearanceUpdate()
        }.launchIn(scope)
    }

    override fun onViewWillDisappear(self: UIViewController) {
        lifecycleObservers.filter {
            it.value.event == UIControllerEvent.ViewWillDisappear
        }.forEach { it.value.onEvent() }
        scope.coroutineContext.cancelChildren()
    }

    override fun willMove(to: UIViewController?) {
        if (to == null) {
            lifecycleObservers.filter {
                it.value.event == UIControllerEvent.OnDestroy
            }.forEach { it.value.onEvent() }
        }
    }

    override fun addObserver(id: String, observer: UIControllerObserver) {
        lifecycleObservers[id] = observer
    }

    override fun removeObserver(id: String) {
        lifecycleObservers.remove(id)
    }
}

fun RootViewControllerScreen(controller: ObservableUIController) = ComposeUIViewController {
    CompositionLocalProvider(LocalObservableUIController provides controller) {
        IosShowcaseTheme {
            NavigationRoot(HomeScreen())
        }
    }
}
